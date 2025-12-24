package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Thông tin đăng nhập của user")
public record LoginRequest(

        @Schema(description = "Email của user", example = "admin@hikari.com")
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        String email,

        @Schema(description = "Mật khẩu của user", example = "ledang")
        @Size(min = 6, message = "Mật khẩu phải có ít nhất 8 ký tự")
        String password
) {}
