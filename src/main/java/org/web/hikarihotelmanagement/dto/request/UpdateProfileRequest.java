package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Schema(description = "Request cập nhật thông tin cá nhân")
public record UpdateProfileRequest(
        @Schema(description = "Tên người dùng", example = "Nguyễn Văn A")
        @Size(min = 2, max = 100, message = "Tên phải có từ 2 đến 100 ký tự")
        String name,

        @Schema(description = "Số điện thoại", example = "0123456789")
        @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải có 10 chữ số")
        String phone,

        @Schema(description = "Ngày sinh")
        Date birthDate
) {}
