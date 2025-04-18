package com.trackingsystem.controller;


import java.net.InetAddress;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.trackingsystem.models.SystemAdmin;
import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleRegRepository;
import com.trackingsystem.service.LocationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@PreAuthorize("hasRole('ADMIN')")
@Controller
@RequestMapping(path="/tracking")
public class VehicleLocationController {
    
    @Autowired
    private VehicleRegRepository registrationRepository;
    
    @Autowired
    private LocationService locationService;
    
    
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping("/save")
    public String saveLocation(@RequestParam Long vehicleRegNum, HttpServletRequest request, Model model) {
        // Optionally check if it exists (for user-friendly error)
        if (registrationRepository.findById(vehicleRegNum).isEmpty()) {
            model.addAttribute("error", "Error: Vehicle with registration number " + vehicleRegNum + " not found.");
            return "vehicleLocationList";
        }

        String ownerIp = getClientIp(request);
        if (ownerIp == null || ownerIp.isEmpty()) {
            model.addAttribute("error", "Error: Could not determine client IP.");
            return "vehicleLocationList";
        }

        GeolocationResult geolocationResult = getGeolocationFromIp(ownerIp);
        if (geolocationResult == null || geolocationResult.location == null) {
            model.addAttribute("error", "Error: Could not retrieve geolocation for IP: " + ownerIp);
            return "vehicleLocationList";
        }

        VehicleLocation savedLocation = locationService.saveLocation(
            vehicleRegNum, // ✅ Just pass the Long
            geolocationResult.location.getCurrentLat(),
            geolocationResult.location.getCurrentLong(),
            geolocationResult.ip
        );

        List<VehicleLocation> locationList = locationService.getAll();
        model.addAttribute("locationList", locationList);

        return "index";
    }




   

    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
        };
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    public static GeolocationResult getGeolocationFromIp(String ip) {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String localIp = localhost.getHostAddress().trim();

            if ("0:0:0:0:0:0:0:1".equals(localIp) || "127.0.0.1".equals(localIp)) {
                return new GeolocationResult(localIp, new Location(0.0f, 0.0f));
            }

            return getGeolocation(ip);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeolocationResult(ip, null);
        }
    }

    private static GeolocationResult getGeolocation(String ip) {
        String apiKey = "CACF6543D6974102E28563A824AC6BFA";
        String url = "https://api.ip2location.io?ip=" + ip + "&key=" + apiKey + "&format=json";

        try {
            String response = new org.springframework.web.client.RestTemplate().getForObject(url, String.class);
            JsonNode jsonNode = new ObjectMapper().readTree(response);
            return new GeolocationResult(ip, new Location(jsonNode.get("latitude").floatValue(), jsonNode.get("longitude").floatValue()));
        } catch (Exception e) {
            e.printStackTrace();
            return new GeolocationResult(ip, null);
        }
    }

    public static class GeolocationResult {
        private String ip;
        private Location location;

        public GeolocationResult(String ip, Location location) {
            this.ip = ip;
            this.location = location;
        }

        public String getIp() { return ip; }
        public void setIp(String ip) { this.ip = ip; }
        public Location getLocation() { return location; }
        public void setLocation(Location location) { this.location = location; }
    }

    public static class Location {
        private float currentLong;
        private float currentLat;

        public Location(float currentLat, float currentLong) {
            this.currentLat = currentLat;
            this.currentLong = currentLong;
        }

        public float getCurrentLong() { return currentLong; }
        public float getCurrentLat() { return currentLat; }
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
