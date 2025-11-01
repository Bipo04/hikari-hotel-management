package org.web.hikarihotelmanagement.enums;

public enum Role {
    USER(0, "Khách hàng"),
    ADMIN(1, "Quản trị viên");

    private final int code;
    private final String description;

    Role(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Role fromCode(int code) {
        for (Role role : Role.values()) {
            if (role.code == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role code: " + code);
    }
}
