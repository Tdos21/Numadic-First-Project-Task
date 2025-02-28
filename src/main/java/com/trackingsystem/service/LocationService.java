package com.trackingsystem.service;

import java.util.List;
import java.util.Optional;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;
import com.trackingsystem.repository.VehicleLocationRepository;
import com.trackingsystem.repository.VehicleRegRepository;

@Service
public class LocationService {

    @Autowired
    public VehicleLocationRepository locationRepo;

    @Autowired
    private VehicleLocationRepository locationRepository;

    @Autowired
    private VehicleRegRepository vehicleRegRepository;

    public VehicleLocation saveLocation(Long vehicleRegNum, Float currentLat, Float currentLong, String ownerIp) {
        Optional<VehicleReg> vehicleOptional = vehicleRegRepository.findByVehicleRegNum(vehicleRegNum);

        if (!vehicleOptional.isPresent()) {
            throw new RuntimeException("Vehicle with registration number " + vehicleRegNum + " not found.");
        }

        VehicleReg vehicleRegNum1 = vehicleOptional.get();

        VehicleLocation location = new VehicleLocation();
        location.setCurrentLat(currentLat);
        location.setCurrentLong(currentLong);
        
        // Using java.sql.Timestamp
        location.setBeforeTime(new Timestamp(System.currentTimeMillis()));  // Correct timestamp usage
        location.setCurrentTime(new Timestamp(System.currentTimeMillis())); // Correct timestamp usage
        
        location.setVehicleRegNum(vehicleRegNum1);
        location.setOwnerIp(ownerIp);

        return locationRepository.save(location);
    }

    
    
    public VehicleLocation getLatestLocation(Long vehicleRegNum) {
        return locationRepository.findTopByVehicleRegNum_VehicleRegNumOrderByCurrentTimeDesc(vehicleRegNum);
    }



	public List<VehicleLocation> getAll() {
		
		return (List<VehicleLocation>) locationRepository.findAll();
	}
	
	public Optional<VehicleLocation> findByVehicleRegNum(Long vehicleRegNum) {
	    // Retrieve the VehicleReg entity using the vehicleRegNum
	    Optional<VehicleReg> vehicleOptional = vehicleRegRepository.findById(vehicleRegNum);

	    // If the vehicle with the given registration number is not found, throw an exception
	    if (!vehicleOptional.isPresent()) {
	        throw new RuntimeException("Vehicle with registration number " + vehicleRegNum + " not found.");
	    }

	    // Retrieve the VehicleReg entity
	    VehicleReg vehicleReg = vehicleOptional.get();

	    // Now, use the VehicleReg entity to find the corresponding VehicleLocation
	    return locationRepository.findByVehicleRegNum(vehicleReg);
	}


}
