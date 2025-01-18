package com.beautytouch.beautytouch.repositories;

import com.beautytouch.beautytouch.entity.EmailVerificationToken;
import com.beautytouch.beautytouch.entity.User;
import com.beautytouch.beautytouch.services.VerificationTokenService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    EmailVerificationToken findByToken(String token);
    EmailVerificationToken findByUser(User user);
}
