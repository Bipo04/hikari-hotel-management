package org.web.hikarihotelmanagement.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.config.VNPayConfig;
import org.web.hikarihotelmanagement.entity.Booking;
import org.web.hikarihotelmanagement.entity.Request;
import org.web.hikarihotelmanagement.entity.RoomAvailability;
import org.web.hikarihotelmanagement.enums.BookingStatus;
import org.web.hikarihotelmanagement.enums.RequestStatus;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.repository.BookingRepository;
import org.web.hikarihotelmanagement.repository.RequestRepository;
import org.web.hikarihotelmanagement.repository.RoomAvailabilityRepository;
import org.web.hikarihotelmanagement.repository.RoomAvailabilityRequestRepository;
import org.web.hikarihotelmanagement.service.CustomerTierService;
import org.web.hikarihotelmanagement.service.VNPayService;
import org.web.hikarihotelmanagement.util.VNPayUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayServiceImpl implements VNPayService {

    private final VNPayConfig vnPayConfig;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final RoomAvailabilityRequestRepository roomAvailabilityRequestRepository;
    private final CustomerTierService customerTierService;

    private static final TimeZone VN_TIMEZONE = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");

    @Override
    public String createPaymentUrl(Long bookingId, HttpServletRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException("Không tìm thấy booking"));

        if (booking.getStatus() != BookingStatus.PAYMENT_PENDING) {
            throw new ApiException("Booking không ở trạng thái chờ thanh toán");
        }

        long amount = booking.getAmount().longValue() * 100; // VNPay nhân 100

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", vnPayConfig.getVnpTmnCode());
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", booking.getBookingCode());
        vnpParams.put("vnp_OrderInfo", "Thanh toán booking: " + booking.getBookingCode());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnPayConfig.getVnpReturnUrl());
        vnpParams.put("vnp_IpAddr", getIpAddress(request));

        // --- Tạo thời gian theo GMT+7 ---
        Calendar cld = Calendar.getInstance(VN_TIMEZONE);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(VN_TIMEZONE);

        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        cld.add(Calendar.MINUTE, 15); // Hết hạn sau 15 phút
        String vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        // --- Tạo query string và secure hash ---
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append('=').append(encodeURIComponent(fieldValue));
                query.append(encodeURIComponent(fieldName)).append('=').append(encodeURIComponent(fieldValue));
                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getVnpHashSecret(), hashData.toString());
        query.append("&vnp_SecureHash=").append(vnpSecureHash);

        String paymentUrl = vnPayConfig.getVnpUrl() + "?" + query.toString();
        log.info("Payment URL: {}", paymentUrl);
        return paymentUrl;
    }

    private String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.US_ASCII.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public Map<String, String> handleCallback(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnpSecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // --- Xác thực chữ ký ---
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            hashData.append(fieldName).append('=').append(encodeURIComponent(fieldValue));
            if (itr.hasNext()) hashData.append('&');
        }

        String signValue = VNPayUtil.hmacSHA512(vnPayConfig.getVnpHashSecret(), hashData.toString());

        Map<String, String> result = new HashMap<>();
        if (signValue.equals(vnpSecureHash)) {
            String bookingCode = request.getParameter("vnp_TxnRef");
            String responseCode = request.getParameter("vnp_ResponseCode");

            Booking booking = bookingRepository.findByBookingCode(bookingCode)
                    .orElseThrow(() -> new ApiException("Không tìm thấy booking"));

            // Kiểm tra idempotency - nếu đã xử lý callback rồi thì không xử lý nữa
            if (booking.getStatus() != BookingStatus.PAYMENT_PENDING) {
                log.warn("Callback đã được xử lý trước đó cho booking: {}. Trạng thái hiện tại: {}", 
                         bookingCode, booking.getStatus());
                result.put("status", "already_processed");
                result.put("message", "Callback đã được xử lý trước đó");
                result.put("bookingCode", bookingCode);
                result.put("currentStatus", booking.getStatus().name());
                return result;
            }

            if ("00".equals(responseCode)) {
                booking.setStatus(BookingStatus.PAYMENT_COMPLETED);
                bookingRepository.save(booking);

                List<Request> requests = requestRepository.findByBookingId(booking.getId());
                for (Request req : requests) {
                    req.setStatus(RequestStatus.PAYMENT_COMPLETED);
                }
                requestRepository.saveAll(requests);

                customerTierService.updateUserStatistics(booking.getUser().getId(), booking.getAmount());

                log.info("Thanh toán thành công cho booking: {}", bookingCode);
                result.put("status", "success");
                result.put("message", "Thanh toán thành công");
                result.put("bookingCode", bookingCode);
            } else {
                booking.setStatus(BookingStatus.CANCELLED);
                bookingRepository.save(booking);
                unlockRooms(booking);

                log.info("Thanh toán thất bại cho booking: {}. Response code: {}", bookingCode, responseCode);
                result.put("status", "failed");
                result.put("message", "Thanh toán thất bại");
                result.put("bookingCode", bookingCode);
            }
        } else {
            log.error("SecureHash mismatch! Expected: {}, Got: {}", signValue, vnpSecureHash);
            result.put("status", "invalid");
            result.put("message", "Chữ ký không hợp lệ");
        }

        return result;
    }

    private void unlockRooms(Booking booking) {
        List<Request> requests = requestRepository.findByBookingId(booking.getId());
        for (Request req : requests) {
            req.setStatus(RequestStatus.CANCELLED);
            roomAvailabilityRequestRepository.deleteByRequestId(req.getId());

            List<RoomAvailability> availabilities = roomAvailabilityRepository.findByRoomAndDateRange(
                    req.getRoom().getId(),
                    req.getCheckIn().toLocalDate(),
                    req.getCheckOut().toLocalDate().minusDays(1)
            );
            for (RoomAvailability availability : availabilities) {
                availability.setIsAvailable(true);
            }
            roomAvailabilityRepository.saveAll(availabilities);
        }
        requestRepository.saveAll(requests);
        log.info("Unlocked rooms for cancelled booking: {}", booking.getBookingCode());
    }

    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) ipAddress = request.getRemoteAddr();
        return ipAddress;
    }
}
