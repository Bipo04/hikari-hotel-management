package org.web.hikarihotelmanagement.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private String roomTypeName;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
