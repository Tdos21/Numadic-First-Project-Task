package com.trackingsystem.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trackingsystem.models.SystemAdmin;
import com.trackingsystem.models.VehicleOwner;
import com.trackingsystem.repository.VehicleOwnerRepository;
import com.trackingsystem.repository.VehicleRegRepository;
import com.trackingsystem.service.OwnerService;

import jakarta.servlet.http.HttpSession;
@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping(path="/api/registerOwner")
public class VehicleOwnerController {
	
	
    public VehicleOwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

	@Autowired
	protected VehicleOwnerRepository ownerRepository;
	
	
	@Autowired
	public OwnerService ownerService;
	

	
	@GetMapping("/getOwnerForm")
    public String showOwnerForm(@RequestParam(required = false) Long ownerId, HttpSession session, Model model) {
        
        VehicleOwner owner = (ownerId != null) ? ownerService.getOwnerById(ownerId) : new VehicleOwner();
        model.addAttribute("owner", owner);

        return "registerOwner"; // Renders the registration/edit form
    }
	
	

	
    @PostMapping("/addOwner")
    public String saveOwner(
            @ModelAttribute("owner") VehicleOwner owner,
            Model model
    ) {
      
        try {
            if (owner.getOwnerId() == null) {
                ownerService.createOwner(owner); // register
                model.addAttribute("success", "Owner registered successfully!");
            } else {
                ownerService.updateOwner(owner); // update
                model.addAttribute("success", "Owner updated successfully!");
            }
            return "redirect:/api/registerOwner/all";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("owner", owner);
            return "registerOwner"; // Rerender form with previous data
        }
    }

	
	@GetMapping("/all")
	public String getAllVehicles(Model model) {
	    List<VehicleOwner> owners = ownerService.getAllOwners();
	    model.addAttribute("vehicle_owner", owners);
	    return "vehicleOwnersList"; // This should match the Thymeleaf template filename
	}

	
	
    
    
    @Autowired
    public VehicleRegRepository vehicleRepo;
    
    
    

    
    @GetMapping("/delete/{ownerId}")
    public String deleteOwner(@PathVariable Long ownerId, HttpSession session, Model model) {
       

        ownerService.deleteOwnerById(ownerId);
        return "redirect:/api/registerOwner/all";
    }
    
    

}
