package com.beautytouch.beautytouch.services;

import com.beautytouch.beautytouch.entity.Role;
import com.beautytouch.beautytouch.entity.User;
import com.beautytouch.beautytouch.repositories.UsersRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UsersRepositories usersRepositories;
    private  final  PasswordEncoder passwordEncoder ;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private EmailService emailService;
    public UserService(UsersRepositories usersRepositories, PasswordEncoder passwordEncoder) {
        this.usersRepositories = usersRepositories;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public String registerUser(User user, String confirmPassword) {
        try {
            // Kiểm tra xác nhận mật khẩu
            if (!user.getPassword().equals(confirmPassword)) {
                return "Mật khẩu xác nhận không khớp.";
            }

            // Kiểm tra email đã tồn tại
            if (usersRepositories.getUsersByEmail(user.getEmail()).isPresent()) {
                return "Email đã tồn tại.";
            }

            // Gán role mặc định
            user.setRole("USER");



            // Mã hóa mật khẩu
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(Instant.now());

            System.out.println("Before save: " + user.toString()); // Log thông tin trước khi lưu
            usersRepositories.save(user);
            System.out.println("After save: User saved successfully"); // Log thông tin sau khi lưu

            return "Đăng ký thành công!";
        } catch (Exception e) {
            System.err.println("Error occurred during registration: " + e.getMessage());
            e.printStackTrace(); // In stack trace đầy đủ lỗi ra console
            return "Có lỗi xảy ra khi lưu dữ liệu!";
        }
    }

    public String getLoggedInUsername() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getName();
    }

    public User getUser(String email){
        return usersRepositories.findUserByEmail(email);

    }
    public String registerUserWithVerification(User user) {
        // Kiểm tra email đã tồn tại hay chưa
        if (usersRepositories.existsByEmail(user.getEmail())) {
            return "Email đã tồn tại. Vui lòng sử dụng email khác!";
        }

        // Lưu tài khoản với trạng thái chưa kích hoạt
        user.setIsActive(false);
        user.setRole("USER");
        User savedUser = usersRepositories.save(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(Instant.now());
        // Tạo mã xác nhận
        String verificationToken = verificationTokenService.generateVerificationToken(savedUser);

        // Gửi mã xác nhận qua email
        emailService.sendVerificationEmail(savedUser.getEmail(), verificationToken);

        return "success"; // Đăng ký thành công
    }
}




