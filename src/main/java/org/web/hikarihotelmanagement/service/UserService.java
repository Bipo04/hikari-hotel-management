package org.web.hikarihotelmanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.web.hikarihotelmanagement.dto.request.ChangePasswordRequest;
import org.web.hikarihotelmanagement.dto.request.UpdateProfileRequest;
import org.web.hikarihotelmanagement.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);
    List<UserResponse> getAllUsersWithoutPaging();
    UserResponse getUserById(Long id);
    void softDeleteUser(Long id);
    void restoreUser(Long id);
    void deleteUnverifiedUsers();
    void changePassword(String email, ChangePasswordRequest request);
    UserResponse getCurrentUserProfile(String email);
    UserResponse updateProfile(String email, UpdateProfileRequest request);
}
