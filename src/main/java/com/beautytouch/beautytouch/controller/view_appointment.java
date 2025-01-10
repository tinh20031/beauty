package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.*;
import com.beautytouch.beautytouch.services.AppointmentService;
import com.beautytouch.beautytouch.services.Studio_Service;
import com.beautytouch.beautytouch.services.Studio_ServiceService;
import com.beautytouch.beautytouch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam String studioId,  // studioId is still a String
            @RequestParam String serviceId) {  // serviceId is still a String

        try {
            // Convert studioId and serviceId from String to Integer
            Integer studioIdInt = Integer.parseInt(studioId);  // Convert studioId to Integer
            Integer serviceIdInt = Integer.parseInt(serviceId);  // Convert serviceId to Integer

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate appointmentDate = LocalDate.parse(date, dateFormatter);  // Use LocalDate instead of LocalDateTime
            String formattedDate = appointmentDate.toString(); // Save in 'yyyy-MM-dd' format

            // Parse time (use the time string)
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mma");
            LocalTime appointmentTime = LocalTime.parse(time.toUpperCase(), timeFormatter);
            String formattedTime = appointmentTime.format(DateTimeFormatter.ofPattern("h:mma"));
            appointments appointment = new appointments();
            appointment.setAppointmentDate(formattedDate);
            appointment.setTime(formattedTime);
            appointment.setPriceAppoint(price);
            appointment.setStatus("pending");

            // Fetch the studio and service objects using the IDs (converted to Integer)
            Studio studio = studio_service.GetStudioById(studioIdInt);  // Using Integer version
            appointment.setStudio(studio);

            StudioService studioServiceEntity = studio_serviceService.getServiceById(serviceIdInt);  // Using Integer version
            appointment.setService(studioServiceEntity);

            // Call the service layer to save the appointment
            appointmentService.saveAppointment(appointment);

            // Redirect or return success message
            return "view-appointment";  // Redirect to a page that shows appointments or a confirmation page

        } catch (NumberFormatException e) {
            // Handle error if the studioId or serviceId are not valid integers
            e.printStackTrace();
            return "error";  // You can return an error view or handle it differently
        }
    }
}

