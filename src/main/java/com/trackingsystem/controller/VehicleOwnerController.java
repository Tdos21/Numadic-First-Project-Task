package com.trackingsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.trackingsystem.models.VehicleOwner;
import com.trackingsystem.repository.VehicleOwnerRepository;
import com.trackingsystem.repository.VehicleRegRepository;
import com.trackingsystem.service.OwnerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path="/api/registerOwner")
public class VehicleOwnerController {
	
	@Autowired
    public VehicleOwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

	@Autowired
	protected VehicleOwnerRepository ownerRepository;
	
	
	@Autowired
	public OwnerService ownerService;
	
	@PostMapping(path = "/addOwner")
	public String registerOwner(
	        @RequestParam("ownerFullName") String ownerFullName,
	        @RequestParam("email") String email,
	        @RequestParam("ownerAddress") String ownerAddress,
	        @RequestParam("ownerCellNumber") String ownerCellNumber,
	        @RequestParam("password") String password
	        ) {
	    try {
	        
	        VehicleOwner reg = ownerService.createOwner(ownerFullName, email, ownerAddress, ownerCellNumber,password);
	        return "index";
	    } catch (Exception exception) {
	        return "Error: " + exception.getMessage();
	    }
	}
	
	@GetMapping("/all")
    public ResponseEntity<?> getAllVehicles() {
        List<VehicleOwner> owners = ownerService.getAllOwners();
        return ResponseEntity.ok(owners);
    }
	
	@GetMapping("/getOwnerForm")
	public String getOwnerForm() {
		return "registerOwner";
	}
    
    
    @Autowired
    public VehicleRegRepository vehicleRepo;
    
    
    
//    @Autowired
//    private VehicleRegService vehicleRegService;

    @DeleteMapping("/delete/{ownerId}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long ownerId) {
        try {
            if (ownerService.deleteOwner(ownerId)) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Vehicle not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while deleting the vehicle");
        }
    }
    
    
    //editOwner
    
    @PostMapping(path = "/editOwner/{id}")
    public String editOwner(
            @RequestParam("ownerId") Long ownerId,
            @RequestParam("ownerFullName") String ownerFullName,
            @RequestParam("email") String email,
            @RequestParam("ownerAddress") String ownerAddress,
            @RequestParam("ownerCellNumber") String ownerCellNumber,
            @RequestParam("password") String password,
            Model model, HttpSession session) {

        // Check if the logged-in user is an admin
        Object loggedInUser = session.getAttribute("loggedInAdmin");
        if (loggedInUser == null) {
            // If not logged in or not an admin, redirect to the login page
            return "adminLogin";
        }

        try {
            // Find the existing owner by ID
            VehicleOwner existingOwner = ownerRepository.findById(ownerId)
                    .orElseThrow(() -> new Exception("Owner not found with ID: " + ownerId));

            // Update owner details
            existingOwner.setOwnerId(ownerId);
            existingOwner.setOwnerFullName(ownerFullName);
            existingOwner.setEmail(email);
            existingOwner.setOwnerCellNumber(ownerCellNumber);
            existingOwner.setOwnerAddress(ownerAddress);
            existingOwner.setPassword(password);

            // Save the updated owner
            ownerRepository.save(existingOwner);

            // Redirect to the admin dashboard or another relevant page
            return "ownerList";
        } catch (Exception exception) {
            // Handle exceptions and return an error page
            model.addAttribute("errorMessage", "Error updating owner: " + exception.getMessage());
            return "error"; // Thymeleaf error page
        }
    }

}
