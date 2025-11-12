package org.web.hikarihotelmanagement.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RoomTypeRequest {
    private String name;
    private String roomClass;
    private String description;
    private Integer capacity;
    private BigDecimal price;
}
