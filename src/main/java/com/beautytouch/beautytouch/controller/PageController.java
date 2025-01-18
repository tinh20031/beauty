package com.beautytouch.beautytouch.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//role cho user
public class PageController {
    @GetMapping("/index")
    @PreAuthorize("hasRole('USER')")
    public String home() {
        return "index";
    }
//    @RequestMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();  // Xóa session cũ
//        return "redirect:/login";
//    }
@GetMapping("/verify_email_template")
public String verify_email() {
    return "/verify_email";
}
    @GetMapping("/login")
    @PreAuthorize("hasRole('USER')")
    public String login() {
        return "login";
    }

    @GetMapping("/user/return_appointment")
    @PreAuthorize("hasRole('USER')")
    public String appointment() {
        return "appointment";
    }


    //role cho stuudio

    @GetMapping("/role_studio/studio_index")
    @PreAuthorize("hasRole('STUDIO')")
    public String adminDashboard(   ) {
        return"redirect:/list-studio";
    }

    @GetMapping("/role_studio/service_studio")
    @PreAuthorize("hasRole('STUDIO')")
    public String service_stu() {
        return "redirect:/list_service_studio";
    }

    @GetMapping("/role_studio/studio_appointments")
    @PreAuthorize("hasRole('STUDIO')")
    public String appoint_studio() {
        return "redirect:/studio-appointments";
    }
}







//role cho admin