package org.web.hikarihotelmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
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
public class RoomBookingRequest {
    
    @NotNull(message = "ID phòng không được để trống")
    private Long roomId;
    
    @NotNull(message = "Ngày check-in không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    
    @NotNull(message = "Ngày check-out không được để trống")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    
    @NotNull(message = "Số lượng khách không được để trống")
    @Min(value = 1, message = "Số lượng khách phải ít nhất là 1")
    private Integer numberOfGuests;
    
    private String note; // Ghi chú cho phòng này
}
