package org.web.hikarihotelmanagement.enums;

public enum IdentityType {
    CMND(0, "Chứng minh nhân dân"),
    CCCD(1, "Căn cước công dân"),
    PASSPORT(2, "Hộ chiếu"),
    DRIVER_LICENSE(3, "Bằng lái xe");

    private final int code;
    private final String description;

    IdentityType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static IdentityType fromCode(int code) {
        for (IdentityType type : IdentityType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid IdentityType code: " + code);
    }
}
