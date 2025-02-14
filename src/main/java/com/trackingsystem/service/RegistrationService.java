package com.trackingsystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleRegRepository;
import jakarta.transaction.Transactional;

@Service
public class RegistrationService {
	
	@Autowired
	protected VehicleRegRepository vehicleRepository;

    // Constructor Injection (No need for @Autowired if there's only one constructor)
 

    // Register Vehicle method
    public VehicleReg registerVehicle(Long vehicleRegNum, String vehicleName, String engineCapacity, String vehicleState) throws Exception {
        
        // Optionally, validate the inputs here (e.g., check for null, empty, or incorrect values)
        if (vehicleRegNum == null || vehicleName == null || vehicleState == null || engineCapacity == null) {
            throw new Exception("All vehicle details are required");
        }
        
        // Create VehicleReg object
        VehicleReg reg = new VehicleReg(vehicleRegNum, vehicleName, engineCapacity, vehicleState);

        // Save the vehicle registration
        return vehicleRepository.save(reg); // Save and return the saved instance
    }
	
	
	public VehicleReg editVehicle(Long vehicleRegNum, VehicleReg vehicleDetails) throws Exception {
        Optional<VehicleReg> petOpt = vehicleRepository.findById(vehicleRegNum);
        if (petOpt.isEmpty()) {
            throw new Exception("Pet not found with id " + vehicleRegNum);
        }

        VehicleReg reg = petOpt.get();
        reg.setVehicleRegNum(vehicleDetails.getVehicleRegNum());
        reg.setVehicleName(vehicleDetails.getVehicleName());
        reg.setEngineCapacity(vehicleDetails.getEngineCapacity());
        reg.setVehicleState(vehicleDetails.getVehicleState());
        
        return vehicleRepository.save(reg);
    }


    @Transactional
    public List<VehicleReg> getAllVehicles() {
        return (List<VehicleReg>) vehicleRepository.findAll();
    }

    // Method to delete a pet by ID
    public boolean deleteVehicle(Long vehicleRegNum) {
        Optional<VehicleReg> pet = vehicleRepository.findById(vehicleRegNum);
        if (pet.isPresent()) {
        	vehicleRepository.delete(pet.get());
            return true;
        }
        return false;
    }

    
    public VehicleReg getVehicleById(Long vehicleRegNum) {
        // Assuming the PetRepository has a findById method
        return vehicleRepository.findById(vehicleRegNum).orElse(null);
    }


}
