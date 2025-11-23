package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.dto.response.PageResponse;
import org.web.hikarihotelmanagement.dto.response.UserResponse;
import org.web.hikarihotelmanagement.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API quản lý người dùng dành cho Admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách người dùng",
            description = "Lấy danh sách tất cả người dùng với phân trang. Bao gồm cả user đã bị xóa mềm (status = false)."
    )
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") 
                ? Sort.Direction.ASC 
                : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserResponse> userPage = userService.getAllUsers(pageable);

        PageResponse<UserResponse> response = new PageResponse<>(
                userPage.getContent(),
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isFirst(),
                userPage.isLast()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy thông tin người dùng theo ID",
            description = "Lấy thông tin chi tiết của một người dùng dựa trên ID"
    )
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id
    ) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa mềm người dùng",
            description = "Xóa mềm người dùng bằng cách chuyển status thành false. User không bị xóa khỏi database."
    )
    public ResponseEntity<Map<String, String>> softDeleteUser(
            @PathVariable Long id
    ) {
        userService.softDeleteUser(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Xóa người dùng thành công");
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/restore")
    @Operation(
            summary = "Kích hoạt lại người dùng",
            description = "Kích hoạt lại người dùng đã bị xóa mềm bằng cách chuyển status thành true."
    )
    public ResponseEntity<Map<String, String>> restoreUser(
            @PathVariable Long id
    ) {
        userService.restoreUser(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Kích hoạt lại người dùng thành công");
        
        return ResponseEntity.ok(response);
    }
}
