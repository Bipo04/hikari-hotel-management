package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.ChangePasswordRequest;
import org.web.hikarihotelmanagement.dto.request.CreateReviewRequest;
import org.web.hikarihotelmanagement.dto.request.UpdateProfileRequest;
import org.web.hikarihotelmanagement.dto.response.BookingDetailResponse;
import org.web.hikarihotelmanagement.dto.response.ReviewResponse;
import org.web.hikarihotelmanagement.dto.response.UserResponse;
import org.web.hikarihotelmanagement.entity.User;
import org.web.hikarihotelmanagement.repository.UserRepository;
import org.web.hikarihotelmanagement.service.BookingService;
import org.web.hikarihotelmanagement.service.CustomerTierService;
import org.web.hikarihotelmanagement.service.ReviewService;
import org.web.hikarihotelmanagement.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "API quản lý thông tin cá nhân")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final CustomerTierService customerTierService;
    private final UserRepository userRepository;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    @GetMapping("/profile")
    @Operation(summary = "Lấy thông tin cá nhân")
    public ResponseEntity<UserResponse> getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName();
        UserResponse userProfile = userService.getCurrentUserProfile(email);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/profile")
    @Operation(summary = "Cập nhật thông tin cá nhân")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UserResponse updatedProfile = userService.updateProfile(email, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        userService.changePassword(email, request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Đổi mật khẩu thành công");

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/bookings")
    @Operation(summary = "Lấy danh sách đơn đặt phòng của người dùng", 
               description = "Trả về danh sách tất cả đơn đặt phòng của user đang đăng nhập")
    public ResponseEntity<List<BookingDetailResponse>> getUserBookings(Authentication authentication) {
        String email = authentication.getName();
        List<BookingDetailResponse> bookings = bookingService.getUserBookings(email);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/bookings/{bookingId}")
    @Operation(summary = "Lấy chi tiết một đơn đặt phòng", 
               description = "Trả về chi tiết đơn đặt phòng theo ID")
    public ResponseEntity<BookingDetailResponse> getBookingDetail(
            @PathVariable Long bookingId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        BookingDetailResponse booking = bookingService.getBookingDetail(bookingId, email);
        return ResponseEntity.ok(booking);
    }
    
    @PostMapping("/reviews")
    @Operation(summary = "Đánh giá đơn đặt phòng", 
               description = "Tạo đánh giá cho booking sau khi tất cả các phòng đã checkout")
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        ReviewResponse review = reviewService.createReview(request, email);
        return ResponseEntity.ok(review);
    }
}
