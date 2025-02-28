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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackingsystem.models.VehicleOwner;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleRegRepository;
import com.trackingsystem.service.RegistrationService;


@Controller
@RequestMapping(path="/api/vehicleReg")
public class VehicleRegController {
	

	 @Autowired
	    public VehicleRegController(RegistrationService registrationService) {
	        this.registrationService = registrationService;
	    }
	
	
	@Autowired
	public RegistrationService registrationService;
	
	@PostMapping(path = "/addVehicle")
	public String registerVehicle(
	        @RequestParam("vehicleRegNum") Long vehicleRegNum,
	        @RequestParam("vehicleName") String vehicleName,
	        @RequestParam("engineCapacity") String engineCapacity,
	        @RequestParam("vehicleState") String vehicleState,
	        @RequestParam("vehicleOwner") VehicleOwner vehicleOwner) {
	    try {
	        // Log to check input parameters
	        System.out.println("Received vehicle details:");
	        System.out.println("VehicleRegNum: " + vehicleRegNum);
	        System.out.println("VehicleName: " + vehicleName);
	        System.out.println("EngineCapacity: " + engineCapacity);
	        System.out.println("VehicleState: " + vehicleState);
	        System.out.println("VehicleState: " + vehicleOwner);
	        
	        
	        VehicleReg reg = registrationService.registerVehicle(vehicleRegNum, vehicleName, engineCapacity, vehicleState, vehicleOwner);
	        return "index";
	    } catch (Exception exception) {
	        return "error: " + exception.getMessage();
	    }
	}
	
	//getting all data microservice
	 @GetMapping("/all")
	    public String getVehicleList(Model model) {
	        List<VehicleReg> vehicles = registrationService.getAllVehicles();

	        // Debugging log
	        System.out.println("Fetched Vehicles: " + vehicles);
	        for (VehicleReg vehicle : vehicles) {
	            System.out.println("Vehicle: " + vehicle.getVehicleRegNum() + 
	                    ", Owner: " + (vehicle.getVehicleOwner() != null ? 
	                    vehicle.getVehicleOwner().getOwnerFullName() : "No Owner"));
	        }

	        model.addAttribute("vehicle_reg", vehicles);
	        return "vehicleRegList"; // Ensure this matches the Thymeleaf template filename
	    }

    
    @GetMapping("/vehicleRegForm")
    public String getRegForm() {
    	return "registerVehicle";
    }
    
    
    @Autowired
    public VehicleRegRepository vehicleRepo;
    
    
    
//    @Autowired
//    private VehicleRegService vehicleRegService;

    @GetMapping("/edit/{vehicleRegNum}")
    public String editVehicle(@PathVariable Long vehicleRegNum, Model model) {
        VehicleReg vehicle = registrationService.getVehicleById(vehicleRegNum);
        model.addAttribute("vehicle", vehicle);
        return "editVehicle"; // Make sure this is a valid Thymeleaf template
    }

    @GetMapping("/deleteOwner/{vehicleRegNum}")
    public String deleteVehicle(@PathVariable Long vehicleRegNum) {
        registrationService.deleteVehicle(vehicleRegNum);
        return "redirect:/api/vehicleReg/all"; // Redirect back to the list
    }

}
