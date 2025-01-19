package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.*;
import com.beautytouch.beautytouch.services.AppointmentService;
import com.beautytouch.beautytouch.services.Studio_Service;
import com.beautytouch.beautytouch.services.Studio_ServiceService;
import com.beautytouch.beautytouch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
public class view_appointment {
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    UserService userService;
    @Autowired
    Studio_Service studio_service;
    @Autowired
    Studio_ServiceService studio_serviceService;

    @PostMapping("/bookAppointment/create")
    public String createAppointment(
            @RequestParam String price,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam String studioId,
            @RequestParam String serviceId) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User currentUser = userService.getUser(username);

            Integer studioIdInt = Integer.parseInt(studioId);
            Integer serviceIdInt = Integer.parseInt(serviceId);

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate appointmentDate = LocalDate.parse(date, dateFormatter);  // Use LocalDate instead of LocalDateTime
            String formattedDate = appointmentDate.toString(); // Save in 'yyyy-MM-dd' format


            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mma");
            LocalTime appointmentTime = LocalTime.parse(time.toUpperCase(), timeFormatter);
            String formattedTime = appointmentTime.format(DateTimeFormatter.ofPattern("h:mma"));
            appointments appointment = new appointments();
            appointment.setAppointmentDate(formattedDate);
            appointment.setTime(formattedTime);
            appointment.setPriceAppoint(price);

            appointment.setStatus("pending".trim().toLowerCase());
//            appointment.setStatus(AppointmentStatus.PENDING); // Đặt trạng thái mặc định

            appointment.setUser(currentUser);

            Studio studio = studio_service.GetStudioById(studioIdInt);  // Using Integer version
            appointment.setStudio(studio);

            StudioService studioServiceEntity = studio_serviceService.getServiceById(serviceIdInt);  // Using Integer version
            appointment.setService(studioServiceEntity);


            appointmentService.saveAppointment(appointment);


            return "redirect:/list-appointments";

        } catch (NumberFormatException e) {

            e.printStackTrace();
            return "error";
        }
    }
}

