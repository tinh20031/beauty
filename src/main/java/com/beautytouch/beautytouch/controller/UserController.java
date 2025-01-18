package com.beautytouch.beautytouch.controller;

import com.beautytouch.beautytouch.entity.EmailVerificationToken;
import com.beautytouch.beautytouch.entity.User;
import com.beautytouch.beautytouch.repositories.UsersRepositories;
import com.beautytouch.beautytouch.services.EmailService;
import com.beautytouch.beautytouch.services.UserService;
import com.beautytouch.beautytouch.services.VerificationTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    UsersRepositories userRepository;
    @Autowired
    VerificationTokenService verificationTokenService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               @RequestParam("confirmPassword") String confirmPassword,
                               BindingResult result, Model model,
                               RedirectAttributes redirectAttributes) {
        // Kiểm tra lỗi từ validation
        if (result.hasErrors()) {
            return "signup"; // Nếu có lỗi, trả lại trang đăng ký
        }

        // Kiểm tra mật khẩu xác nhận có khớp không
        if (!confirmPassword.equals(user.getPassword())) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "signup";
        }

        // Lưu thông tin người dùng vào hệ thống (UserService xử lý lưu individual user entry)
        String registrationStatus = userService.registerUserWithVerification(user);

        if ("success".equals(registrationStatus)) {
            // Tạo mã xác nhận
            String token = verificationTokenService.generateVerificationToken(user);

//            // Tạo chủ đề và nội dung email
//            String subject = "Xác nhận email đăng ký tài khoản!";
//            String emailBody = "<h1>Xác nhận tài khoản</h1>" +
//                    "<p>Chào bạn <strong>" + user.getName() + "</strong>!</p>" +
//                    "<p>Cảm ơn bạn đã đăng ký tài khoản với chúng tôi.</p>" +
//                    "<p>Vui lòng nhập mã xác nhận dưới đây tại trang xác nhận:</p>" +
//                    "<p><strong>" + token + "</strong></p>" +
//                    "<p>Nếu bạn không yêu cầu đăng ký này, hãy bỏ qua email này.</p>";
//
//            // Gửi email
//            emailService.sendEmail(user.getEmail(), subject, emailBody);

            // Thông báo người dùng kiểm tra email
            redirectAttributes.addFlashAttribute("message",
                    "Đăng ký thành công! Mã xác nhận đã được gửi đến email của bạn. Vui lòng kiểm tra email.");
            return "redirect:/verify_email_template"; // Điều hướng đến trang nhập mã xác nhận
        } else {
            // Có lỗi trong khi đăng ký
            model.addAttribute("error", registrationStatus);
            return "signup";
        }
    }

    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        // Kiểm tra xem token có hợp lệ không
        if (!verificationTokenService.isValidToken(token)) {
            model.addAttribute("error", "Mã xác nhận không hợp lệ hoặc đã hết hạn!");
            return "verify_email"; // Trả về trang xác nhận nếu lỗi
        }

        // Nếu hợp lệ, kích hoạt tài khoản người dùng
        EmailVerificationToken verificationToken = verificationTokenService.findByToken(token);
        User user = verificationToken.getUser();
        user.setIsActive(true); // Kích hoạt tài khoản
        userRepository.save(user); // Lưu thông tin vào cơ sở dữ liệu

        // Xóa token sau khi xác nhận
        verificationTokenService.deleteToken(token);

        // Hiển thị thông báo thành công
        model.addAttribute("message", "Tài khoản của bạn đã được kích hoạt thành công!");
        return "login"; // Điều hướng người dùng đến trang đăng nhập
    }



}
