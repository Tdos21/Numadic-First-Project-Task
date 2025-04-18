
package com.trackingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trackingsystem.dto.OwnerDetails;

import com.trackingsystem.models.VehicleOwner;

import com.trackingsystem.repository.VehicleOwnerRepository;

@Service
public class OwnerUserDetailsService implements UserDetailsService {

	@Autowired
    private VehicleOwnerRepository ownerRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        VehicleOwner owner = ownerRepo.findByEmail(email);
        if (owner == null) {
            throw new UsernameNotFoundException("Owner not found with email: " + email);
        }
        return new OwnerDetails(owner);
    }

}

