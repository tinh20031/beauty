package com.beautytouch.beautytouch.security;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Kiểm tra các role của người dùng
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDIO"))) {
            response.sendRedirect("/role_studio/studio_index");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            response.sendRedirect("/admin/template_admin");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USERS"))) {
            response.sendRedirect("/user/index");
        } else {
            response.sendRedirect("/");
        }
    }
}