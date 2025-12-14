package org.web.hikarihotelmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityBookingInfoDto {
    private LocalDate date;
    private Long bookingId;
    private String bookingCode;
}
