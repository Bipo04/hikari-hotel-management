package org.web.hikarihotelmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoomTypeRequest {
    
    @NotNull(message = "Ngày check-in không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    
    @NotNull(message = "Ngày check-out không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
}
