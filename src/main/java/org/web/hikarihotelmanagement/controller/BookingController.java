package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.CreateBookingRequest;
import org.web.hikarihotelmanagement.dto.response.BookingResponse;
import org.web.hikarihotelmanagement.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking", description = "API quản lý đặt phòng")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping
    @Operation(summary = "Tạo đơn đặt phòng mới",
               description = "Tạo đơn đặt phòng với nhiều phòng. Nếu paymentMethod là VNPAY, sẽ trả về paymentUrl để redirect user thanh toán")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest
    ) {
        String userEmail = authentication.getName();
        BookingResponse bookingResponse = bookingService.createBooking(request, userEmail, httpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingResponse);
    }
}
