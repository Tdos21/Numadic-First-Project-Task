package com.trackingsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trackingsystem.models.VehicleLocation;
import com.trackingsystem.models.VehicleReg;

@Repository
public interface VehicleLocationRepository extends JpaRepository<VehicleLocation, Long> {

	//Optional<VehicleLocation> findById(VehicleReg vehicleRegNum);

	

}
