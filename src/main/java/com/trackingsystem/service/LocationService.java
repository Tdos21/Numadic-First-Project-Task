package com.trackingsystem.service;

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
    public VehicleLocation saveLocation(Long vehicleRegNum, Float currentLat, Float currentLong, String ownerIp) {
        Optional<VehicleReg> vehicleOptional = vehicleRegRepository.findById(vehicleRegNum);
        if (vehicleOptional.isEmpty()) {
            throw new RuntimeException("Vehicle with registration number " + vehicleRegNum + " not found.");
        }

        VehicleReg vehicleRegNum1 = vehicleOptional.get();

        VehicleLocation location = new VehicleLocation();
        location.setVehicleRegNum(vehicleRegNum1); // Assuming ManyToOne mapping
        location.setCurrentLat(currentLat);
        location.setCurrentLong(currentLong);
        location.setOwnerIp(ownerIp);

        return locationRepository.save(location);
    }



    public List<VehicleLocation> getAll() {
        return locationRepository.findAll();
    }

    public VehicleLocation getLatestLocation(VehicleReg vehicleRegNum) {
        return locationRepository.findTopByVehicleRegNum(vehicleRegNum);
    }
    
    @Transactional
    public void deleteLocationById(Long logId) {
        locationRepository.deleteById(logId);
    }

}
