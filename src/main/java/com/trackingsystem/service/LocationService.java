package com.trackingsystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleLocationRepository;
import com.trackingsystem.repository.VehicleRegRepository;

@Service
public class LocationService {

    @Autowired
    private VehicleLocationRepository locationRepository;

    @Autowired
    private VehicleRegRepository vehicleRegRepository;

    public VehicleLocation saveLocation(VehicleReg vehicleRegNum, Float currentLat, Float currentLong, String ownerIp) {
        Optional<VehicleReg> vehicleOptional = vehicleRegRepository.findByVehicleRegNum(vehicleRegNum);
        if (vehicleOptional.isEmpty()) {
            throw new RuntimeException("Vehicle with registration number " + vehicleRegNum + " not found.");
        }

        VehicleLocation location = new VehicleLocation();
        location.setVehicleRegNum(vehicleRegNum);
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
}
