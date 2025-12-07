package org.web.hikarihotelmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web.hikarihotelmanagement.service.VNPayService;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "API thanh toán VNPay")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {
    
    private final VNPayService vnPayService;
    
    @PostMapping("/vnpay/create/{bookingId}")
    @Operation(summary = "Tạo URL thanh toán VNPay", description = "Tạo URL redirect để thanh toán qua VNPay")
    public ResponseEntity<Map<String, String>> createPayment(
            @PathVariable Long bookingId,
            HttpServletRequest request
    ) {
        String paymentUrl = vnPayService.createPaymentUrl(bookingId, request);
        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }
    
    @GetMapping("/vnpay/callback")
    @Operation(summary = "Xử lý callback từ VNPay", description = "Endpoint để VNPay gọi về sau khi thanh toán")
    public ResponseEntity<Map<String, String>> handleCallback(HttpServletRequest request) {
        Map<String, String> result = vnPayService.handleCallback(request);
        return ResponseEntity.ok(result);
    }
}
