package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.response.ReviewResponse;
import org.web.hikarihotelmanagement.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Management", description = "API quản lý đánh giá")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/my-reviews")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Lấy tất cả review của người dùng hiện tại",
               description = "Trả về danh sách tất cả review mà user đã đánh giá, sắp xếp theo thời gian mới nhất")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<ReviewResponse> reviews = reviewService.getReviewsByUser(userDetails.getUsername());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/room-type/{roomTypeId}")
    @Operation(summary = "Lấy tất cả review của một loại phòng",
               description = "Trả về danh sách tất cả review cho loại phòng được chỉ định, sắp xếp theo thời gian mới nhất. API này public, không cần đăng nhập.")
    public ResponseEntity<List<ReviewResponse>> getReviewsByRoomType(
            @PathVariable Long roomTypeId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByRoomType(roomTypeId);
        return ResponseEntity.ok(reviews);
    }
}
