package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Thông tin user cần tạo")
public record RegisterRequest(

        @Schema(description = "Địa chỉ email của user", example = "ledang123@gmail.com")
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        String email,

        @Schema(description = "Mật khẩu của user", example = "P@ssw0rd!")
        @NotBlank(message = "Password không được để trống")
        @Size(min = 6, message = "Password phải ít nhất 6 ký tự")
        String password,

        @Schema(description = "Tên hiển thị của user", example = "Lê Đảng")
        @NotBlank(message = "Tên người dùng không được để trống")
        @Size(max = 100, message = "Display name tối đa 100 ký tự")
        String fullName
) {}
