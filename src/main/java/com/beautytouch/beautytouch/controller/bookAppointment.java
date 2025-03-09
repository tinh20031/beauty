package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.*;
import com.beautytouch.beautytouch.services.*;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    ReviewService reviewService;
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
    @GetMapping("/review-appointment")
    public String showReviewForm(@RequestParam("appointmentId") Integer appointmentId, Model model) {
        appointments appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null || !"confirmed".equals(appointment.getStatus())) {
            return "redirect:/list-appointments"; // Quay lại nếu không hợp lệ
        }

        model.addAttribute("appointment", appointment);
        model.addAttribute("studio", appointment.getStudio());
        model.addAttribute("service", appointment.getService());
        return "review-form"; // Trang form đánh giá
    }
    @PostMapping("/studios/{studioId}/reviews")
    public String saveReview(@PathVariable Integer studioId,
                             @RequestParam Integer appointmentId,
                             @RequestParam Integer rating,
                             @RequestParam String comment,
                             Model model) {
        try {
            // Lấy studio từ DB
            Studio studio = studio_service.GetStudioById(studioId);
            if (studio == null) {
                model.addAttribute("error", "Studio không tồn tại.");
                return "review-form";
            }

            // Lấy user hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.getUser(username);
            if (user == null) {
                model.addAttribute("error", "Người dùng không tồn tại.");
                return "review-form";
            }

            // Lấy service từ appointment (giả định appointment chứa service)
            appointments appointment = appointmentService.getAppointmentById(appointmentId);
            if (appointment == null) {
                model.addAttribute("error", "Lịch hẹn không tồn tại.");
                return "review-form";
            }
            StudioService service = appointment.getService();

            // Tạo đối tượng Review
            Review review = new Review();
            review.setRating(rating);
            review.setComment(comment);
            review.setStudio(studio);
            review.setUser(user);
            review.setService(service);

            // Lưu vào DB
            reviewService.saveReview(review);

            // Thêm thông báo thành công
            model.addAttribute("message", "Đánh giá đã được gửi thành công!");
            return "redirect:/list-appointments";
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi gửi đánh giá. Vui lòng thử lại! " + e.getMessage());
            return "review-form";
        }
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
            @RequestParam(value = "studioId", required = false) Integer studioId,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.getUser(username);

        if (currentUser == null) {
            throw new RuntimeException("User not found.");
        }


        List<Studio> userStudios = studio_service.getStudiosByUserId(currentUser.getId());
        model.addAttribute("studios", userStudios);

        List<appointments> appointmentsForStudio;


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



    //thanh toán cho user
    @PostMapping("/payment")
    public String generateMomoPayment(@RequestParam Integer appointmentId, Model model) {
        // Lấy lịch hẹn theo ID
        appointments appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment == null) {
            model.addAttribute("error", "Lịch hẹn không tồn tại.");
            return "error"; // Trả về trang lỗi nếu không tìm thấy lịch hẹn
        }

        // Lấy giá trị lịch hẹn
        String priceAppointStr = appointment.getPriceAppoint();
        double originalPrice;

        // Chuyển đổi giá trị từ String sang double
        try {
            originalPrice = Double.parseDouble(priceAppointStr);
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Giá trị lịch hẹn không hợp lệ.");
            return "error"; // Trả về trang lỗi nếu giá trị không hợp lệ
        }

        // Lấy tên dịch vụ
        String serviceType = appointment.getService().getService().getServiceName().trim().toLowerCase();
        System.out.println("Tên dịch vụ: " + serviceType); // Debug kiểm tra dịch vụ thực tế

        // Xác định mức giảm giá
        double discountPercentage = serviceType.equalsIgnoreCase("combo chụp ảnh") ? 0.12 : 0.10;
        double discountAmount = originalPrice * discountPercentage;
        double paymentAmount = originalPrice - discountAmount;

        System.out.println("Mức giảm giá thực tế: " + (discountPercentage * 100) + "%");
        System.out.println("Số tiền giảm: " + discountAmount);
        System.out.println("Số tiền thanh toán: " + paymentAmount);

        // Định dạng số tiền thanh toán
        DecimalFormat df = new DecimalFormat("#,###");
        String formattedPaymentAmount = df.format(paymentAmount);
        String formattedDiscountAmount = df.format(discountAmount);


        model.addAttribute("originalPrice", df.format(originalPrice) + " VND");
        model.addAttribute("discountAmount", formattedDiscountAmount + " VND");
        model.addAttribute("paymentAmount", formattedPaymentAmount + " VND");
        model.addAttribute("qrCodeUrl", "URL_CỦA_MÃ_QR");
        model.addAttribute("payUrl", "URL_CỦA_TRANG THANH TOÁN");

        return "payment_vnpay";
    }


    @GetMapping("/admin_appoint")
    public String viewAllAppointments(Model model) {
        List<appointments> allAppointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", allAppointments);
        return "template_admin";
    }



}
