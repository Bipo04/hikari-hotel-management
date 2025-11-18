package org.web.hikarihotelmanagement.dto.request;

import lombok.Data;

@Data
public class RoomRequest {
    private String roomNumber;
    private Long roomTypeId;
    private String description;
    private String status;
}
