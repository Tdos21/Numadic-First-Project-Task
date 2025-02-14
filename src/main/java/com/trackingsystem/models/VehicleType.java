package com.trackingsystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
public class VehicleType {
	

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int typeId;
	
	@Column
	public String vehicleType;
	
	
	@Column
	public String vehicleBrand;
	
	@Column
	public int vehicleModel;
	
    public VehicleType() {
		
	}
	
    public VehicleType(Integer typeId, String vehicleType, String vehicleBrand, int vehicleModel) {
		
		this.typeId = typeId;
		this.vehicleType = vehicleType;
		this.vehicleBrand = vehicleBrand;
		this.vehicleModel = vehicleModel;
	}

	
	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		vehicleType = vehicleType;
	}

	public String getBrand() {
		return vehicleBrand;
	}

	public void setBrand(String brand) {
		vehicleBrand = brand;
	}

	public int getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(int vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	@Override
	public String toString() {
		return "VehicleType [typeId=" + typeId + ", VehicleType=" + vehicleType + ", Brand=" +vehicleBrand + ", vehicleModel="
				+ vehicleModel + "]";
	}
}
