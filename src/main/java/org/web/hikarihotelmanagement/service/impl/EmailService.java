package org.web.hikarihotelmanagement.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.sender:no-reply@hikarihotel.com}")
    private String emailFrom;

    @Value("${spring.domain.name:http://localhost:8080}")
    private String websiteUrl;

    @Async
    public void sendEmailWithTemplate(String to, String subject, String templateName, Map<String, Object> variables)
            throws MessagingException {

        // Thêm website URL vào variables nếu chưa có
        if (variables != null && !variables.containsKey("websiteUrl")) {
            variables.put("websiteUrl", websiteUrl);
        }

        log.info("Chuẩn bị gửi email tới: {} với template: {}", to, templateName);

        Context context = new Context();
        if (variables != null) {
            context.setVariables(variables);
        }
        // Đảm bảo websiteUrl luôn có trong context
        context.setVariable("websiteUrl", websiteUrl);

        String htmlContent = templateEngine.process(templateName, context);
        log.debug("Xử lý template email thành công cho: {}", to);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(emailFrom);

        mailSender.send(message);
        log.info("Gửi email thành công tới: {} với chủ đề: {}", to, subject);
    }
}
