package com.beautytouch.beautytouch.services;

import com.beautytouch.beautytouch.entity.EmailVerificationToken;
import com.beautytouch.beautytouch.entity.User;
import com.beautytouch.beautytouch.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    // Tạo mã xác nhận mới
    public String generateVerificationToken(User user) {
        String token = String.format("%06d", new Random().nextInt(999999));
        Instant expiryDate = Instant.now().plus(15, ChronoUnit.MINUTES);

        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(expiryDate);

        tokenRepository.save(verificationToken);

        return token;
    }

    // Tìm token dựa theo user
    public EmailVerificationToken findByUser(User user) {
        return tokenRepository.findByUser(user); // Gọi repository ở đây
    }

    // Kiểm tra token hợp lệ
    public boolean isValidToken(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return false; // Token không tồn tại
        }
        return verificationToken.getExpiryDate().isAfter(Instant.now());
    }

    // Xóa token sau khi sử dụng
    public void deleteToken(String token) {
        EmailVerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }
    }

    // Tìm token theo giá trị
    public EmailVerificationToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}