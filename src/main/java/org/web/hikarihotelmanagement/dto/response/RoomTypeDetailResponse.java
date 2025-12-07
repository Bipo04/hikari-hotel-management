package org.web.hikarihotelmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.web.hikarihotelmanagement.enums.RoomClass;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDetailResponse {
    private Long id;
    private String name;
    private RoomClass roomClass;
    private String description;
    private Integer capacity;
    private BigDecimal price;
    private List<AmenityResponse> amenities; // Danh sách tiện nghi
    private List<RoomDetailResponse> availableRooms; // Danh sách phòng có thể đặt
}
