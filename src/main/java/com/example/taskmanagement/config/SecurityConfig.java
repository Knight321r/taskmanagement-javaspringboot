package com.example.taskmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing purposes
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register","/auth/login").permitAll() // Allow unauthenticated access to register
//                        .anyRequest().authenticated() // Secure all other endpoints
                                .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults()); // Use HTTP Basic authentication
//                .httpBasic(Customizer.withDefaults()); // Use HTTP Basic authentication

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
