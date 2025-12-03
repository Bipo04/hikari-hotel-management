package org.web.hikarihotelmanagement.enums;

public enum BookingStatus {
    PAYMENT_PENDING(0, "Chờ thanh toán"),
    PAYMENT_COMPLETED(1, "Đã thanh toán"),
    CANCELLED(2, "Đã hủy"),
    DECLINED(3, "Bị từ chối");

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
