package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.request.ChangePasswordRequest;
import org.web.hikarihotelmanagement.dto.request.UpdateProfileRequest;
import org.web.hikarihotelmanagement.dto.response.UserResponse;
import org.web.hikarihotelmanagement.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "API quản lý thông tin cá nhân")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Lấy thông tin cá nhân")
    public ResponseEntity<UserResponse> getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName();
        UserResponse userProfile = userService.getCurrentUserProfile(email);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/profile")
    @Operation(summary = "Cập nhật thông tin cá nhân")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        UserResponse updatedProfile = userService.updateProfile(email, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu")
    public ResponseEntity<Map<String, String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        userService.changePassword(email, request);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đổi mật khẩu thành công");
        
        return ResponseEntity.ok(response);
    }
    
}
