package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.entity.StudioService; // Entity StudioService


import com.beautytouch.beautytouch.services.Studio_Service;
import com.beautytouch.beautytouch.services.Studio_ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class bookAppointment {
    @Autowired
    Studio_Service studio_service;
    @Autowired
    Studio_ServiceService studio_serviceService; // Dịch vụ xử lý StudioService



    @PostMapping("/bookAppointment")
    public String bookAppointment(@RequestParam("studioId") Integer studioId,
                                  @RequestParam("serviceId") Integer serviceId,
                                  Model model) {
        // Lấy thông tin studio
        Studio studio = studio_service.GetStudioById(studioId);
        if (studio == null) {
            throw new RuntimeException("Studio không tồn tại với ID: " + studioId);
        }

        // Lấy thông tin dịch vụ
        StudioService selectedService = studio_serviceService.getServiceByStudioIdAndServiceId(studioId, serviceId);
        if (selectedService == null) {
            throw new RuntimeException("Dịch vụ không tồn tại với Studio ID: " + studioId + " và Service ID: " + serviceId);
        }

        // Thêm thông tin vào model
        model.addAttribute("studio", studio);
        model.addAttribute("service", selectedService);

        // Trả về view hiển thị
        return "appointment";
    }



}
