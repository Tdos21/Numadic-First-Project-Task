package com.trackingsystem.controller;




import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.trackingsystem.dto.LocationDTO;
import com.trackingsystem.dto.OwnTracksPayload;
import com.trackingsystem.models.SystemAdmin;
import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleRegRepository;
import com.trackingsystem.service.LocationService;


import jakarta.servlet.http.HttpSession;



@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping(path="/tracking")
public class VehicleLocationController {
    
    @Autowired
    private VehicleRegRepository registrationRepository;
    
    @Autowired
    private LocationService locationService;
    
    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<String> updateLocationFromOwnTracks(@ModelAttribute LocationDTO dto) {
        Long vehicleRegNum = dto.getVehicleRegNum();
        Float currentLat = dto.getCurrentLat();
        Float currentLong = dto.getCurrentLong();

        if (vehicleRegNum == null || currentLat == null || currentLong == null) {
            return ResponseEntity.badRequest().body("Error: Missing data from OwnTracks.");
        }

        if (registrationRepository.findByVehicleRegNum(vehicleRegNum).isEmpty()) {
            return ResponseEntity.badRequest().body("Vehicle not found.");
        }

        locationService.saveLocation(vehicleRegNum, currentLat, currentLong, "From OwnTracks");

        return ResponseEntity.ok("Location updated via OwnTracks.");
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestLocation(@RequestParam Long vehicleRegNum) {
        Optional<VehicleReg> vehicleRegOptional = registrationRepository.findById(vehicleRegNum);
        
        if (vehicleRegOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Vehicle with ID " + vehicleRegNum + " not found.");
        }

        VehicleReg vehicleReg = vehicleRegOptional.get();
        VehicleLocation latestLocation = locationService.getLatestLocation(vehicleReg);
        
        if (latestLocation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No location data found for vehicle: " + vehicleReg.getVehicleRegNum());
        }

        return ResponseEntity.ok(latestLocation);
    }

    @GetMapping("/all")
    public String getAllLocations(Model model) {
        List<VehicleLocation> locationList = locationService.getAll();
        model.addAttribute("locationList", locationList);
        return "vehicleLocationList";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @GetMapping("/locations")
    public String showLocations(Model model) {
        List<VehicleLocation> locations = locationService.getAll();
        model.addAttribute("locationList", locations);
        return "vehicleTrackList";  // This should match the HTML file name
    }
    
    @GetMapping("/locations/delete/{logId}")
    public String deleteLocation(@PathVariable Long logId, HttpSession session, Model model) {
        SystemAdmin admin = (SystemAdmin) session.getAttribute("loggedInAdmin");

        if (admin == null) {
            model.addAttribute("error", "You must be logged in as admin.");
            return "adminLogin";
        }

        locationService.deleteLocationById(logId);
        return "redirect:/tracking/locations";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @GetMapping("/getRegister")
	public String showVehicleRegisterPage() {
	    
	    return "registerLocation"; // This will look for templates/homeIndex.html
	}
    
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @GetMapping("/getTrackingPage")
	public String showLoginPage() {
	    
	    return "vehicleTracking"; // This will look for templates/homeIndex.html
	}


}
