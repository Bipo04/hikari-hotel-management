package org.web.hikarihotelmanagement.enums;

public enum RoomClass {
    STANDARD(0, "Standard"),
    SUPERIOR(1, "Superior"),
    BUSINESS(2, "Business"),
    SUITE(3, "Suite");

    private final int code;
    private final String description;

    RoomClass(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RoomClass fromCode(int code) {
        for (RoomClass roomClass : RoomClass.values()) {
            if (roomClass.code == code) {
                return roomClass;
            }
        }
        throw new IllegalArgumentException("Hạng phòng không hợp lệ " + code);
    }
}
