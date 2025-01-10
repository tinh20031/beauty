package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.services.Studio_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudioController {
    @Autowired
    private Studio_Service studioService;
    @GetMapping("/shop")
    public String ShowStudios (Model model){
        List<Studio> studioList = studioService.GetAllStudio();
        model.addAttribute("Studio",studioList );
        return "shop";
    }


//    @GetMapping("/studio/{id}")
//    public String showStudioDetails(@PathVariable("id") Integer studioId, Model model) {
//        Studio studio = studioService.GetStudioById(studioId);
//        model.addAttribute("studio", studio);
//        return "studio-detail"; // Trả về trang chi tiết của studio
//    }
}
