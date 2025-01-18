package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.Studio;


import com.beautytouch.beautytouch.entity.StudioService;
import com.beautytouch.beautytouch.entity.User;
import com.beautytouch.beautytouch.entity.appointments;
import com.beautytouch.beautytouch.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class bookAppointment {
    @Autowired
    Studio_Service studio_service;
    @Autowired
    Studio_ServiceService studio_serviceService; // Dịch vụ xử lý StudioService
    @Autowired
    private EmailService emailService;
    @Autowired
    UserService userService;
    @Autowired
    AppointmentService appointmentService;

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


    @GetMapping("/list-appointments")
    public String viewAppointments(Model model) {
        // Lấy thông tin người dùng hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getUser(username);

        // Lấy danh sách lịch hẹn theo user ID
        List<appointments> appointmentsList = appointmentService.getAppointmentsByUserId(currentUser.getId());

        // Thêm dữ liệu vào model để hiển thị trong view
        model.addAttribute("appointments", appointmentsList);

        return "view-appointment"; // Tên của file HTML hiển thị danh sách lịch hẹn
    }

    @PostMapping("/delete-id-appointments")
    public String deleteAppointment(@RequestParam("id") Integer id) {
        try {

            appointmentService.deleteById(id);
            return "redirect:/list-appointments";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error";
        }
    }


    @GetMapping("/studio-appointments")
    public String displayStudiosAndAppointments(
            @RequestParam(value = "studioId", required = false) Integer studioId,  // ID of the selected studio
            Model model) {
        // Get the logged-in user's information
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getUser(username);

        if (currentUser == null) {
            throw new RuntimeException("User not found."); // Ensure we have a valid user
        }

        // Retrieve studios owned by the logged-in user
        List<Studio> userStudios = studio_service.getStudiosByUserId(currentUser.getId());
        model.addAttribute("studios", userStudios);  // Pass studios to the view

        List<appointments> appointmentsForStudio;

        // Fetch appointments if `studioId` is selected
        if (studioId != null && studioId != -1) {
            appointmentsForStudio = appointmentService.getAppointmentsByStudioIds(List.of(studioId));
            model.addAttribute("selectedStudioId", studioId); // Pass selected studio ID
        } else {
            // No specific studio selected; return empty appointments list
            appointmentsForStudio = List.of();
            model.addAttribute("selectedStudioId", -1);  // Default to no studio selected
        }

        // Add appointments data to the model
        model.addAttribute("appointments", appointmentsForStudio);

        return "studio_appointments"; // Return Thymeleaf template
    }



    //upate trạng thái trong lịch đặt của studio
    @PostMapping("/appointments/update-status")
    public String updateAppointmentStatus(@RequestParam Integer appointmentId, @RequestParam String status) {
        // Tìm lịch hẹn dựa vào ID
        appointments appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment != null) {
            // Điều kiện: Chỉ khi trạng thái hiện tại là "pending" mới cập nhật được thành "in progress".
            if (appointment.getStatus().equals("pending") && status.equals("in progress")) {
                appointment.setStatus(status); // Cập nhật trạng thái thành in progress
                appointmentService.saveAppointment(appointment); // Lưu lại thay đổi
            }
        }

        return "redirect:/studio-appointments"; // Chuyển hướng về trang quản lý studio
    }
//thanh toán sau khi đặt lịch

    @PostMapping("/payment/complete")
    public String completePayment(@RequestParam Integer appointmentId,
                                  @RequestParam String userEmail) {
        appointments appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment != null) {
            appointment.setStatus("paid");
            appointmentService.saveAppointment(appointment);

            // Gửi email thông báo
            String subject = "Lịch sử giao dịch thanh toán";
            String content = "Thanh toán thành công cho lịch hẹn #" + appointmentId
                    + ". Giá: " + appointment.getPriceAppoint() + " VND.";
            emailService.sendEmail(userEmail, subject, content); // Tự viết service Email

            return "redirect:/list-appointments"; // Trả về danh sách lịch hẹn
        } else {
            return "error";
        }
    }


    //role studio xác nhân đã thanh toán chuyển sst confirm
    @PostMapping("/appointments/confirm")
    public String confirmAppointment(@RequestParam Integer appointmentId) {
        appointments appointment = appointmentService.getAppointmentById(appointmentId);

        if (appointment != null) {
            appointment.setStatus("confirmed");
            appointmentService.saveAppointment(appointment);
        }

        return "redirect:/studio-appointments";
    }
}
