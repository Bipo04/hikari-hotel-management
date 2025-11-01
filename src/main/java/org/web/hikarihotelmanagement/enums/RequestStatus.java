package org.web.hikarihotelmanagement.enums;

public enum RequestStatus {
    PENDING(0, "Chờ xử lý"),
    CONFIRMED(1, "Đã xác nhận"),
    PAYMENT_PENDING(2, "Chờ thanh toán"),
    PAYMENT_COMPLETED(3, "Đã thanh toán"),
    CHECKED_IN(4, "Đã check-in"),
    CHECKED_OUT(5, "Đã check-out"),
    CANCELLED(6, "Đã hủy"),
    COMPLETED(7, "Hoàn thành");

    private final int code;
    private final String description;

    RequestStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RequestStatus fromCode(int code) {
        for (RequestStatus status : RequestStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid RequestStatus code: " + code);
    }
}
