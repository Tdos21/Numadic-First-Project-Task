package com.trackingsystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleLocationRepository;
import com.trackingsystem.repository.VehicleRegRepository;

import jakarta.transaction.Transactional;

@Service
public class LocationService {

    @Autowired
    private VehicleLocationRepository locationRepository;

    @Autowired
    private VehicleRegRepository vehicleRegRepository;

    
    @Transactional
    public VehicleLocation saveLocation(Long vehicleRegNum, Float currentLat, Float currentLong, String ownerNote) {
        Optional<VehicleReg> vehicleOptional = vehicleRegRepository.findByVehicleRegNum(vehicleRegNum);
        if (vehicleOptional.isEmpty()) {
            throw new RuntimeException("Vehicle with registration number " + vehicleRegNum + " not found.");
        }

        VehicleReg vehicle = vehicleOptional.get();

        VehicleLocation location = new VehicleLocation();
        location.setVehicleRegNum(vehicle); // assuming ManyToOne mapping
        location.setCurrentLat(currentLat);
        location.setCurrentLong(currentLong);
        location.setOwnerIp(ownerNote); // In this case, "OwnTracks Device" or any note
        location.setCurrentTime(LocalDateTime.now()); // Optional: log timestamp

        return locationRepository.save(location);
    }



    @Transactional
    public List<VehicleLocation> getAll() {
        return locationRepository.findAll();
    }

    @Transactional
    public VehicleLocation getLatestLocation(VehicleReg vehicleRegNum) {
        return locationRepository.findTopByVehicleRegNum(vehicleRegNum);
    }
    
    @Transactional
    public void deleteLocationById(Long logId) {
        locationRepository.deleteById(logId);
    }

}
