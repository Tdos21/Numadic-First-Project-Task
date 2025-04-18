package com.trackingsystem.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trackingsystem.models.VehicleOwner;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleRegRepository;
import com.trackingsystem.service.RegistrationService;

import jakarta.servlet.http.HttpSession;

@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping(path="/api/vehicleReg")
public class VehicleRegController {
	

	 @Autowired
	    public VehicleRegController(RegistrationService registrationService) {
	        this.registrationService = registrationService;
	    }
	
	
	@Autowired
	public RegistrationService registrationService;
	
	 @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
	@PostMapping(path = "/addVehicle")
	public String registerVehicle(
	        @RequestParam("vehicleRegNum") Long vehicleRegNum,
	        @RequestParam("vehicleName") String vehicleName,
	        @RequestParam("engineCapacity") String engineCapacity,
	        @RequestParam("vehicleState") String vehicleState,
	        RedirectAttributes redirectAttributes,
	        Principal principal
	) {
	    try {
	        // 1. Get the email of the logged-in owner
	        String email = principal.getName(); // returns email if that's how login works

	        // 2. Use the email to fetch the VehicleOwner from the database
	        VehicleOwner vehicleOwner = registrationService.getVehicleOwnerByEmail(email); 

	        // Logging for debugging
	        System.out.println("Email: " + email);
	        System.out.println("VehicleOwner: " + vehicleOwner.getOwnerFullName());

	        // 3. Register the vehicle
	        VehicleReg reg = registrationService.registerVehicle(
	                vehicleRegNum, vehicleName, engineCapacity, vehicleState, vehicleOwner
	        );

	        redirectAttributes.addFlashAttribute("successMessage", "Vehicle registered successfully!");
	        return "index";

	    } catch (Exception exception) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Error: " + exception.getMessage());
	        return "redirect:/api/vehicleReg/all";
	    }
	}
	 
	 
	 
	 
	 @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
	    @PostMapping("/editVehicle")
	    public String editVehicle(
	            @ModelAttribute("vehicle") VehicleReg vehicle, 
	            RedirectAttributes redirectAttributes,
	            Model model
	    ) {
	        try {
	            registrationService.updateVehicle(vehicle);
	            redirectAttributes.addFlashAttribute("successMessage", "Vehicle updated successfully!");
	            return "redirect:/api/vehicleReg/all";
	        } catch (Exception e) {
	            model.addAttribute("errorMessage", e.getMessage());
	            model.addAttribute("vehicle", vehicle);
	            return "editVehicle";   // re-render the same form
	        }
	    }


	
	//getting all data microservice
	
	 @GetMapping("/all")
	 public String getVehicleList(Model model) {
	     List<VehicleReg> vehicleList = registrationService.getAllVehicles();
	     model.addAttribute("vehicleList", vehicleList);
	     return "vehicleRegList";
	 }


	 @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
	@GetMapping("/vehicleRegForm")
	public String getRegForm(Principal principal) {
	    System.out.println("Logged in as: " + principal.getName());
	    return "registerVehicle";
	}


	
	@GetMapping("/edit/{vehicleRegNum}")
	public String editVehicle(@PathVariable Long vehicleRegNum, Model model, HttpSession session) {
	    
	    VehicleReg vehicle = registrationService.getVehicleById(vehicleRegNum);
	    model.addAttribute("vehicle", vehicle);
	    return "editVehicle";
	}

	
	@GetMapping("/deleteOwner/{vehicleRegNum}")
	public String deleteVehicle(@PathVariable Long vehicleRegNum, HttpSession session) {
	   
	    registrationService.deleteVehicle(vehicleRegNum);
	    return "redirect:/api/vehicleReg/all";
	}
}
