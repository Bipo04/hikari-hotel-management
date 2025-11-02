package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web.hikarihotelmanagement.dto.request.ChangePasswordRequest;
import org.web.hikarihotelmanagement.dto.request.UpdateProfileRequest;
import org.web.hikarihotelmanagement.dto.response.UserResponse;
import org.web.hikarihotelmanagement.entity.User;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.mapper.UserMapper;
import org.web.hikarihotelmanagement.repository.UserRepository;
import org.web.hikarihotelmanagement.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Lấy danh sách tất cả người dùng (bao gồm đã xóa mềm) với phân trang: trang={}, kích thước={}", 
                 pageable.getPageNumber(), pageable.getPageSize());

        Page<User> users = userRepository.findAll(pageable);
        
        return users.map(userMapper::toUserResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        log.info("Lấy thông tin người dùng theo ID: {}", id);

        User user = userRepository.findById(id).orElse(null);
        
        if (user == null) {
            log.warn("Không tìm thấy người dùng với ID: {}", id);
            throw new ApiException("Không tìm thấy người dùng với ID: " + id);
        }
        
        log.debug("Tìm thấy người dùng: email={}", user.getEmail());
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public void softDeleteUser(Long id) {
        log.info("Xóa mềm người dùng với ID: {}", id);
        
        User user = userRepository.findById(id).orElse(null);
        
        if (user == null) {
            log.warn("Xóa mềm thất bại - không tìm thấy người dùng với ID: {}", id);
            throw new ApiException("Không tìm thấy người dùng với ID: " + id);
        }
        
        if (!user.getStatus()) {
            log.warn("Xóa mềm thất bại - người dùng đã bị vô hiệu hóa trước đó, ID: {}", id);
            throw new ApiException("Người dùng đã bị vô hiệu hóa trước đó");
        }

        user.setStatus(false);
        userRepository.save(user);
        
        log.info("Xóa mềm người dùng thành công với ID: {}", id);
    }

    @Override
    @Transactional
    public void restoreUser(Long id) {
        log.info("Kích hoạt lại người dùng với ID: {}", id);
        
        User user = userRepository.findById(id).orElse(null);
        
        if (user == null) {
            log.warn("Kích hoạt lại thất bại - không tìm thấy người dùng với ID: {}", id);
            throw new ApiException("Không tìm thấy người dùng với ID: " + id);
        }
        
        if (user.getStatus()) {
            log.warn("Kích hoạt lại thất bại - người dùng đang ở trạng thái hoạt động, ID: {}", id);
            throw new ApiException("Người dùng đang ở trạng thái hoạt động");
        }

        user.setStatus(true);
        userRepository.save(user);
        
        log.info("Kích hoạt lại người dùng thành công với ID: {}", id);
    }

    @Override
    @Transactional
    public void deleteUnverifiedUsers() {
        log.info("Bắt đầu tác vụ định kỳ xóa người dùng chưa xác thực");

        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(1);

        List<User> unverifiedUsers = userRepository.findUnverifiedUsersCreatedBefore(cutoffTime);
        
        if (!unverifiedUsers.isEmpty()) {
            log.info("Tìm thấy {} người dùng chưa xác thực cần xóa", unverifiedUsers.size());

            userRepository.deleteAll(unverifiedUsers);
            
            log.info("Đã xóa thành công {} người dùng chưa xác thực", unverifiedUsers.size());
        } else {
            log.info("Không tìm thấy người dùng chưa xác thực nào để xóa");
        }
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        log.info("Đổi mật khẩu cho người dùng: {}", email);

        if (!request.newPassword().equals(request.confirmPassword())) {
            log.warn("Đổi mật khẩu thất bại - mật khẩu mới và xác nhận không khớp cho: {}", email);
            throw new ApiException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Đổi mật khẩu thất bại - không tìm thấy người dùng: {}", email);
                    return new ApiException("Không tìm thấy người dùng");
                });

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            log.warn("Đổi mật khẩu thất bại - mật khẩu hiện tại không đúng cho: {}", email);
            throw new ApiException("Mật khẩu hiện tại không đúng");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            log.warn("Đổi mật khẩu thất bại - mật khẩu mới trùng với mật khẩu cũ cho: {}", email);
            throw new ApiException("Mật khẩu mới không được trùng với mật khẩu hiện tại");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        log.info("Đổi mật khẩu thành công cho người dùng: {}", email);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserProfile(String email) {
        log.info("Lấy thông tin cá nhân cho người dùng: {}", email);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.warn("Không tìm thấy người dùng với email: {}", email);
            throw new ApiException("Không tìm thấy người dùng");
        }

        log.debug("Lấy thông tin cá nhân thành công cho: {}", email);
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {
        log.info("Cập nhật thông tin cá nhân cho người dùng: {}", email);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            log.warn("Cập nhật thất bại - không tìm thấy người dùng: {}", email);
            throw new ApiException("Không tìm thấy người dùng");
        }

        userMapper.updateUserFromDto(request, user);
        userRepository.save(user);
        
        log.info("Cập nhật thông tin cá nhân thành công cho người dùng: {}", email);

        return userMapper.toUserResponse(user);
    }

}
