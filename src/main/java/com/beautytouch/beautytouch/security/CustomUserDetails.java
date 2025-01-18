package com.beautytouch.beautytouch.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class CustomUserDetails extends User {

    private String fullName;
    private String role;
    private Integer userId;  // Thêm thuộc tính userId

    // Constructor để khởi tạo CustomUserDetails với email, password, fullName, role và userId
    public CustomUserDetails(String email, String password, String fullName, String role, Integer userId) {
        super(email, password, Collections.singletonList(new SimpleGrantedAuthority(role.toUpperCase())));
        this.fullName = fullName;
        this.role = role;
        this.userId = userId;  // Lưu trữ userId
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public Integer getUserId() {
        return userId;  // Getter để lấy userId
    }
}
