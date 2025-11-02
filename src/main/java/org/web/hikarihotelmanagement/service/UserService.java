package org.web.hikarihotelmanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.web.hikarihotelmanagement.dto.request.ChangePasswordRequest;
import org.web.hikarihotelmanagement.dto.request.UpdateProfileRequest;
import org.web.hikarihotelmanagement.dto.response.UserResponse;

public interface UserService {
    
    /**
     * Lấy danh sách tất cả người dùng với phân trang (bao gồm cả user đã xóa mềm)
     */
    Page<UserResponse> getAllUsers(Pageable pageable);
    
    /**
     * Lấy thông tin người dùng theo ID
     */
    UserResponse getUserById(Long id);
    
    /**
     * Xóa mềm người dùng (chuyển status thành false)
     */
    void softDeleteUser(Long id);
    
    /**
     * Kích hoạt lại người dùng đã bị xóa mềm (chuyển status thành true)
     */
    void restoreUser(Long id);
    
    /**
     * Xóa các user chưa xác thực sau 1 ngày
     */
    void deleteUnverifiedUsers();
    
    /**
     * Đổi mật khẩu cho user
     */
    void changePassword(String email, ChangePasswordRequest request);
    
    /**
     * Lấy thông tin cá nhân của user đang đăng nhập
     */
    UserResponse getCurrentUserProfile(String email);
    
    /**
     * Cập nhật thông tin cá nhân
     */
    UserResponse updateProfile(String email, UpdateProfileRequest request);
}
