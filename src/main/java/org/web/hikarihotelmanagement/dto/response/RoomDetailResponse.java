package org.web.hikarihotelmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.web.hikarihotelmanagement.enums.RoomStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailResponse {
    private Long id;
    private String roomNumber;
    private String description;
    private RoomStatus status;
}
