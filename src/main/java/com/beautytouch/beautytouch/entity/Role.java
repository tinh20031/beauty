package com.beautytouch.beautytouch.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    USERS("ROLE_USERS"),
    ADMIN("ROLE_ADMIN"),
    STUDIO("ROLE_STUDIO");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    /**
     * Chuyển đổi từ giá trị chuỗi trong cơ sở dữ liệu sang Role enum.
     * Lưu ý: Cần đảm bảo không có sự khác biệt chữ hoa và chữ thường
     */
    public static Role fromString(String role) {
        if (role != null) {
            switch (role.toLowerCase()) {
                case "users": return USERS;
                case "admin": return ADMIN;
                case "studio": return STUDIO;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }

    /**
     * Chuyển đổi giá trị enum thành String với tiền tố "ROLE_".
     * Sử dụng phương thức này khi bạn muốn truyền dữ liệu vào `GrantedAuthority`.
     */
    public String toGrantedAuthority() {
        return this.role;  // Trả về giá trị String như "ROLE_USERS", "ROLE_ADMIN", "ROLE_STUDIO"
    }
}
