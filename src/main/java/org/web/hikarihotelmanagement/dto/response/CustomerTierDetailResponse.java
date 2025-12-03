package org.web.hikarihotelmanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response chi tiết của một hạng khách hàng")
public class CustomerTierDetailResponse {
    
    @Schema(description = "ID của hạng", example = "1")
    private Long id;
    
    @Schema(description = "Mã hạng", example = "PLATINUM")
    private String code;
    
    @Schema(description = "Tên hạng", example = "Bạch Kim")
    private String name;
    
    @Schema(description = "Chi tiêu tối thiểu", example = "200000000")
    private BigDecimal minSpending;
    
    @Schema(description = "Số booking tối thiểu", example = "80")
    private Integer minBookings;
    
    @Schema(description = "Phần trăm giảm giá", example = "20")
    private Integer discountPercent;
    
    @Schema(description = "Thứ tự hạng", example = "5")
    private Integer tierOrder;
    
    @Schema(description = "Mô tả", example = "Hạng cao cấp nhất")
    private String description;
    
    @Schema(description = "Trạng thái kích hoạt", example = "true")
    private Boolean active;
    
    @Schema(description = "Số lượng user đang có hạng này", example = "15")
    private Long userCount;
    
    @Schema(description = "Thời gian tạo")
    private LocalDateTime createdAt;
    
    @Schema(description = "Thời gian cập nhật")
    private LocalDateTime updatedAt;
}
