package com.trackingsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleRegRepository;
import com.trackingsystem.service.RegistrationService;


@RestController
@RequestMapping(path="/api/vehicleReg")
public class VehicleRegController {
	
	/*
	 @Autowired
	    public VehicleRegController(RegistrationService registrationService) {
	        this.registrationService = registrationService;
	    }
	
	*/
	
	
	@Autowired
	public RegistrationService registrationService;
	
	@PostMapping(path="/addVehicle")
	public String registerVehicle(
			@RequestParam("vehicleRegNum") Long vehicleRegNum,
			@RequestParam("vehicleName") String vehicleName,
			@RequestParam("engineCapacity") String engineCapacity,
			@RequestParam("vehicleState") String vehicleState,
			Model model
			) {
		
		try {
			VehicleReg reg = registrationService.registerVehicle(vehicleRegNum, vehicleName, engineCapacity, vehicleState);
			 // Optionally store owner in session
            return "Okay, Created";
		}catch (Exception exception) {
            model.addAttribute("errorMessage", "User not saved: " + exception.getMessage());
            return "server error"; 
        }
	}
	
	@PutMapping("/edit/{petId}")
	public String editVehicle(
			@PathVariable Long vehicleRegNum, 
			@ModelAttribute VehicleReg vehicleDetails, 
			Model model) {
	        System.out.println("Received petId: " + vehicleRegNum);
	        
	    try {
	        VehicleReg updatedInfo = registrationService.editVehicle(vehicleRegNum, vehicleDetails);

	        return "updated successfully"; // Redirect to adminDashboard after successful update
	    } catch (Exception e) {
	        model.addAttribute("error", "Error: " + e.getMessage());
	        return "error updating"; // Return to error page in case of an exception
	    }
	}
	
	
	//getting all data microservice
    @GetMapping("/all")
    public ResponseEntity<?> getAllVehicles() {
        List<VehicleReg> pets = registrationService.getAllVehicles();
        return ResponseEntity.ok(pets);
    }
    
    
    @Autowired
    public VehicleRegRepository vehicleRepo;
    
    
    /*
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long vehicleRegNum) {
        try {
            // Check if the pet exists in the repository
            if (vehicleRepository.existsById(vehicleRegNum)) {
                // Delete the pet directly using the repository
                vehicleRepo.deleteById(vehicleRegNum);
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

	*/

}
