package com.trackingsystem.service;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trackingsystem.models.VehicleType;
import com.trackingsystem.repository.VehicleTypeRepository;

@Service
public class TypeService {
	
	@Autowired
	public VehicleTypeRepository typeRepo;
	
	
	public VehicleType createType(String vehicleType, String vehicleBrand, Integer vehicleModel
            ) throws Exception {
		VehicleType reg = new VehicleType(null, vehicleType, vehicleBrand, vehicleModel);
    return typeRepo.save(reg); // Save owner and return the saved instance
}
	
	
	public VehicleType editType(Integer typeId, VehicleType typeDetails ) throws Exception {
        Optional<VehicleType> petOpt = typeRepo.findById(typeId);
        if (petOpt.isEmpty()) {
            throw new Exception("Pet not found with id " + typeId);
        }

        VehicleType reg = petOpt.get();
        reg.setVehicleType(typeDetails.getVehicleType());
        reg.setBrand(typeDetails.getBrand());
        reg.setVehicleModel(typeDetails.getVehicleModel());
        reg.setTypeId(typeDetails.getTypeId());
        
        return typeRepo.save(reg);
    }


    // Method to get all pets
    public List<VehicleType> getAllTypes() {
        return (List<VehicleType>) typeRepo.findAll();
    }

    // Method to delete a pet by ID
    public boolean deleteLocation(Integer typeId) {
        Optional<VehicleType> pet = typeRepo.findById(typeId);
        if (pet.isPresent()) {
            typeRepo.delete(pet.get());
            return true;
        }
        return false;
    }

    
    public VehicleType getVehicleById(Integer typeId) {
        // Assuming the PetRepository has a findById method
        return typeRepo.findById(typeId).orElse(null);
    }




}
