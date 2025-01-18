package com.beautytouch.beautytouch.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Gửi email đơn giản thông qua SMTP
     *
     * @param toEmail Địa chỉ email người nhận
     * @param subject Nội dung tiêu đề email
     * @param body    Nội dung email
     */

    /**
     * Gửi email xác nhận tài khoản
     *
     * @param toEmail Địa chỉ email người nhận
     * @param token   Mã xác nhận
     */
    public void sendVerificationEmail(String toEmail, String token) {
        String subject = "Xác nhận tài khoản của bạn";
        String body = "<h1>Xác nhận tài khoản</h1>" +
                "<p>Chào bạn,</p>" +
                "<p>Mã xác nhận của bạn là: <strong>" + token + "</strong></p>" +
                "<p>Vui lòng nhập mã này trên trang xác nhận để kích hoạt tài khoản của bạn.</p>";

        // Gửi email
        sendEmail(toEmail, subject, body);
    }
    public void sendEmail(String toEmail, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // "true" để gửi nội dung HTML
            helper.setFrom("tinhtqqe170178@fpt.edu.vn");
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }
}