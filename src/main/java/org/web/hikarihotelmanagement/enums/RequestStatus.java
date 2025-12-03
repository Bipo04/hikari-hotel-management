package org.web.hikarihotelmanagement.enums;

public enum RequestStatus {
    PAYMENT_PENDING(0, "Chờ thanh toán"),
    PAYMENT_COMPLETED(1, "Đã thanh toán"),
    CHECKED_IN(2, "Đã check-in"),
    CHECKED_OUT(3, "Đã check-out"),
    CANCELLED(4, "Đã hủy");

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
