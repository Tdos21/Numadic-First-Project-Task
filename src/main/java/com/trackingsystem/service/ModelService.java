package com.trackingsystem.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trackingsystem.models.VehicleModel;
import com.trackingsystem.repository.VehicleModelRepository;
import jakarta.transaction.Transactional;

@Service
public class ModelService {

	@Autowired
	public VehicleModelRepository modelRepo;
	
	@Transactional
	public VehicleModel createModel(Integer vehicleModelNum
            ) throws Exception {
		VehicleModel reg = new VehicleModel( vehicleModelNum);
    return modelRepo.save(reg); // Save owner and return the saved instance
	}
	
    
    @Transactional
    public List<VehicleModel> getAllModels() {
        return (List<VehicleModel>) modelRepo.findAll();
    }

    // Method to delete a pet by ID
	@Transactional
    public boolean deleteModel(Integer vehicleModelNum) {
        Optional<VehicleModel> pet = modelRepo.findById(vehicleModelNum);
        if (pet.isPresent()) {
        	modelRepo.delete(pet.get());
            return true;
        }
        return false;
    }

}
