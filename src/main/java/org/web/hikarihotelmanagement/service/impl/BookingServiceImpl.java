package org.web.hikarihotelmanagement.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.request.CheckInRequest;
import org.web.hikarihotelmanagement.dto.request.CreateBookingRequest;
import org.web.hikarihotelmanagement.dto.request.RoomBookingRequest;
import org.web.hikarihotelmanagement.dto.response.BookingDetailResponse;
import org.web.hikarihotelmanagement.dto.response.BookingResponse;
import org.web.hikarihotelmanagement.dto.response.BookingRoomResponse;
import org.web.hikarihotelmanagement.dto.response.CustomerTierDetailResponse;
import org.web.hikarihotelmanagement.entity.*;
import org.web.hikarihotelmanagement.enums.BookingStatus;
import org.web.hikarihotelmanagement.enums.RequestStatus;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.mapper.BookingMapper;
import org.web.hikarihotelmanagement.repository.*;
import org.web.hikarihotelmanagement.service.BookingService;
import org.web.hikarihotelmanagement.service.VNPayService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RoomAvailabilityRepository roomAvailabilityRepository;
    private final RoomAvailabilityRequestRepository roomAvailabilityRequestRepository;
    private final VNPayService vnPayService;
    private final BookingMapper bookingMapper;
    private final CustomerTierServiceImpl customerTierService;
    private final GuestRepository guestRepository;

    private void validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDate today = LocalDate.now();

        if (checkInDate.isBefore(today)) {
            throw new ApiException("Ngày check-in không thể là ngày trong quá khứ");
        }

        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            throw new ApiException("Ngày check-out phải sau ngày check-in");
        }
    }

    @Override
    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request, String userEmail, HttpServletRequest httpRequest) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng"));

        if (request.getRooms() == null || request.getRooms().isEmpty()) {
            throw new ApiException("Danh sách phòng không được để trống");
        }

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setBookingCode(generateBookingCode());
        booking.setStatus(BookingStatus.PAYMENT_PENDING);
        booking.setPaymentMethod(request.getPaymentMethod());
        booking.setAmount(BigDecimal.ZERO);
        booking.setNote(request.getBookingNote());

        booking = bookingRepository.save(booking);

        List<Request> requests = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (RoomBookingRequest roomReq : request.getRooms()) {
            validateDates(roomReq.getCheckInDate(), roomReq.getCheckOutDate());

            Room room = roomRepository.findById(roomReq.getRoomId())
                    .orElseThrow(() -> new ApiException("Không tìm thấy phòng với ID: " + roomReq.getRoomId()));

            if (!isRoomAvailable(room, roomReq.getCheckInDate(), roomReq.getCheckOutDate())) {
                throw new ApiException("Phòng " + room.getRoomNumber() + " không còn trống trong thời gian này");
            }

            if (roomReq.getNumberOfGuests() > room.getRoomType().getCapacity()) {
                throw new ApiException("Số lượng khách vượt quá sức chứa của phòng " + room.getRoomNumber());
            }

            Request newRequest = new Request();
            newRequest.setBooking(booking);
            newRequest.setRoom(room);
            newRequest.setCheckIn(roomReq.getCheckInDate().atTime(14, 0));
            newRequest.setCheckOut(roomReq.getCheckOutDate().atTime(12, 0));
            newRequest.setNumberOfGuests(roomReq.getNumberOfGuests());
            newRequest.setStatus(RequestStatus.PAYMENT_PENDING);

            requests.add(newRequest);

            BigDecimal roomTotal = calculateRoomPrice(room.getId(), roomReq.getCheckInDate(), roomReq.getCheckOutDate());
            totalAmount = totalAmount.add(roomTotal);
        }

        requests = requestRepository.saveAll(requests);

        for (int i = 0; i < requests.size(); i++) {
            Request savedRequest = requests.get(i);
            RoomBookingRequest roomReq = request.getRooms().get(i);
            updateRoomAvailability(savedRequest, roomReq.getCheckInDate(), roomReq.getCheckOutDate());
        }

        CustomerTierDetailResponse customerTier = customerTierService.getCurrentUserTier(userEmail);
        int discount = customerTier.getDiscountPercent();
        
        BigDecimal discountAmount = totalAmount
                .multiply(BigDecimal.valueOf(100 - discount))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        booking.setAmount(discountAmount);
        booking = bookingRepository.save(booking);

        return buildBookingResponse(booking, requests, discount, httpRequest);
    }

    private boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDate lastNightDate = checkOutDate.minusDays(1);
        long numberOfDays = ChronoUnit.DAYS.between(checkInDate, lastNightDate) + 1;

        Boolean isFullyAvailable = roomAvailabilityRepository.isRoomFullyAvailable(
                room.getId(),
                checkInDate,
                lastNightDate,
                numberOfDays
        );

        return Boolean.TRUE.equals(isFullyAvailable);
    }

    private void updateRoomAvailability(Request request, LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDate lastNightDate = checkOutDate.minusDays(1);
        List<RoomAvailability> availabilities = roomAvailabilityRepository.findByRoomAndDateRange(
                request.getRoom().getId(),
                checkInDate,
                lastNightDate
        );

        if (availabilities.isEmpty()) {
            throw new ApiException("Không tìm thấy thông tin availability cho phòng trong khoảng thời gian này");
        }

        List<RoomAvailabilityRequest> roomAvailabilityRequests = new ArrayList<>();

        for (RoomAvailability availability : availabilities) {
            availability.setIsAvailable(false);

            RoomAvailabilityRequest roomAvailabilityRequest = new RoomAvailabilityRequest();
            roomAvailabilityRequest.setRoomAvailability(availability);
            roomAvailabilityRequest.setRequest(request);
            roomAvailabilityRequests.add(roomAvailabilityRequest);
        }

        roomAvailabilityRepository.saveAll(availabilities);
        roomAvailabilityRequestRepository.saveAll(roomAvailabilityRequests);
    }

    private BigDecimal calculateRoomPrice(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        List<RoomAvailability> availabilities = roomAvailabilityRepository.findByRoomAndDateRange(
                roomId,
                checkInDate,
                checkOutDate.minusDays(1)
        );

        if (availabilities.isEmpty()) {
            throw new ApiException("Không tìm thấy thông tin giá phòng cho khoảng thời gian này");
        }

        return availabilities.stream()
                .map(RoomAvailability::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateBookingCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }

    private BookingResponse buildBookingResponse(Booking booking, List<Request> requests,int discount, HttpServletRequest httpRequest) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setBookingCode(booking.getBookingCode());
        response.setStatus(booking.getStatus());
        response.setPaymentMethod(booking.getPaymentMethod());
        response.setAmount(booking.getAmount());
        response.setCreatedAt(booking.getCreatedAt());
        response.setBookingNote(booking.getNote());
        response.setDiscount(discount);

        if ("VNPAY".equalsIgnoreCase(booking.getPaymentMethod())) {
            String paymentUrl = vnPayService.createPaymentUrl(booking.getId(), httpRequest);
            response.setPaymentUrl(paymentUrl);
        }

        List<BookingRoomResponse> roomResponses = requests.stream()
                .map(req -> {
                    BookingRoomResponse roomResp = new BookingRoomResponse();
                    roomResp.setRequestId(req.getId());
                    roomResp.setRoomId(req.getRoom().getId());
                    roomResp.setRoomNumber(req.getRoom().getRoomNumber());
                    roomResp.setCheckInDate(req.getCheckIn().toLocalDate());
                    roomResp.setCheckOutDate(req.getCheckOut().toLocalDate());
                    roomResp.setNumberOfGuests(req.getNumberOfGuests());
                    return roomResp;
                })
                .collect(Collectors.toList());

        response.setRooms(roomResponses);

        return response;
    }

    @Override
    public List<BookingDetailResponse> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng"));

        List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        return bookings.stream()
                .map(bookingMapper::toBookingDetailResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDetailResponse getBookingDetail(Long bookingId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException("Không tìm thấy đơn đặt phòng"));

        // Kiểm tra booking có thuộc về user này không
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new ApiException("Bạn không có quyền xem đơn đặt này");
        }

        return bookingMapper.toBookingDetailResponse(booking);
    }

    // Admin methods
    @Override
    public List<BookingDetailResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        
        return bookings.stream()
                .map(bookingMapper::toBookingDetailResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingDetailResponse> getBookingsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("Không tìm thấy người dùng"));
        
        List<Booking> bookings = bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        return bookings.stream()
                .map(bookingMapper::toBookingDetailResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public BookingDetailResponse getBookingDetailAdmin(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException("Không tìm thấy đơn đặt phòng"));
        
        return bookingMapper.toBookingDetailResponse(booking);
    }
    
    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ApiException("Không tìm thấy đơn đặt phòng"));
        
        // Kiểm tra trạng thái booking
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ApiException("Đơn đặt phòng đã được hủy trước đó");
        }
        
        // Kiểm tra xem có request nào đã check-in hoặc check-out chưa
        boolean hasCheckedInOrOut = booking.getRequests().stream()
                .anyMatch(req -> req.getStatus() == RequestStatus.CHECKED_IN || 
                                 req.getStatus() == RequestStatus.CHECKED_OUT);
        
        if (hasCheckedInOrOut) {
            throw new ApiException("Không thể hủy đơn đặt phòng khi đã có phòng check-in hoặc check-out");
        }

        // Nếu đã thanh toán thành công (PAYMENT_COMPLETED), trừ lại tiền và số đơn của user
        if (booking.getStatus() == BookingStatus.PAYMENT_COMPLETED) {
            User user = booking.getUser();

            // Trừ tổng chi tiêu
            BigDecimal currentSpent = user.getTotalSpent();
            if (currentSpent != null && currentSpent.compareTo(booking.getAmount()) >= 0) {
                user.setTotalSpent(currentSpent.subtract(booking.getAmount()));
            } else {
                user.setTotalSpent(BigDecimal.ZERO);
            }

            // Trừ số booking
            Integer currentBookings = user.getTotalBookings();
            if (currentBookings != null && currentBookings > 0) {
                user.setTotalBookings(currentBookings - 1);
            } else {
                user.setTotalBookings(0);
            }

            userRepository.save(user);

            // Cập nhật lại tier của user
            customerTierService.updateCustomerTier(user);

            log.info("Đã hoàn trả thống kê cho user {}: Trừ {} VND và 1 booking",
                    user.getEmail(), booking.getAmount());
        }
        
        // Cập nhật trạng thái booking
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setPaymentUrl(null);
        bookingRepository.save(booking);
        
        // Cập nhật trạng thái các request
        for (Request request : booking.getRequests()) {
            request.setStatus(RequestStatus.CANCELLED);
;
            requestRepository.save(request);
            
            // Mở lại phòng (set isAvailable = true cho các RoomAvailability)
            List<RoomAvailabilityRequest> roomAvailabilityRequests = 
                    roomAvailabilityRequestRepository.findByRequestId(request.getId());
            
            for (RoomAvailabilityRequest rar : roomAvailabilityRequests) {
                RoomAvailability availability = rar.getRoomAvailability();
                availability.setIsAvailable(true);
                roomAvailabilityRepository.save(availability);
            }
            
            // Xóa các RoomAvailabilityRequest
            roomAvailabilityRequestRepository.deleteAll(roomAvailabilityRequests);
        }
        
        log.info("Đã hủy booking {} với status {}", booking.getBookingCode(), booking.getStatus());
    }
    
    @Override
    @Transactional
    public void checkInRequest(CheckInRequest request) {
        Request requestEntity = requestRepository.findById(request.getRequestId())
                .orElseThrow(() -> new ApiException("Không tìm thấy request"));
        
        // Kiểm tra trạng thái request
        if (requestEntity.getStatus() != RequestStatus.PAYMENT_COMPLETED) {
            throw new ApiException("Chỉ có thể check-in khi request đã thanh toán");
        }
        
        // Validate thông tin giấy tờ của khách
        for (CheckInRequest.GuestInfo guestInfo : request.getGuests()) {
            validateIdentityNumber(guestInfo);
        }
        
        // Cập nhật trạng thái
        requestEntity.setStatus(RequestStatus.CHECKED_IN);
        requestRepository.save(requestEntity);
        
        // Tạo danh sách guests
        for (CheckInRequest.GuestInfo guestInfo : request.getGuests()) {
            Guest guest = new Guest();
            guest.setRequest(requestEntity);
            guest.setFullName(guestInfo.getFullName());
            guest.setIdentityType(guestInfo.getIdentityType());
            guest.setIdentityNumber(guestInfo.getIdentityNumber());
            guest.setIdentityIssuedDate(guestInfo.getIdentityIssuedDate());
            guest.setIdentityIssuedPlace(guestInfo.getIdentityIssuedPlace());
            
            guestRepository.save(guest);
        }
        
        log.info("Đã check-in request {} với {} khách", requestEntity.getId(), request.getGuests().size());
    }
    
    private void validateIdentityNumber(CheckInRequest.GuestInfo guestInfo) {
        String identityNumber = guestInfo.getIdentityNumber();
        if (identityNumber == null) return;
        
        identityNumber = identityNumber.trim();
        
        switch (guestInfo.getIdentityType()) {
            case CCCD -> {
                if (!identityNumber.matches("^\\d{12}$")) {
                    throw new ApiException("CCCD phải có đúng 12 chữ số");
                }
            }
            case CMND -> {
                if (!identityNumber.matches("^\\d{9}$|^\\d{12}$")) {
                    throw new ApiException("CMND phải có 9 hoặc 12 chữ số");
                }
            }
            case PASSPORT -> {
                if (!identityNumber.matches("^[A-Z]\\d{7,8}$")) {
                    throw new ApiException("Hộ chiếu phải có 1 chữ cái viết hoa và 7-8 chữ số (VD: B12345678)");
                }
            }
            case DRIVER_LICENSE -> {
                if (!identityNumber.matches("^\\d{12}$")) {
                    throw new ApiException("Bằng lái xe phải có đúng 12 chữ số");
                }
            }
        }
    }
    
    @Override
    @Transactional
    public void checkOutRequest(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ApiException("Không tìm thấy request"));
        
        // Kiểm tra trạng thái request
        if (request.getStatus() != RequestStatus.CHECKED_IN) {
            throw new ApiException("Chỉ có thể check-out khi request đã check-in");
        }
        
        // Cập nhật trạng thái
        request.setStatus(RequestStatus.CHECKED_OUT);
        requestRepository.save(request);
        
        log.info("Đã check-out request {}", requestId);
    }
}
