package com.trackingsystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackingsystem.models.VehicleOwner;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleOwnerRepository;
import com.trackingsystem.repository.VehicleRegRepository;
import jakarta.transaction.Transactional;

@Service
public class RegistrationService {
	
	@Autowired
	protected VehicleRegRepository vehicleRepository;
	
	@Autowired
	protected VehicleOwnerRepository ownerRepository;

	@Transactional
	public VehicleReg registerVehicle(Long vehicleRegNum, String vehicleName, String engineCapacity, String vehicleState, VehicleOwner vehicleOwner) throws Exception {
	    // Log input data
	    System.out.println("Received vehicle details:");
	    System.out.println("VehicleRegNum: " + vehicleRegNum);
	    System.out.println("VehicleName: " + vehicleName);
	    System.out.println("EngineCapacity: " + engineCapacity);
	    System.out.println("VehicleState: " + vehicleState);

	    // Validate the inputs
	    if (vehicleRegNum == null || vehicleName == null || vehicleState == null || engineCapacity == null || vehicleOwner == null) {
	        throw new Exception("All vehicle details are required");
	    }

	    // Check if the vehicle already exists in the database
	    VehicleReg reg = vehicleRepository.findById(vehicleRegNum)
	        .orElse(new VehicleReg(vehicleRegNum, 0, vehicleName, engineCapacity, vehicleState,vehicleOwner)); // Create new if not exists

	    // Update the entity fields (if it exists)
	    reg.setVehicleName(vehicleName);
	    reg.setEngineCapacity(engineCapacity);
	    reg.setVehicleState(vehicleState);

	    // Save the updated entity
	    return vehicleRepository.save(reg);
	}


    @Transactional // Ensures the entity is managed within a session
	public VehicleReg editVehicle(Long vehicleRegNum, VehicleReg vehicleDetails) throws Exception {
	        VehicleReg reg = vehicleRepository.findById(vehicleRegNum)
	                .orElseThrow(() -> new Exception("Vehicle not found with id " + vehicleRegNum));

	        // Update fields
	        reg.setVehicleRegNum(vehicleDetails.getVehicleRegNum());
	        reg.setVehicleName(vehicleDetails.getVehicleName());
	        reg.setEngineCapacity(vehicleDetails.getEngineCapacity());
	        reg.setVehicleState(vehicleDetails.getVehicleState());

	        return reg; // No need to explicitly save, JPA auto-detects changes
	    }

    @Transactional
    public List<VehicleReg> getAllVehicles() {
        List<VehicleReg> vehicles = vehicleRepository.findAll();
        
        // Debugging log
        System.out.println("Total Vehicles Found: " + vehicles.size());

        return vehicles;
    }
    
    
    // Method to delete a pet by ID
    @Transactional
    public boolean deleteVehicle(Long vehicleRegNum) {
        if (vehicleRepository.existsById(vehicleRegNum)) {
            vehicleRepository.deleteById(vehicleRegNum);
            return true;
        }
        return false;
    }
    
    public VehicleReg getVehicleById(Long vehicleRegNum) {
        // Assuming the PetRepository has a findById method
        return vehicleRepository.findById(vehicleRegNum).orElse(null);
    }
    
    public VehicleOwner getVehicleOwnerByEmail(String email) {
        VehicleOwner owner = ownerRepository.findByEmail(email);
        if (owner == null) {
            throw new RuntimeException("Vehicle owner not found with email: " + email);
        }
        return owner;
    }


    
    @Transactional
    public VehicleReg updateVehicle(VehicleReg updatedVehicle) throws Exception {
        // Load existing entity (optimistic lock via @Version)
        VehicleReg existing = vehicleRepository.findById(updatedVehicle.getVehicleRegNum())
                .orElseThrow(() -> new Exception("Vehicle not found with ID: " 
                                                  + updatedVehicle.getVehicleRegNum()));

        // Copy editable fields
        existing.setVehicleName(updatedVehicle.getVehicleName());
        existing.setEngineCapacity(updatedVehicle.getEngineCapacity());
        existing.setVehicleState(updatedVehicle.getVehicleState());

        // JPA will automatically bump @Version and save
        return vehicleRepository.save(existing);
    }



}
