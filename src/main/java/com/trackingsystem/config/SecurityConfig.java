package com.trackingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/loginRequest", "/auth/register").permitAll() // Allow login & registration
                .anyRequest().permitAll() // Require authentication for other routes
            )
            .formLogin(form -> form
                .loginPage("/auth/login") // Custom login page
                .defaultSuccessUrl("/app/homepage", true) // Redirect after successful login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // Always create a session
                .maximumSessions(1) // Limit one session per user
                .expiredUrl("/auth/loginRequest?expired=true") // Redirect when session expires
            )
            .csrf(csrf -> csrf.disable()); // Disable CSRF for now (enable in production)

        return http.build();
    }

}
