package org.web.hikarihotelmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.web.hikarihotelmanagement.enums.RoomClass;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoomTypeResponse {
    private Long id;
    private String name;
    private RoomClass roomClass;
    private String description;
    private Integer capacity;
    private BigDecimal price;
    private Integer availableRoomCount; // Số lượng phòng còn trống
    private String primaryImageUrl;
}
