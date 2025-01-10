package com.beautytouch.beautytouch.security;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUsersByEmail(username); // Tìm người dùng bằng email

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // Chuyển đối tượng User thành UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name().toUpperCase()))
        );


    }
}
