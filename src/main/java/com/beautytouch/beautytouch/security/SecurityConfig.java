package com.beautytouch.beautytouch.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/", "/css/**", "/js/**").permitAll() // Publicly accessible paths
                        .anyRequest().authenticated() // All other paths require authentication
                )
                .formLogin((form) -> form
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/test") // Redirect after successful login
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout") // Logout URL
                        .logoutSuccessUrl("/login?logout") // Redirect after successful logout
                        .permitAll()
                )

        ;

        return http.build();
    }

    PasswordEncoder encoder = new BCryptPasswordEncoder();
    String encodedPassword = encoder.encode("plainPassword"); // Mã hóa mật khẩu



}
