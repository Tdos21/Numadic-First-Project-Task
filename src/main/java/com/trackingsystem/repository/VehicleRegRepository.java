package com.trackingsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trackingsystem.models.VehicleReg;

@Repository
public interface VehicleRegRepository extends JpaRepository<VehicleReg, Long> {
    

	Optional<VehicleReg> findByVehicleRegNum(Long vehicleRegNum);
}

