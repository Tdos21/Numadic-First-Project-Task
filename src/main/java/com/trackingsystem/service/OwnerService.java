package com.trackingsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	public VehicleOwner createOwner(String ownerFullName, String email, String ownerAddress, String ownerCellNumber, String password) throws Exception {
	    
	    if (ownerFullName == null || email == null || ownerAddress == null || ownerCellNumber == null || password == null) {
	        throw new Exception("All vehicle details are required");
	    }

	    // Check if the vehicle already exists in the database
	    VehicleOwner reg = new VehicleOwner(null, ownerFullName, email, ownerAddress,ownerCellNumber,password);

	    // Update the entity fields (if it exists)
	    reg.setOwnerFullName(ownerFullName);
	    reg.setEmail(email);
	    reg.setOwnerAddress(ownerAddress);
	    reg.setOwnerCellNumber(ownerCellNumber);
	    reg.setPassword(passwordEncoder.encode(password));

	    // Save the updated entity
	    return ownerRepository.save(reg);
	}
	
	
	@Transactional
    public List<VehicleOwner> getAllOwners() {
        return (List<VehicleOwner>) ownerRepository.findAll();
    }

    // Method to delete a pet by ID
    @Transactional
    public boolean deleteOwner(Long vehicleRegNum) {
        if (ownerRepository.existsById(vehicleRegNum)) {
            ownerRepository.deleteById(vehicleRegNum);
            return true;
        }
        return false;
    }


}
