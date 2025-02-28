package com.trackingsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.trackingsystem.models.VehicleModel;
import com.trackingsystem.repository.VehicleModelRepository;
import com.trackingsystem.service.ModelService;

@Controller
public class VehicleModelController {
	
	@Autowired
    public VehicleModelController(ModelService modelService) {
        this.modelService = modelService;
    }
	
	
	@Autowired
	public ModelService modelService;
	
	@Autowired
	public VehicleModelRepository modelRepo;

	@PostMapping(path="/addModel")
	public String addModel(
			@RequestParam("vehicleModelNum") Integer vehicleModelNum,
			Model model
			) {
		
		try {
			VehicleModel model1 = modelService.createModel(vehicleModelNum);
			 // Optionally store owner in session
            return "Okay, Created";
		}catch (Exception exception) {
            model.addAttribute("errorMessage", "User not saved: " + exception.getMessage());
            return "server error 500"; 
        }
	}
	
	@GetMapping("/all")
    public ResponseEntity<?> getAllModels() {
        List<VehicleModel> models = modelService.getAllModels();
        return ResponseEntity.ok(models);
    }
    
    
    @DeleteMapping("/delete/{brandId}")
    public ResponseEntity<?> deleteBrand(@PathVariable int brandId) {
        try {
            // Check if the pet exists in the repository
            if (modelRepo.existsById(brandId)) {
                // Delete the pet directly using the repository
                modelRepo.deleteById(brandId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                // Pet not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Pet not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions that occur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while deleting the pet");
        }
    }


}
