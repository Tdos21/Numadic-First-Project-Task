
package com.trackingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trackingsystem.dto.AdminDetails;

import com.trackingsystem.models.SystemAdmin;

import com.trackingsystem.repository.SystemAdminRepository;


@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private SystemAdminRepository adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemAdmin admin = adminRepo.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin not found with username: " + username);
        }
        return new AdminDetails(admin);
    }
}

