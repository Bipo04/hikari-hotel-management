package org.web.hikarihotelmanagement.dto.request;

import lombok.Data;
import org.web.hikarihotelmanagement.enums.RoomStatus;

@Data
public class RoomRequest {
    private String roomNumber;
    private Long roomTypeId;
    private String description;
    private RoomStatus status;
}
