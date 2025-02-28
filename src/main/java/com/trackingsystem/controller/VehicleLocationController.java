package com.trackingsystem.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleLocationRepository;
import com.trackingsystem.repository.VehicleRegRepository;
import com.trackingsystem.service.LocationService;

import jakarta.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(path="/tracking")
public class VehicleLocationController {
    
    @Autowired
    private VehicleLocationRepository locationRepository;
    
    @Autowired
    private VehicleRegRepository registrationRepository;
    
    @Autowired
    private LocationService locationService;

    @PostMapping("/save")
    public String saveLocation(@RequestParam(required = false) Long vehicleRegNum, HttpServletRequest request, Model model) {
        if (vehicleRegNum == null) {
            model.addAttribute("error", "Error: vehicleRegNum is required and cannot be null.");
            return "vehicleLocationList"; // Return to Thymeleaf template with error message
        }

        // Retrieve VehicleReg object using vehicleRegNum
        Optional<VehicleReg> vehicleRegOptional = registrationRepository.findById(vehicleRegNum);
        if (vehicleRegOptional.isEmpty()) {
            model.addAttribute("error", "Error: Vehicle with registration number " + vehicleRegNum + " not found.");
            return "vehicleLocationList"; // Return to locationList template with error message
        }

        VehicleReg vehicleRegNumEntity = vehicleRegOptional.get();

        String ownerIp = getClientIp(request);
        if (ownerIp == null || ownerIp.isEmpty()) {
            model.addAttribute("error", "Error: Could not determine client IP.");
            return "vehicleLocationList"; // Return to locationList template with error message
        }

        System.out.println("Owner IP: " + ownerIp);

        // Get geolocation based on IP
        GeolocationResult geolocationResult = getGeolocationFromIp(ownerIp);
        if (geolocationResult == null || geolocationResult.location == null) {
            model.addAttribute("error", "Error: Could not retrieve geolocation for IP: " + ownerIp);
            geolocationResult = new GeolocationResult(ownerIp, new Location(0.0f, 0.0f)); // Default location (0,0)
        } else {
            System.out.println("Geolocation retrieved: Latitude = " + geolocationResult.location.getCurrentLat() + ", Longitude = " + geolocationResult.location.getCurrentLong());
        }

        // Save the vehicle location using the geolocation (lat, long) and vehicleRegNum
        VehicleLocation savedLocation = locationService.saveLocation(vehicleRegNum, geolocationResult.location.getCurrentLat(), geolocationResult.location.getCurrentLong(), geolocationResult.ip);

        // Retrieve the list of all locations
        List<VehicleLocation> locationList = locationService.getAll();
        model.addAttribute("locationList", locationList);

        return "index"; // Return to the locationList Thymeleaf template
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // Fallback to remote address
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    public static GeolocationResult getGeolocationFromIp(String ip) {
        String localIp = "";
        String publicIp = ip;

        try {
            InetAddress localhost = InetAddress.getLocalHost();
            localIp = localhost.getHostAddress().trim();
            System.out.println("Local IP Address: " + localIp);

            if (publicIp == null || publicIp.isEmpty()) {
                URL url = new URL("http://bot.whatismyipaddress.com");
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                publicIp = reader.readLine().trim();
                System.out.println("Public IP Address: " + publicIp);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving IP address: " + e.getMessage());
            publicIp = "Unknown";
        }

        if ("0:0:0:0:0:0:0:1".equals(localIp) || "127.0.0.1".equals(localIp)) {
            return new GeolocationResult(localIp, new Location(0.0f, 0.0f));
        }

        return getGeolocation(publicIp);
    }

    private static GeolocationResult getGeolocation(String ip) {
        String apiKey = "CACF6543D6974102E28563A824AC6BFA"; // Replace with your API key
        String url = "https://api.ip2location.io?ip=" + ip + "&key=" + apiKey + "&format=json";

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Geolocation API Response: " + response);

            if (response == null || response.isEmpty()) {
                System.out.println("Error: No response from API");
                return new GeolocationResult(ip, null);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            JsonNode latitudeNode = jsonNode.get("latitude");
            JsonNode longitudeNode = jsonNode.get("longitude");

            if (latitudeNode != null && longitudeNode != null) {
                float latitude = latitudeNode.floatValue();
                float longitude = longitudeNode.floatValue();
                return new GeolocationResult(ip, new Location(latitude, longitude));
            } else {
                System.out.println("Error: Latitude or Longitude not found in response.");
                return new GeolocationResult(ip, null);
            }

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

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

   

    public static class Location {
        private float currentLong;
        private float currentLat;

        public Location(float currentLong, float currentLat) {
            this.currentLong = currentLong;
            this.currentLat = currentLat;
        }

        public float getCurrentLong() {
            return currentLong;
        }

        public void setCurrentLong(float currentLong) {
            this.currentLong = currentLong;
        }

        public float getCurrentLat() {
            return currentLat;
        }

        public void setCurrentLat(float currentLat) {
            this.currentLat = currentLat;
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestLocation(@RequestParam Long vehicleRegNum) {
        VehicleLocation latestLocation = locationService.getLatestLocation(vehicleRegNum);
        if (latestLocation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No location data found for vehicle: " + vehicleRegNum));
        }
        return ResponseEntity.ok(latestLocation);
    }

    @GetMapping("/getRegister")
    public String getLocationRegisterForm() {
        return "registerLocation";
    }

    @GetMapping("/all")
    public String getAllLocations(Model model) {
        List<VehicleLocation> locationList = locationService.getAll();
        System.out.println("Location List: " + locationList);
        model.addAttribute("locationList", locationList);
        return "vehicleLocationList";
    }
}
 