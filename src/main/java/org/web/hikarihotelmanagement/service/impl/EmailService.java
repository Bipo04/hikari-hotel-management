package org.web.hikarihotelmanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.api.url}")
    private String brevoApiUrl;

    @Value("${spring.mail.sender}")
    private String emailFrom;

    @Value("${spring.mail.sender.name:Hikari Hotel}")
    private String senderName;

    @Value("${spring.domain.name}")
    private String websiteUrl;

    @Async
    public void sendEmailWithTemplate(String to, String subject, String templateName, Map<String, Object> variables) {

        // Thêm website URL vào variables nếu chưa có
        if (variables != null && !variables.containsKey("websiteUrl")) {
            variables.put("websiteUrl", websiteUrl);
        }

        log.info("Chuẩn bị gửi email tới: {} với template: {}", to, templateName);

        try {
            Context context = new Context();
            if (variables != null) {
                context.setVariables(variables);
            }
            // Đảm bảo websiteUrl luôn có trong context
            context.setVariable("websiteUrl", websiteUrl);

            String htmlContent = templateEngine.process(templateName, context);
            log.debug("Xử lý template email thành công cho: {}", to);

            // Tạo request body cho Brevo API
            Map<String, Object> emailRequest = new HashMap<>();
            emailRequest.put("sender", Map.of("name", senderName, "email", emailFrom));
            emailRequest.put("to", List.of(Map.of("email", to)));
            emailRequest.put("subject", subject);
            emailRequest.put("htmlContent", htmlContent);

            // Tạo headers với API key
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", brevoApiKey);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(emailRequest, headers);

            // Gọi Brevo API
            restTemplate.postForObject(brevoApiUrl, request, Map.class);

            log.info("Gửi email thành công tới: {} với chủ đề: {} qua Brevo API", to, subject);

        } catch (Exception e) {
            log.error("Lỗi khi gửi email tới: {} - {}", to, e.getMessage(), e);
            throw new RuntimeException("Không thể gửi email: " + e.getMessage(), e);
        }
    }
}
