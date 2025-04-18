package com.trackingsystem.config;

import com.trackingsystem.service.AdminUserDetailsService;
import com.trackingsystem.service.OwnerUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private AdminUserDetailsService adminUserDetailsService;
    
    @Autowired
    private OwnerUserDetailsService ownerUserDetailsService;

    
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
   

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(adminUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public DaoAuthenticationProvider ownerAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(ownerUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        
        authBuilder.authenticationProvider(authenticationProvider());         // admin
        authBuilder.authenticationProvider(ownerAuthenticationProvider());   // owner

        return authBuilder.build();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .authorizeHttpRequests(auth -> auth
        	    .requestMatchers("/assets/**", "/css/**", "/js/**", "/images/**").permitAll()
        	    .requestMatchers("/auth/register", "/auth/loginPage").permitAll()
        	    .requestMatchers("/api/systemAdmin/login", "/api/systemAdmin/create").permitAll()

        	    // 👇 Owner & Admin access endpoints
        	    .requestMatchers("/api/vehicleReg/**", "/api/registerOwner/**").hasAnyRole("ADMIN", "OWNER")

        	    // 👇 Admin-only routes
        	    .requestMatchers("/app/adminDash/**", "/api/admin/**").hasRole("ADMIN")
        	    
        	    .anyRequest().authenticated()
        	

            )
            .formLogin(form -> form
                .loginPage("/auth/loginPage") // Default fallback login page
                .loginProcessingUrl("/login") // Default login endpoint
                .successHandler(successHandler) // Redirect based on role
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/loginPage?logout=true")
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1)
                .expiredUrl("/auth/loginPage?expired=true")
                .and()
            )
            .csrf(csrf -> csrf.disable());

        // 🔐 Register both auth providers
        http.authenticationProvider(authenticationProvider());
        http.authenticationProvider(ownerAuthenticationProvider());

        return http.build();
    }

}
