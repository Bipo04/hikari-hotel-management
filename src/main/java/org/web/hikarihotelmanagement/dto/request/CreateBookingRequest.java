package org.web.hikarihotelmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {
    
    @NotEmpty(message = "Danh sách phòng không được để trống")
    @Valid
    private List<RoomBookingRequest> rooms;

    @Schema(description = "Phương thức thanh toán", example = "VNPAY")
    @NotNull(message = "Phương thức thanh toán không được để trống")
    private String paymentMethod;

    @Schema(description = "Ghi chú")
    private String bookingNote;
}
