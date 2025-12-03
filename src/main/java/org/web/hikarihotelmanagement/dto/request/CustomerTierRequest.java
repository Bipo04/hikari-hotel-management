package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request tạo hoặc cập nhật hạng khách hàng")
public class CustomerTierRequest {
    
    @NotBlank(message = "Mã hạng không được để trống")
    @Size(max = 50, message = "Mã hạng không được vượt quá 50 ký tự")
    @Pattern(regexp = "^[A-Z_]+$", message = "Mã hạng chỉ được chứa chữ in hoa và dấu gạch dưới")
    @Schema(description = "Mã hạng (VD: BRONZE, SILVER, GOLD, DIAMOND, PLATINUM)", 
            example = "PLATINUM")
    private String code;
    
    @NotBlank(message = "Tên hạng không được để trống")
    @Size(max = 100, message = "Tên hạng không được vượt quá 100 ký tự")
    @Schema(description = "Tên hiển thị của hạng", example = "Bạch Kim")
    private String name;
    
    @NotNull(message = "Điều kiện chi tiêu tối thiểu không được để trống")
    @DecimalMin(value = "0", message = "Chi tiêu tối thiểu phải >= 0")
    @Schema(description = "Số tiền chi tiêu tối thiểu để đạt hạng này", 
            example = "200000000")
    private BigDecimal minSpending;
    
    @NotNull(message = "Số booking tối thiểu không được để trống")
    @Min(value = 0, message = "Số booking tối thiểu phải >= 0")
    @Schema(description = "Số lượng booking tối thiểu để đạt hạng này", 
            example = "80")
    private Integer minBookings;
    
    @NotNull(message = "Phần trăm giảm giá không được để trống")
    @Min(value = 0, message = "Phần trăm giảm giá phải >= 0")
    @Max(value = 100, message = "Phần trăm giảm giá phải <= 100")
    @Schema(description = "Phần trăm giảm giá cho hạng này", example = "20")
    private Integer discountPercent;
    
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    @Schema(description = "Mô tả chi tiết về hạng", 
            example = "Hạng cao cấp nhất dành cho khách hàng chi tiêu trên 200 triệu đồng")
    private String description;
    
    @Schema(description = "Trạng thái kích hoạt của hạng", example = "true")
    private Boolean active = true;
}
