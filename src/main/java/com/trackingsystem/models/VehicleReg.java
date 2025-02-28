package com.trackingsystem.models;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;

@Entity
public class VehicleReg {

	@Id
    private Long vehicleRegNum;
	
	@Version
    private int version;

    @Column
    private String vehicleName;

    @Column
    private String engineCapacity;

    @Column
    private String vehicleState;

    @OneToMany(mappedBy = "vehicleRegNum", cascade = CascadeType.ALL)
    private List<VehicleLocation> locations;
    
    @ManyToOne
    @JoinColumn(name = "ownerId")
    private VehicleOwner vehicleOwner;
	
    
    
    public VehicleReg() {
    	
    }
	
    public VehicleReg(Long vehicleRegNum, int version, String vehicleName, String engineCapacity, String vehicleState,
			VehicleOwner vehicleOwner) {
		this.vehicleRegNum = vehicleRegNum;
		this.version = version;
		this.vehicleName = vehicleName;
		this.engineCapacity = engineCapacity;
		this.vehicleState = vehicleState;
		this.vehicleOwner = vehicleOwner;
	}

	@Override
	public String toString() {
		return "VehicleReg [vehicleRegNum=" + vehicleRegNum + ", version=" + version + ", vehicleName=" + vehicleName
				+ ", engineCapacity=" + engineCapacity + ", vehicleState=" + vehicleState + ", locations=" + locations
				+ ", vehicleOwner=" + vehicleOwner + "]";
	}

	public Long getVehicleRegNum() {
		return vehicleRegNum;
	}

	public void setVehicleRegNum(Long vehicleRegNum) {
		this.vehicleRegNum = vehicleRegNum;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
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

	public VehicleOwner getVehicleOwner() {
		return vehicleOwner;
	}

	public void setVehicleOwner(VehicleOwner vehicleOwner) {
		this.vehicleOwner = vehicleOwner;
	}
	

	
}
