package com.trackingsystem.models;

import java.sql.Timestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class VehicleLocation {

	    
		@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long logId;

	    @Column
	    private Float currentLong;

	    @Column
	    private Float currentLat;
	    
	    
	    @Column(name = "`current_time`") 
	    private Timestamp currentTime;
	    
	    @Column(name = "`before_time`") 
	    private Timestamp beforeTime;
	    @Column
	    private String ownerIp;  // Capture owner's IP address

	    @ManyToOne
	    @JoinColumn(name = "vehicleRegNum")
	    private VehicleReg vehicleRegNum;

	    // Constructors
	    public VehicleLocation() {}

	    
	    public VehicleLocation(Long logId, Float currentLong, Float currentLat, Timestamp beforeTime, Timestamp currentTime,
				String ownerIp, VehicleReg vehicleRegNum) {
			super();
			this.logId = logId;
			this.currentLong = currentLong;
			this.currentLat = currentLat;
			this.beforeTime = beforeTime;
			this.currentTime = currentTime;
			this.ownerIp = ownerIp;
			this.vehicleRegNum = vehicleRegNum;
		}


		public Long getLogId() {
			return logId;
		}


		public void setLogId(Long logId) {
			this.logId = logId;
		}


		public Float getCurrentLong() {
			return currentLong;
		}


		public void setCurrentLong(Float currentLong) {
			this.currentLong = currentLong;
		}


		public Float getCurrentLat() {
			return currentLat;
		}


		public void setCurrentLat(Float currentLat) {
			this.currentLat = currentLat;
		}


		public Timestamp getBeforeTime() {
			return beforeTime;
		}


		public void setBeforeTime(Timestamp beforeTime) {
			this.beforeTime = beforeTime;
		}


		public Timestamp getCurrentTime() {
			return currentTime;
		}


		public void setCurrentTime(Timestamp currentTime) {
			this.currentTime = currentTime;
		}


		public String getOwnerIp() {
			return ownerIp;
		}


		public void setOwnerIp(String ownerIp) {
			this.ownerIp = ownerIp;
		}


		public VehicleReg getVehicleRegNum() {
			return vehicleRegNum;
		}


		public void setVehicleRegNum(VehicleReg vehicleRegNum) {
			this.vehicleRegNum = vehicleRegNum;
		}
	    
	    

}	   