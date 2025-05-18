package com.trackingsystem.dto;

public class LocationDTO {
    private Long vehicleRegNum;  // Keeping Long for vehicle registration number
    private Float currentLat;   // Changed to Double for more precision
    private Float currentLong;  // Changed to Double for more precision

    // Constructors (optional)
    public LocationDTO() {}

    public LocationDTO(Long vehicleRegNum, Float currentLat, Float currentLong) {
        this.vehicleRegNum = vehicleRegNum;
        this.currentLat = currentLat;
        this.currentLong = currentLong;
    }

    // Getters and Setters
    public Long getVehicleRegNum() {
        return vehicleRegNum;
    }

    public void setVehicleRegNum(Long vehicleRegNum) {
        this.vehicleRegNum = vehicleRegNum;
    }

    public Float getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(Float currentLat) {
        // Optional: Add validation for latitude
        if (currentLat >= -90.0 && currentLat <= 90.0) {
            this.currentLat = currentLat;
        } else {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees.");
        }
    }

    public Float getCurrentLong() {
        return currentLong;
    }

    public void setCurrentLong(Float currentLong) {
        // Optional: Add validation for longitude
        if (currentLong >= -180.0 && currentLong <= 180.0) {
            this.currentLong = currentLong;
        } else {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees.");
        }
    }
}
