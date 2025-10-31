package org.web.hikarihotelmanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kết quả trả về sau khi đăng ký hoặc đăng nhập")
public record AuthenticationResponse(

        @Schema(description = "JWT token dùng để xác thực", example = "eyJhbGciOiJIUzI1...")
        String token,

        @Schema(description = "Email của user", example = "ledang123@gmail.com")
        String email,

        @Schema(description = "Tên đầy đủ", example = "Lê Đảng")
        String fullName
) {}
