package com.trackingsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.trackingsystem.models.VehicleType;
import com.trackingsystem.repository.VehicleTypeRepository;
import com.trackingsystem.service.TypeService;

@Controller
@RequestMapping(path = "/addType")
public class VehicleTypeController {
	
	@Autowired
	public TypeService typeService;
	
	@Autowired
	public VehicleTypeRepository typeRepo;

	@PostMapping(path="/addVehicle")
	public String addType(
			@RequestParam("vehicleType") String vehicleType,
			@RequestParam("vehicleBrand") String vehicleBrand,
			@RequestParam("vehicleModel") Integer vehicleModel,
			Model model
			) {
		
		try {
			VehicleType type = typeService.createType(vehicleType, vehicleBrand, vehicleModel);
			 // Optionally store owner in session
            return "Okay, Created";
		}catch (Exception exception) {
            model.addAttribute("errorMessage", "User not saved: " + exception.getMessage());
            return "server error 500"; 
        }
	}
	
	@PutMapping("/edit/{petId}")
	public String editType(
			@PathVariable int typeId, 
			@ModelAttribute VehicleType vehicleTypes, 
			Model model) {
	        System.out.println("Received petId: " + typeId);
	        
	    try {
	        VehicleType typeInfo = typeService.editType(typeId, vehicleTypes);

	        return "updated successfully"; // Redirect to adminDashboard after successful update
	    } catch (Exception e) {
	        model.addAttribute("error", "Error: " + e.getMessage());
	        return "error updating"; // Return to error page in case of an exception
	    }
	}
	
	
	//getting all data microservice
    @GetMapping("/all")
    public ResponseEntity<?> getAllTypes() {
        List<VehicleType> pets = typeService.getAllTypes();
        return ResponseEntity.ok(pets);
    }
    
    
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<?> deleteType(@PathVariable int typeId) {
        try {
            // Check if the pet exists in the repository
            if (typeRepo.existsById(typeId)) {
                // Delete the pet directly using the repository
                typeRepo.deleteById(typeId);
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
