package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request đổi mật khẩu")
public record ChangePasswordRequest(
        @Schema(description = "Mật khẩu hiện tại", example = "OldPassword123!")
        @NotBlank(message = "Mật khẩu hiện tại không được để trống")
        String currentPassword,

        @Schema(description = "Mật khẩu mới (ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt)", 
                example = "NewPassword123!")
        @NotBlank(message = "Mật khẩu mới không được để trống")
        @Size(min = 6, message = "Mật khẩu phải có ít nhất 8 ký tự")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Mật khẩu phải bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt"
        )
        String newPassword,

        @Schema(description = "Xác nhận mật khẩu mới", example = "NewPassword123!")
        @NotBlank(message = "Xác nhận mật khẩu không được để trống")
        String confirmPassword
) {}
