package org.web.hikarihotelmanagement.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AmenityResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
