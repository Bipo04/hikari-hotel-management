package org.web.hikarihotelmanagement.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.request.CreateBookingRequest;
import org.web.hikarihotelmanagement.dto.request.RoomBookingRequest;
import org.web.hikarihotelmanagement.dto.response.BookingDetailResponse;
import org.web.hikarihotelmanagement.dto.response.BookingResponse;
import org.web.hikarihotelmanagement.dto.response.BookingRoomResponse;
import org.web.hikarihotelmanagement.entity.*;
import org.web.hikarihotelmanagement.enums.BookingStatus;
import org.web.hikarihotelmanagement.enums.RequestStatus;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.repository.BookingRepository;
import org.web.hikarihotelmanagement.repository.RequestRepository;
import org.web.hikarihotelmanagement.repository.RoomAvailabilityRepository;
import org.web.hikarihotelmanagement.repository.RoomAvailabilityRequestRepository;
import org.web.hikarihotelmanagement.repository.RoomRepository;
import org.web.hikarihotelmanagement.repository.UserRepository;
import org.web.hikarihotelmanagement.service.BookingService;
import org.web.hikarihotelmanagement.service.VNPayService;

import java.math.BigDecimal;
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
    private final org.web.hikarihotelmanagement.mapper.BookingMapper bookingMapper;

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
            newRequest.setNote(roomReq.getNote());

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

        booking.setAmount(totalAmount);
        booking = bookingRepository.save(booking);

        return buildBookingResponse(booking, requests, httpRequest);
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

    private BookingResponse buildBookingResponse(Booking booking, List<Request> requests, HttpServletRequest httpRequest) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getId());
        response.setBookingCode(booking.getBookingCode());
        response.setStatus(booking.getStatus());
        response.setPaymentMethod(booking.getPaymentMethod());
        response.setAmount(booking.getAmount());
        response.setCreatedAt(booking.getCreatedAt());

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
                    roomResp.setNote(req.getNote());
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
}