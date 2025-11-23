package org.web.hikarihotelmanagement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.web.hikarihotelmanagement.config.CloudflareR2Config;
import org.web.hikarihotelmanagement.exception.ApiException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudflareR2Service {

    private final S3Client s3Client;
    private final CloudflareR2Config r2Config;

    @Value("${upload.allowed-content-types}")
    private String allowedContentTypesStr;

    @Value("${upload.max-file-size}")
    private long maxFileSize;

    public String uploadFile(MultipartFile file) {
        validateFile(file);

        String key = generateFileName(file.getOriginalFilename());

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(r2Config.getBucketName())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("Tải file thành công: {}", key);
            return key;

        } catch (S3Exception e) {
            log.error("Lỗi khi tải file lên R2: {}", e.getMessage());
            throw new ApiException("Ảnh không tải được: " + e.getMessage());
        } catch (IOException e) {
            log.error("Lỗi khi đọc file: {}", e.getMessage());
            throw new ApiException("Ảnh không tải được: " + e.getMessage());
        }
    }

    public void deleteFile(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(r2Config.getBucketName())
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Xóa file thành công: {}", key);

        } catch (S3Exception e) {
            log.error("Lỗi khi xóa file từ R2: {}", e.getMessage());
            throw new ApiException("Ảnh không xóa được: " + e.getMessage());
        }
    }

    public String getPublicUrl(String key) {
        return r2Config.getPublicUrl() + "/" + key;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("Ảnh không được để trống");
        }

        if (file.getSize() > maxFileSize) {
            throw new ApiException("Kích thước ảnh vượt quá giới hạn " + (maxFileSize / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        List<String> allowedContentTypes = Arrays.asList(allowedContentTypesStr.split(","));
        if (contentType == null || !allowedContentTypes.contains(contentType)) {
            throw new ApiException("Loại file không hợp lệ. Chỉ chấp nhận ảnh JPEG, PNG và WebP");
        }
    }

    private String generateFileName(String originalFileName) {
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
