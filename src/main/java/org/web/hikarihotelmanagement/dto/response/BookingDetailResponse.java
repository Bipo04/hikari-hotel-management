package org.web.hikarihotelmanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.web.hikarihotelmanagement.enums.BookingStatus;
import org.web.hikarihotelmanagement.enums.IdentityType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Thông tin chi tiết đơn đặt phòng")
public record BookingDetailResponse(
        @Schema(description = "ID đơn đặt", example = "1")
        Long id,

        @Schema(description = "ID người đặt", example = "1")
        Long userId,

        @Schema(description = "Mã đơn đặt", example = "BK1001")
        String bookingCode,

        @Schema(description = "Tên loại phòng", example = "Phòng Standard")
        String roomType,

        @Schema(description = "Ảnh đại diện loại phòng", example = "/images/room-standard.jpg")
        String image,

        @Schema(description = "Danh sách request trong booking")
        List<RequestItemResponse> requests,

        @Schema(description = "Tổng tiền", example = "5000000")
        BigDecimal price,

        @Schema(description = "Trạng thái đơn đặt", example = "CONFIRMED")
        BookingStatus status,

        @Schema(description = "Ngày đặt")
        LocalDateTime bookingDate
) {
    @Schema(description = "Thông tin từng request (phòng)")
    public record RequestItemResponse(
            @Schema(description = "ID request", example = "1")
            Long id,

            @Schema(description = "Số phòng", example = "Phòng 601")
            String roomNumber,

            @Schema(description = "Số khách", example = "3")
            Integer guests,

            @Schema(description = "Ngày check-in")
            LocalDateTime checkIn,

            @Schema(description = "Ngày check-out")
            LocalDateTime checkOut,

            @Schema(description = "Trạng thái request", example = "CONFIRMED")
            String status,

            @Schema(description = "Lưu ý")
            String note,

            @Schema(description = "Danh sách khách (chỉ có khi đã check-in hoặc check-out)")
            List<GuestResponse> guestList
    ) {}

    @Schema(description = "Thông tin khách")
    public record GuestResponse(
            @Schema(description = "ID khách", example = "1")
            Long id,

            @Schema(description = "Họ tên", example = "Nguyễn Văn A")
            String fullName,

            @Schema(description = "Loại giấy tờ", example = "CITIZEN_ID")
            IdentityType identityType,

            @Schema(description = "Số giấy tờ", example = "001234567890")
            String identityNumber,

            @Schema(description = "Ngày cấp")
            LocalDate identityIssuedDate,

            @Schema(description = "Nơi cấp", example = "Công an TP. Hồ Chí Minh")
            String identityIssuedPlace
    ) {}
}
