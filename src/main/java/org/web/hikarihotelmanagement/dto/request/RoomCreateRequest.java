package org.web.hikarihotelmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.web.hikarihotelmanagement.enums.RoomStatus;

@Data
public class RoomCreateRequest {
    @NotBlank
    private String roomNumber;

    @NotNull
    private Long roomTypeId;

    private String description;

    @NotNull
    private RoomStatus status;
}
