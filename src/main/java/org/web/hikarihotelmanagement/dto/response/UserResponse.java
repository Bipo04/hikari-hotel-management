package org.web.hikarihotelmanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.web.hikarihotelmanagement.enums.Role;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "Thông tin chi tiết người dùng")
public record UserResponse(
        @Schema(description = "ID người dùng", example = "1")
        Long id,

        @Schema(description = "Tên người dùng", example = "Nguyễn Văn A")
        String name,

        @Schema(description = "Email", example = "user@example.com")
        String email,

        @Schema(description = "Số điện thoại", example = "0123456789")
        String phone,

        @Schema(description = "Ngày sinh")
        Date birthDate,

        @Schema(description = "Trạng thái tài khoản (true: active, false: inactive)", example = "true")
        Boolean status,

        @Schema(description = "Vai trò", example = "USER")
        Role role,

        @Schema(description = "Đã xác thực email chưa", example = "true")
        Boolean isVerified,

        @Schema(description = "Ngày tạo tài khoản")
        LocalDateTime createdAt,

        @Schema(description = "Ngày cập nhật")
        LocalDateTime updatedAt
) {}
