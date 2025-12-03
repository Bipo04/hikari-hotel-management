package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request để sắp xếp lại thứ tự các tier")
public class ReorderTierRequest {
    
    @NotEmpty(message = "Danh sách tier không được để trống")
    @Valid
    @Schema(description = "Danh sách các tier với thứ tự mới")
    private List<TierOrderItem> tiers;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TierOrderItem {
        
        @NotNull(message = "Tier ID không được để trống")
        @Schema(description = "ID của tier", example = "1")
        private Long id;
        
        @NotNull(message = "Thứ tự không được để trống")
        @Positive(message = "Thứ tự phải là số dương")
        @Schema(description = "Thứ tự mới (1, 2, 3...)", example = "2")
        private Integer order;
    }
}
