package com.beautytouch.beautytouch.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Định nghĩa PasswordEncoder
    }
    private final CustomUserDetailsService userDetailsService;
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    //lưu token
//    @Bean
//    public CsrfTokenRepository csrfTokenRepository() {
//        return new CookieCsrfTokenRepository();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/index", "/css/**", "/image/**","/login", "/signup","/register","/","/logout","/verify_email_template","/shop","/studio/{id}","/verify-email").permitAll()
                        .requestMatchers("/user/**").hasRole("USERS")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/role_studio/**").hasRole("STUDIO")
                        .anyRequest().authenticated() // All other paths require authentication
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(new CustomAuthenticationSuccessHandler()) // Custom login page

                        .loginProcessingUrl("/j_spring_security_check")// Redirect after successful login
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll())



                        .csrf(csrf -> csrf
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )

        ;




                ;

        return http.build();
    }

//    @Bean
//    public SpringSecurityDialect securityDialect() {
//        return new SpringSecurityDialect(); // Thymeleaf dialect for Spring Security
//    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService) // Sử dụng CustomUserDetailsService để load user
                .passwordEncoder(passwordEncoder());   // Mã hóa mật khẩu bằng BCrypt

        return authenticationManagerBuilder.build();
    }


}
