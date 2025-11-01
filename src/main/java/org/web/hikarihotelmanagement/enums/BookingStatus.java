package org.web.hikarihotelmanagement.enums;

public enum BookingStatus {
    PENDING(0, "Đang chờ xử lý"),
    CONFIRMED(1, "Đã xác nhận"),
    PAYMENT_PENDING(2, "Chờ thanh toán"),
    PAYMENT_COMPLETED(3, "Thanh toán thành công"),
    CANCELLED(4, "Đã hủy"),
    DECLINED(5, "Bị từ chối");

    private final int code;
    private final String description;

    BookingStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static BookingStatus fromCode(int code) {
        for (BookingStatus status : BookingStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid BookingStatus code: " + code);
    }
}
