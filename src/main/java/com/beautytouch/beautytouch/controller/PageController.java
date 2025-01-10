package com.beautytouch.beautytouch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String home() {
        return "index"; // Tệp index.html trong thư mục templates
    }
    @GetMapping("/login")
    public String login() {
        return "login"; // Returns the "login.html" view
    }
    @GetMapping("/test")
        public String test(){
            return "test";
        }

}
