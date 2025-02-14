package com.trackingsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleLocationRepository;

@Service
public class LocationService {

    @Autowired
    public VehicleLocationRepository locationRepo;

    // Create a new VehicleLocation
    public VehicleLocation locateVehicle(String currentLong, String currentLat, VehicleReg vehicleRegNum) throws Exception {
        // Set currentTime using system time
        VehicleLocation reg = new VehicleLocation(null, currentLong, currentLat, vehicleRegNum);
        reg.setCurrentTime(new Date());  // Set the current time
        return locationRepo.save(reg);  // Save and return the saved instance
    }

    // Edit an existing VehicleLocation
    public VehicleLocation editLocation(Long logId, VehicleLocation locationDetails) throws Exception {
        Optional<VehicleLocation> petOpt = locationRepo.findById(logId);
        if (petOpt.isEmpty()) {
            throw new Exception("Vehicle not found with id " + logId);
        }

        VehicleLocation reg = petOpt.get();
        // Update the fields of the existing VehicleLocation
        reg.setCurrentLong(locationDetails.getCurrentLong());
        reg.setCurrentLat(locationDetails.getCurrentLat());
        reg.setCurrentTime(new Date());  // Set the current time
        reg.setVehicleReg(locationDetails.getVehicleReg());
        
        return locationRepo.save(reg);
    }

    // Get all vehicle locations
    public List<VehicleLocation> getAllLocations() {
        return (List<VehicleLocation>) locationRepo.findAll();
    }

    // Delete a vehicle location by ID
    public boolean deleteLocation(Long logId) {
        Optional<VehicleLocation> pet = locationRepo.findById(logId);
        if (pet.isPresent()) {
            locationRepo.delete(pet.get());
            return true;
        }
        return false;
    }

    // Get a vehicle location by ID
    public VehicleLocation getVehicleById(Long logId) {
        return locationRepo.findById(logId).orElse(null);
    }
}
