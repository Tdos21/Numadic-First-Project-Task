package com.trackingsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class VehicleModel {

	@Id
	public int vehicleModelNum;
	
    public VehicleModel() {
		
	}


	public VehicleModel(Integer vehicleModelNum) {
		this.vehicleModelNum = vehicleModelNum;
	}


	public int getVehicleModelNum() {
		return vehicleModelNum;
	}


	public void setVehicleModelNum(int vehicleModelNum) {
		this.vehicleModelNum = vehicleModelNum;
	}


	@Override
	public String toString() {
		return "VehicleModel [vehicleModelNum=" + vehicleModelNum + "]";
	}
	
	

}
