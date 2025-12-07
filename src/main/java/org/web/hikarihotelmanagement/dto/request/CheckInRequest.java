package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.web.hikarihotelmanagement.enums.IdentityType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request để check-in phòng với danh sách khách")
public class CheckInRequest {
    
    @NotNull(message = "Request ID không được để trống")
    @Schema(description = "ID của request (phòng) cần check-in", example = "1")
    private Long requestId;
    
    @NotEmpty(message = "Danh sách khách không được để trống")
    @Valid
    @Schema(description = "Danh sách thông tin khách")
    private List<GuestInfo> guests;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Thông tin khách")
    public static class GuestInfo {
        
        @NotNull(message = "Họ tên không được để trống")
        @Schema(description = "Họ tên khách", example = "Nguyễn Văn A")
        private String fullName;
        
        @NotNull(message = "Loại giấy tờ không được để trống")
        @Schema(description = "Loại giấy tờ", example = "CITIZEN_ID")
        private IdentityType identityType;
        
        @NotNull(message = "Số giấy tờ không được để trống")
        @Schema(description = "Số giấy tờ", example = "001234567890")
        private String identityNumber;
        
        @NotNull(message = "Ngày cấp không được để trống")
        @Schema(description = "Ngày cấp giấy tờ")
        private LocalDate identityIssuedDate;
        
        @NotNull(message = "Nơi cấp không được để trống")
        @Schema(description = "Nơi cấp giấy tờ", example = "Công an TP. Hồ Chí Minh")
        private String identityIssuedPlace;
    }
}
