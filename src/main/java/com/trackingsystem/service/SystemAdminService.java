package com.trackingsystem.service;

import com.trackingsystem.dto.CreateAdminRequest;
import com.trackingsystem.models.SystemAdmin;
import com.trackingsystem.repository.SystemAdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SystemAdminService {

    @Autowired
    private SystemAdminRepository adminRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public SystemAdmin createAdmin(CreateAdminRequest request) {
        SystemAdmin admin = new SystemAdmin();
        
        admin.setUsername(request.getUsername());
        admin.setPassword(encoder.encode(request.getPassword())); // hash password

        return adminRepo.save(admin);
    }
}
