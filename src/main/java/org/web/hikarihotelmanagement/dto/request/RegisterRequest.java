package org.web.hikarihotelmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Date;

@Schema(description = "Thông tin đăng ký tài khoản")
public record RegisterRequest(

        @Schema(description = "Địa chỉ email của user", example = "ledang123@gmail.com")
        @NotBlank(message = "Email không được để trống")
        @Email(message = "Email không hợp lệ")
        String email,

        @Schema(description = "Mật khẩu của user", example = "P@ssw0rd!")
        @NotBlank(message = "Password không được để trống")
        @Size(min = 6, message = "Password phải ít nhất 6 ký tự")
        String password,

        @Schema(description = "Họ và tên đầy đủ", example = "Nguyễn Văn A")
        @NotBlank(message = "Họ tên không được để trống")
        String name,

        @Schema(description = "Số điện thoại", example = "0123456789")
        @NotBlank(message = "Số điện thoại không được để trống")
        @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải có 10 chữ số")
        String phone,

        @Schema(description = "Ngày sinh", example = "2000-01-01")
        @NotNull(message = "Ngày sinh không được để trống")
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date birthDate
) {}
