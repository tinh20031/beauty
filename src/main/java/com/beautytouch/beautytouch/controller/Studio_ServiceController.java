package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.entity.StudioService;
import com.beautytouch.beautytouch.services.Studio_ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

@Controller
public class Studio_ServiceController {
    private final Studio_ServiceService studio_serviceService;

    // Constructor injection
    public Studio_ServiceController(Studio_ServiceService studio_serviceService) {
        this.studio_serviceService = studio_serviceService;
    }

    @GetMapping("/studio/{id}")
    public String getStudioDetails(@PathVariable("id") Integer studioId, Model model) {
        // Lấy thông tin studio
        Studio studio = studio_serviceService.GetStudioById(studioId);
        // Lấy các dịch vụ của studio
        List<StudioService> services = studio_serviceService.getServicesByStudioId(studioId);

        // Thêm vào model
        model.addAttribute("studio", studio);
        model.addAttribute("services", services);

        return "shop_detail";
    }

}
