package com.trackingsystem.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
public class VehicleReg {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vehicleRegNum;

    @Column
    private String vehicleName;

    @Column
    private String engineCapacity;

    @Column
    private String vehicleState;

    @OneToMany(mappedBy = "vehicleReg", cascade = CascadeType.ALL)
    private List<VehicleLocation> locations;
	
    
    public VehicleReg() {
		
	}
	
   public VehicleReg(Long vehicleRegNum, String vehicleName, String engineCapacity, String vehicleState) {
		
		this.vehicleRegNum = vehicleRegNum;
		this.vehicleName = vehicleName;
		this.engineCapacity = engineCapacity;
		this.vehicleState = vehicleState;
	}


   public Long getVehicleRegNum() {
       return vehicleRegNum;
   }
   
	public void setVehicleRegNum(Long vehicleRegNum) {
		vehicleRegNum = vehicleRegNum;
	}


	public String getVehicleName() {
		return vehicleName;
	}


	public void setVehicleName(String vehicleName) {
		vehicleName = vehicleName;
	}


	public String getEngineCapacity() {
		return engineCapacity;
	}


	public void setEngineCapacity(String engineCapacity) {
		this.engineCapacity = engineCapacity;
	}


	public String getVehicleState() {
		return vehicleState;
	}


	public void setVehicleState(String vehicleState) {
		this.vehicleState = vehicleState;
	}


	@Override
	public String toString() {
		return "VehicleReg [VehicleRegNum=" + vehicleRegNum + ", VehicleName=" + vehicleName + ", engineCapacity="
				+ engineCapacity + ", vehicleState=" + vehicleState + "]";
	} 
	
	
	
	

	
}
