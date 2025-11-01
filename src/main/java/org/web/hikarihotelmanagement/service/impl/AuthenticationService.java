package org.web.hikarihotelmanagement.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.web.hikarihotelmanagement.dto.request.LoginRequest;
import org.web.hikarihotelmanagement.dto.request.RegisterRequest;
import org.web.hikarihotelmanagement.dto.request.ResendOtpRequest;
import org.web.hikarihotelmanagement.dto.request.VerifyOtpRequest;
import org.web.hikarihotelmanagement.dto.response.AuthenticationResponse;
import org.web.hikarihotelmanagement.entity.User;
import org.web.hikarihotelmanagement.enums.Role;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.repository.UserRepository;
import org.web.hikarihotelmanagement.service.EmailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ApiException("Email đã tồn tại: " + request.email());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setPhone(request.phone());
        user.setBirthDate(request.birthDate());
        user.setRole(Role.USER);
        user.setStatus(false);
        user.setIsVerified(false);

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(new Date(System.currentTimeMillis() + 5 * 60 * 1000));

        userRepository.save(user);

        sendOtpEmail(user.getEmail(), otp);

        return "Đăng ký thành công! Vui lòng kiểm tra email để xác thực OTP.";
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException("Không tìm thấy user với email: " + request.email()));

        if (user.getIsVerified() == null || !user.getIsVerified()) {
            throw new ApiException("Tài khoản chưa được xác thực. Vui lòng xác thực OTP từ email.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        request.password()
                )
        );

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, user.getEmail(), user.getName());
    }

    private void sendOtpEmail(String email, String otp) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("otp", otp);
            
            emailService.sendEmailWithTemplate(
                email,
                "Xác thực tài khoản Hikari Hotel",
                "emails/otp-verification",
                variables
            );
        } catch (MessagingException e) {
            throw new ApiException("Không thể gửi email OTP: " + e.getMessage());
        }
    }

    public String verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException("Không tìm thấy user với email: " + request.email()));

        if (Boolean.TRUE.equals(user.getIsVerified())) {
            return "Tài khoản đã được xác thực trước đó.";
        }

        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            throw new ApiException("OTP chưa được tạo. Vui lòng đăng ký lại.");
        }

        if (new Date().after(user.getOtpExpiry())) {
            throw new ApiException("OTP đã hết hạn. Vui lòng yêu cầu gửi lại OTP.");
        }

        if (!user.getOtp().equals(request.otp())) {
            throw new ApiException("OTP không chính xác.");
        }

        user.setIsVerified(true);
        user.setStatus(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "Xác thực tài khoản thành công!";
    }

    public String resendOtp(ResendOtpRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException("Không tìm thấy user với email: " + request.email()));

        if (Boolean.TRUE.equals(user.getIsVerified())) {
            throw new ApiException("Tài khoản đã được xác thực.");
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
        userRepository.save(user);

        sendOtpEmail(user.getEmail(), otp);

        return "OTP mới đã được gửi đến email của bạn.";
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
