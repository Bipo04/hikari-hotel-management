package org.web.hikarihotelmanagement.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

public interface VNPayService {
    String createPaymentUrl(Long bookingId, HttpServletRequest request);
    Map<String, String> handleCallback(HttpServletRequest request);
}
