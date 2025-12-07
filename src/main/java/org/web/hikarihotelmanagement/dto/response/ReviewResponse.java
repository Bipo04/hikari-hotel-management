package org.web.hikarihotelmanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Thông tin đánh giá")
public record ReviewResponse(
        @Schema(description = "ID đánh giá", example = "1")
        Long id,

        @Schema(description = "ID booking", example = "1")
        Long bookingId,

        @Schema(description = "Mã booking", example = "BK1001")
        String bookingCode,

        @Schema(description = "Tên người đánh giá", example = "Nguyễn Văn A")
        String userName,

        @Schema(description = "Số sao (1-5)", example = "5")
        Integer rating,

        @Schema(description = "Nội dung đánh giá", example = "Phòng đẹp, dịch vụ tốt")
        String comment,

        @Schema(description = "Ngày đánh giá")
        LocalDateTime createdAt
) {}
