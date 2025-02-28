package com.trackingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trackingsystem.models.SystemAdmin;

public interface SystemAdminRepository extends JpaRepository<SystemAdmin, Integer> {

	SystemAdmin findByUsername(String username);

}

