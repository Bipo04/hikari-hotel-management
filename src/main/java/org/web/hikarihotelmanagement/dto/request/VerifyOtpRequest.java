package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Thông tin xác thực OTP")
public record VerifyOtpRequest(

        @Schema(description = "Email của user", example = "ledang123@gmail.com")
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        String email,

        @Schema(description = "Mã OTP 6 chữ số", example = "123456")
        @NotBlank(message = "OTP không được để trống")
        @Pattern(regexp = "^[0-9]{6}$", message = "OTP phải là 6 chữ số")
        String otp
) {}
