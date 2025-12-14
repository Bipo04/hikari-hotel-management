package org.web.hikarihotelmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.web.hikarihotelmanagement.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private String bookingCode;
    private BookingStatus status;
    private String paymentMethod;
    private BigDecimal amount;
    private String bookingNote;
    private List<BookingRoomResponse> rooms;
    private LocalDateTime createdAt;
    private String paymentUrl;
    private Integer discount;
}
