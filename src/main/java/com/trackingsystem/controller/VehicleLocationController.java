package com.trackingsystem.controller;


import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleRegRepository;
import com.trackingsystem.service.LocationService;

import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(path="/tracking")
public class VehicleLocationController {
    
    @Autowired
    private VehicleRegRepository registrationRepository;
    
    @Autowired
    private LocationService locationService;

    @PostMapping("/save")
    public String saveLocation(@RequestParam VehicleReg vehicleRegNum, HttpServletRequest request, Model model) {
        Optional<VehicleReg> vehicleRegOptional = registrationRepository.findByVehicleRegNum(vehicleRegNum);
        if (vehicleRegOptional.isEmpty()) {
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

        VehicleLocation savedLocation = locationService.saveLocation(vehicleRegNum, 
            geolocationResult.location.getCurrentLat(), 
            geolocationResult.location.getCurrentLong(), 
            geolocationResult.ip);

        List<VehicleLocation> locationList = locationService.getAll();
        model.addAttribute("locationList", locationList);

        return "index";
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestLocation(@RequestParam VehicleReg vehicleRegNum) {
        VehicleLocation latestLocation = locationService.getLatestLocation(vehicleRegNum);
        if (latestLocation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No location data found for vehicle: " + vehicleRegNum);
        }
        return ResponseEntity.ok(latestLocation);
    }

    @GetMapping("/all")
    public String getAllLocations(Model model) {
        List<VehicleLocation> locationList = locationService.getAll();
        model.addAttribute("locationList", locationList);
        return "vehicleLocationList";
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
    
    
    @GetMapping("/locations")
    public String showLocations(Model model) {
        List<VehicleLocation> locations = locationService.getAll();
        model.addAttribute("locationList", locations);
        return "vehicleTrackList";  // This should match the HTML file name
    }

}
