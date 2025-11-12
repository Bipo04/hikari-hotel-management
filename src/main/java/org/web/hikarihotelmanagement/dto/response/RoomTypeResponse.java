package org.web.hikarihotelmanagement.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
