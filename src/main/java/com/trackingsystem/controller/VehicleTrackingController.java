package com.trackingsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.models.VehicleType;
import com.trackingsystem.repository.VehicleLocationRepository;
import com.trackingsystem.service.LocationService;

public class VehicleTrackingController {
	
	
	@Autowired
	public LocationService locationService;
	
	@Autowired
	public VehicleLocationRepository locationRepo;
	
	@PostMapping(path="/addVehicle")
	public String addLocation(
			@RequestParam("currentLong") String currentLong,
			@RequestParam("currentLat") String currentLat,
			@RequestParam("vehicleRegNum") VehicleReg vehicleRegNum,
			Model model
			) {
		
		try {
			VehicleLocation type = locationService.locateVehicle(currentLong, currentLat, vehicleRegNum);
			 // Optionally store owner in session
            return "Okay, Created";
		}catch (Exception exception) {
            model.addAttribute("errorMessage", "User not saved: " + exception.getMessage());
            return "server error 500"; 
        }
	}
	
	@PutMapping("/edit/{petId}")
	public String editLocation(
			@PathVariable Long logId, 
			@ModelAttribute VehicleLocation vehicleLoc, 
			Model model) {
	        System.out.println("Received petId: " + logId);
	        
	    try {
	        VehicleLocation typeInfo = locationService.editLocation(logId, vehicleLoc);

	        return "updated successfully"; // Redirect to adminDashboard after successful update
	    } catch (Exception e) {
	        model.addAttribute("error", "Error: " + e.getMessage());
	        return "error updating"; // Return to error page in case of an exception
	    }
	}
	
	
	//getting all data microservice
    @GetMapping("/all")
    public ResponseEntity<?> getAllLocations() {
        List<VehicleLocation> pets = locationService.getAllLocations();
        return ResponseEntity.ok(pets);
    }
    
    
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long logId) {
        try {
            // Check if the pet exists in the repository
            if (locationRepo.existsById(logId)) {
                // Delete the pet directly using the repository
                locationRepo.deleteById(logId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                // Pet not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Pet not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that occur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while deleting the pet");
        }
    }


}
