package com.trackingsystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trackingsystem.controller.PasswordResetRequest;
import com.trackingsystem.models.VehicleOwner;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleOwnerRepository;


import jakarta.transaction.Transactional;

@Service
public class OwnerService {
	
	@Autowired
	protected VehicleOwnerRepository ownerRepository;
	
    @Autowired
	private PasswordEncoder passwordEncoder;
	
    @Transactional
    public VehicleOwner createOwner(VehicleOwner owner) throws Exception {
        validateOwner(owner);

        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        return ownerRepository.save(owner);
    }

    @Transactional
    public VehicleOwner updateOwner(VehicleOwner updatedOwner) throws Exception {
        validateOwner(updatedOwner);

        VehicleOwner existing = ownerRepository.findById(updatedOwner.getOwnerId())
                .orElseThrow(() -> new Exception("Owner not found"));

        existing.setOwnerFullName(updatedOwner.getOwnerFullName());
        existing.setEmail(updatedOwner.getEmail());
        existing.setOwnerAddress(updatedOwner.getOwnerAddress());
        existing.setOwnerCellNumber(updatedOwner.getOwnerCellNumber());

        // Only update password if it's changed
        if (!updatedOwner.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(updatedOwner.getPassword()));
        }

        return ownerRepository.save(existing);
    }

    public VehicleOwner getOwnerById(Long id) {
        return ownerRepository.findById(id).orElse(null);
    }

    

    private void validateOwner(VehicleOwner owner) throws Exception {
        if (owner.getOwnerFullName() == null || owner.getEmail() == null || 
            owner.getOwnerAddress() == null || owner.getOwnerCellNumber() == null || 
            owner.getPassword() == null) {
            throw new Exception("All fields are required.");
        }
    }
	
	@Transactional
	public List<VehicleOwner> getAllOwners() {
	    List<VehicleOwner> owners = (List<VehicleOwner>) ownerRepository.findAll();
	    System.out.println("Retrieved Owners: " + owners.size()); // Debugging
	    return owners;
	}

    // Method to delete a pet by ID@Transactional
    public void deleteOwnerById(Long ownerId) {
        ownerRepository.deleteById(ownerId);
    }

    
    
    @Autowired
    private VehicleOwnerRepository repository;

    
    @Transactional
    public String resetPassword(PasswordResetRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match!";
        }

        Optional<VehicleOwner> vehicleOwner = Optional.ofNullable(repository.findByEmail(request.getEmail()));
        if (vehicleOwner.isEmpty()) {
            return "User not found!";
        }

        VehicleOwner owner = vehicleOwner.get();
        owner.setPassword(passwordEncoder.encode(request.getNewPassword()));
        repository.save(owner);

        return "Password updated successfully!";
    }


}
