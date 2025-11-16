package org.web.hikarihotelmanagement.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoomTypeResponse {
    private Long id;
    private String name;
    private String roomClass;
    private String description;
    private Integer capacity;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<AmenityResponse> amenities;

    @Data
    public static class AmenityResponse {
        private Long id;
        private String name;
        private String description;
    }
}
