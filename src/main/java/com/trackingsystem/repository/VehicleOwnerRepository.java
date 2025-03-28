package com.trackingsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trackingsystem.models.VehicleOwner;

@Repository
public interface VehicleOwnerRepository extends JpaRepository<VehicleOwner, Long> {

	VehicleOwner findByEmail(String email);
}
