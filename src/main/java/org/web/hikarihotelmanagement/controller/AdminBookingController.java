package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.CheckInRequest;
import org.web.hikarihotelmanagement.dto.response.BookingDetailResponse;
import org.web.hikarihotelmanagement.service.BookingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
@Tag(name = "Admin - Booking Management", description = "API quản lý đơn đặt phòng (Admin)")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả đơn đặt phòng",
               description = "Trả về danh sách tất cả đơn đặt phòng trong hệ thống")
    public ResponseEntity<List<BookingDetailResponse>> getAllBookings() {
        List<BookingDetailResponse> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lấy danh sách đơn đặt phòng của một user",
               description = "Trả về danh sách tất cả đơn đặt phòng của user theo userId")
    public ResponseEntity<List<BookingDetailResponse>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingDetailResponse> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Lấy chi tiết một đơn đặt phòng",
               description = "Trả về chi tiết đơn đặt phòng theo ID (không cần kiểm tra quyền sở hữu)")
    public ResponseEntity<BookingDetailResponse> getBookingDetail(@PathVariable Long bookingId) {
        BookingDetailResponse booking = bookingService.getBookingDetailAdmin(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/{bookingId}/cancel")
    @Operation(summary = "Hủy đơn đặt phòng",
               description = "Hủy đơn đặt phòng và hoàn trả dữ liệu:\n" +
                           "- Mở lại phòng (set isAvailable = true)\n" +
                           "- Nếu đã thanh toán: trừ lại tiền và số đơn của user, cập nhật lại tier\n" +
                           "- Nếu đang pending: chỉ hủy đơn")
    public ResponseEntity<Map<String, String>> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã hủy đơn đặt phòng thành công");

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/requests/check-in")
    @Operation(summary = "Check-in phòng",
               description = "Check-in request (phòng) với danh sách khách:\n" +
                           "- Chuyển trạng thái request sang CHECKED_IN\n" +
                           "- Lưu thông tin tất cả khách vào bảng guests")
    public ResponseEntity<Map<String, String>> checkInRequest(@Valid @RequestBody CheckInRequest request) {
        bookingService.checkInRequest(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã check-in thành công");

        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/requests/{requestId}/check-out")
    @Operation(summary = "Check-out phòng",
               description = "Check-out request (phòng):\n" +
                           "- Chuyển trạng thái request sang CHECKED_OUT")
    public ResponseEntity<Map<String, String>> checkOutRequest(@PathVariable Long requestId) {
        bookingService.checkOutRequest(requestId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Đã check-out thành công");

        return ResponseEntity.ok(response);
    }
}
