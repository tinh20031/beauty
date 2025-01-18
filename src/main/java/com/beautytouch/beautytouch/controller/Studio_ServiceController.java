package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.BeautyService;
import com.beautytouch.beautytouch.entity.Studio;
import com.beautytouch.beautytouch.entity.StudioService;
import com.beautytouch.beautytouch.entity.User;
import com.beautytouch.beautytouch.repositories.Service_StudioRepositories;
import com.beautytouch.beautytouch.services.Studio_Service;
import com.beautytouch.beautytouch.services.Studio_ServiceService;
import com.beautytouch.beautytouch.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Controller
public class Studio_ServiceController {
    private final Studio_ServiceService studio_serviceService;
    @Autowired
    private UserService userService;
    @Autowired
    private Studio_Service studio_service;
@Autowired
    Service_StudioRepositories service_studioRepositories;
    // Constructor injection
    public Studio_ServiceController(Studio_ServiceService studio_serviceService) {
        this.studio_serviceService = studio_serviceService;
    }

    @GetMapping("/studio/{id}")
    public String getStudioDetails(@PathVariable("id") Integer studioId, Model model) {
        // Lấy thông tin studio
        Studio studio = studio_serviceService.GetStudioById(studioId);

        List<StudioService> services = studio_serviceService.getServicesByStudioId(studioId);

        // Thêm vào model
        model.addAttribute("studio", studio);
        model.addAttribute("services", services);

        return "shop_detail";
    }



    //role studio
    //xem danh sách dịch vụ
    @GetMapping("/list_service_studio")
    public String listStudiosAndServices(@RequestParam(value = "studioId", required = false) Integer studioId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getUser(username);

        if (currentUser == null) {
            model.addAttribute("errorMessage", "User not found.");
            return "error";
        }

        // Fetch all studios of the current user
        List<Studio> studios = studio_service.getStudiosByUserId(currentUser.getId());
        model.addAttribute("studios", studios);

        // Fetch services for the selected studio (or the first one by default)
        if (studioId == null && !studios.isEmpty()) {
            studioId = studios.get(0).getId(); // Select the first studio if none is specified
        }

        if (studioId != null) {
            List<StudioService> listServices = studio_serviceService.getServicesByStudioId(studioId);
            model.addAttribute("services", listServices);
            model.addAttribute("selectedStudioId", studioId);
        }

        return "service_studio"; // Name of the view
    }

    // thêm danh sách

    @GetMapping("/service_studio/add")
    public String showCreateServiceForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getUser(username);

        // Check if the current user is valid
        if (currentUser == null) {
            model.addAttribute("errorMessage", "User not found.");
            return "error";
        }

        // Fetch all studios for this user
        List<Studio> studios = studio_service.getStudiosByUserId(currentUser.getId());
        model.addAttribute("studios", studios);

        // Add an empty StudioService object for form binding
        model.addAttribute("studioService", new StudioService());

        return "create_form_service"; // Render the form template
    }


    @GetMapping("/service_studio/edit/{id}")
    public String showEditServiceForm(@PathVariable("id") Integer serviceId, Model model) {
        // Tìm StudioService theo id
        StudioService studioService = studio_serviceService.getServiceById(serviceId);
        if (studioService == null) {
            model.addAttribute("errorMessage", "Service not found.");
            return "error";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getUser(username);

        if (currentUser == null) {
            model.addAttribute("errorMessage", "User not found.");
            return "error";
        }

        // Fetch all studios for form select options
        List<Studio> studios = studio_service.getStudiosByUserId(currentUser.getId());
        model.addAttribute("studios", studios);

        // Add the existing service for form binding
        model.addAttribute("studioService", studioService);

        return "create_form_service"; // Render the same form
    }
    @PostMapping("/service_studio/add-or-edit")
    public String saveOrUpdateService(@Valid @ModelAttribute("studioService") StudioService studioService,
                                      BindingResult bindingResult,
                                      Model model) {
        if (bindingResult.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.getUser(username);

            if (currentUser != null) {
                List<Studio> studios = studio_service.getStudiosByUserId(currentUser.getId());
                model.addAttribute("studios", studios);
            }

            return "create_form_service"; // Return the form with validation errors
        }

        // Save the `BeautyService` if it's a new one
        BeautyService beautyService = studioService.getService();
        if (beautyService != null && beautyService.getId() == null) {
            // Save transient BeautyService first to prevent the error
            BeautyService savedService = service_studioRepositories.save(beautyService);
            studioService.setService(savedService);
        }

        if (studioService.getId() == null) {
            // New StudioService
            studio_serviceService.saveService(studioService);
        } else {
            // Update existing StudioService
            StudioService existingService = studio_serviceService.getServiceById(studioService.getId());
            if (existingService != null) {
                existingService.setCustomPrice(studioService.getCustomPrice());
                existingService.setService(studioService.getService());

                if (studioService.getStudio() != null) {
                    existingService.setStudio(studioService.getStudio());
                }

                studio_serviceService.saveService(existingService);
            }
        }

        return "redirect:/list_service_studio"; // Redirect to the service list
    }


    @PostMapping("/delete-service/{id}")
    public String deleteService(@PathVariable("id") Integer serviceId,
                                RedirectAttributes redirectAttributes) {
        try {
            // Call service method to delete
            studio_serviceService.deleteServiceById(serviceId);
            redirectAttributes.addFlashAttribute("successMessage", "đã xóa dịch vụ thành công ");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/list_service_studio";
    }
}
