package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request để đánh giá booking")
public class CreateReviewRequest {
    
    @NotNull(message = "Booking ID không được để trống")
    @Schema(description = "ID của booking cần đánh giá", example = "1")
    private Long bookingId;
    
    @NotNull(message = "Đánh giá không được để trống")
    @Min(value = 1, message = "Đánh giá phải từ 1 đến 5 sao")
    @Max(value = 5, message = "Đánh giá phải từ 1 đến 5 sao")
    @Schema(description = "Số sao đánh giá (1-5)", example = "5")
    private Integer rating;
    
    @Schema(description = "Nội dung đánh giá", example = "Phòng đẹp, dịch vụ tốt")
    private String comment;
}
