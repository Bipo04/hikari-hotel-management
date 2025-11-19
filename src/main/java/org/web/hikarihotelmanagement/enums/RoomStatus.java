package org.web.hikarihotelmanagement.enums;

public enum RoomStatus {
    AVAILABLE(0, "Đang hoạt động"),
    MAINTENANCE(1, "Đang bảo trì"),
    CLEANING(2, "Đang dọn dẹp"),
    OUT_OF_SERVICE(3, "Ngưng hoạt động");

    private final int code;
    private final String description;

    RoomStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RoomStatus fromCode(int code) {
        for (RoomStatus status : RoomStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid RoomStatus code: " + code);
    }
}
