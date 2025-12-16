package org.web.hikarihotelmanagement.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.web.hikarihotelmanagement.dto.request.LoginRequest;
import org.web.hikarihotelmanagement.dto.request.RegisterRequest;
import org.web.hikarihotelmanagement.dto.request.ResendOtpRequest;
import org.web.hikarihotelmanagement.dto.request.VerifyOtpRequest;
import org.web.hikarihotelmanagement.dto.response.AuthenticationResponse;
import org.web.hikarihotelmanagement.entity.CustomerTier;
import org.web.hikarihotelmanagement.entity.User;
import org.web.hikarihotelmanagement.enums.Role;
import org.web.hikarihotelmanagement.exception.ApiException;
import org.web.hikarihotelmanagement.repository.CustomerTierRepository;
import org.web.hikarihotelmanagement.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final CustomerTierRepository customerTierRepository;

    public String register(RegisterRequest request) {
        log.info("Bắt đầu đăng ký cho email: {}", request.email());

        if (userRepository.findByEmail(request.email()).isPresent()) {
            log.warn("Đăng ký thất bại - email đã tồn tại: {}", request.email());
            throw new ApiException("Email đã tồn tại: " + request.email());
        }

        CustomerTier customerTier = customerTierRepository.findAllByOrderByTierOrderAsc().get(0);

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setPhone(request.phone());
        user.setBirthDate(request.birthDate());
        user.setRole(Role.USER);
        user.setCustomerTier(customerTier);
        user.setStatus(false);
        user.setIsVerified(false);

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(new Date(System.currentTimeMillis() + 5 * 60 * 1000));

        userRepository.save(user);
        log.info("Đăng ký người dùng thành công: {}", request.email());

        sendOtpEmail(user.getEmail(), otp);
        log.info("Email OTP đã được gửi tới: {}", request.email());

        return "Đăng ký thành công! Vui lòng kiểm tra email để xác thực OTP.";
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        log.info("Bắt đầu xác thực đăng nhập cho email: {}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Đăng nhập thất bại - không tìm thấy người dùng: {}", request.email());
                    return new ApiException("Không tìm thấy user với email: " + request.email());
                });

        if (user.getIsVerified() == null || !user.getIsVerified()) {
            log.warn("Đăng nhập thất bại - tài khoản chưa xác thực: {}", request.email());
            throw new ApiException("Tài khoản chưa được xác thực. Vui lòng xác thực OTP từ email.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        request.password()
                )
        );

        String token = jwtService.generateToken(user);
        log.info("Đăng nhập thành công cho email: {}", request.email());

        return new AuthenticationResponse(token, user.getEmail(), user.getName());
    }

    private void sendOtpEmail(String email, String otp) {
        log.info("Đang gửi email OTP tới: {}", email);
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("otp", otp);
            
            emailService.sendEmailWithTemplate(
                email,
                "Xác thực tài khoản Hikari Hotel",
                "emails/otp-verification",
                variables
            );
            log.info("Gửi email OTP thành công tới: {}", email);
        } catch (Exception e) {
            log.error("Gửi email OTP thất bại tới: {}. Lỗi: {}", email, e.getMessage());
            throw new ApiException("Không thể gửi email OTP: " + e.getMessage());
        }
    }

    public String verifyOtp(VerifyOtpRequest request) {
        log.info("Bắt đầu xác thực OTP cho email: {}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Xác thực OTP thất bại - không tìm thấy người dùng: {}", request.email());
                    return new ApiException("Không tìm thấy user với email: " + request.email());
                });

        if (Boolean.TRUE.equals(user.getIsVerified())) {
            log.info("Người dùng đã được xác thực trước đó: {}", request.email());
            return "Tài khoản đã được xác thực trước đó.";
        }

        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            log.warn("Xác thực OTP thất bại - OTP chưa được tạo cho: {}", request.email());
            throw new ApiException("OTP chưa được tạo. Vui lòng đăng ký lại.");
        }

        if (new Date().after(user.getOtpExpiry())) {
            log.warn("Xác thực OTP thất bại - OTP đã hết hạn cho: {}", request.email());
            throw new ApiException("OTP đã hết hạn. Vui lòng yêu cầu gửi lại OTP.");
        }

        if (!user.getOtp().equals(request.otp())) {
            log.warn("Xác thực OTP thất bại - OTP không chính xác cho: {}", request.email());
            throw new ApiException("OTP không chính xác.");
        }

        user.setIsVerified(true);
        user.setStatus(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        log.info("Xác thực OTP thành công cho email: {}", request.email());
        return "Xác thực tài khoản thành công!";
    }

    public String resendOtp(ResendOtpRequest request) {
        log.info("Yêu cầu gửi lại OTP cho email: {}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Gửi lại OTP thất bại - không tìm thấy người dùng: {}", request.email());
                    return new ApiException("Không tìm thấy user với email: " + request.email());
                });

        if (Boolean.TRUE.equals(user.getIsVerified())) {
            log.warn("Gửi lại OTP thất bại - tài khoản đã được xác thực: {}", request.email());
            throw new ApiException("Tài khoản đã được xác thực.");
        }

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
        userRepository.save(user);

        log.info("Đã tạo OTP mới cho email: {}", request.email());

        sendOtpEmail(user.getEmail(), otp);

        log.info("Gửi lại OTP thành công cho email: {}", request.email());
        return "OTP mới đã được gửi đến email của bạn.";
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        log.debug("Đã tạo OTP: {}", otp);
        return String.valueOf(otp);
    }
}
