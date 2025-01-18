package com.beautytouch.beautytouch.security;

import com.beautytouch.beautytouch.entity.Role;
import com.beautytouch.beautytouch.entity.User;
import com.beautytouch.beautytouch.repositories.UsersRepositories;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepositories userRepository;

    public CustomUserDetailsService(UsersRepositories userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Lấy người dùng từ cơ sở dữ liệu theo email
        User user = userRepository.getUsersByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Debug log
//        System.out.println("User found: " + user.getEmail() + " with role: " + user.getRole().name());

        // Chuyển role thành "ROLE_" prefix
        // Không cần gọi name() nữa, dùng directly user.getRole().toString()
        String role = "ROLE_" + user.getRole().toString().toUpperCase();  // Convert role to upper case for consistency

        // Trả về đối tượng UserDetails để Spring Security xác thực
        return new CustomUserDetails(
                user.getEmail(),
                user.getPassword(),
                user.getName(),

               // Lấy tên người dùng
                role,
                user.getId()
        );
    }


}
