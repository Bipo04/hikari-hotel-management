package org.web.hikarihotelmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.web.hikarihotelmanagement.dto.request.LoginRequest;
import org.web.hikarihotelmanagement.dto.request.RegisterRequest;
import org.web.hikarihotelmanagement.dto.request.ResendOtpRequest;
import org.web.hikarihotelmanagement.dto.request.VerifyOtpRequest;
import org.web.hikarihotelmanagement.dto.response.AuthenticationResponse;
import org.web.hikarihotelmanagement.service.impl.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterRequest registerRequest) {
        String message = authenticationService.register(registerRequest);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody @Valid VerifyOtpRequest verifyOtpRequest) {
        String message = authenticationService.verifyOtp(verifyOtpRequest);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, String>> resendOtp(@RequestBody @Valid ResendOtpRequest resendOtpRequest) {
        String message = authenticationService.resendOtp(resendOtpRequest);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }
}
