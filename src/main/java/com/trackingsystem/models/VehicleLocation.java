package com.trackingsystem.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "vehicle_location")
public class VehicleLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long logId;

    @Column
    private String currentLong;

    @Column
    private String currentLat;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "currentTime")
    private Date currentTime;

    @ManyToOne
    @JoinColumn(name = "vehicleRegNum")
    private VehicleReg vehicleReg;

    // Default constructor
    public VehicleLocation() {
    }

    // Constructor with parameters, setting the currentTime to the current timestamp
    public VehicleLocation(Long logId, String currentLong, String currentLat, VehicleReg vehicleRegNum) {
        this.logId = logId;
        this.currentLong = currentLong;
        this.currentLat = currentLat;
        this.currentTime = new Date();  // Automatically set to current timestamp
        this.vehicleReg = vehicleRegNum;
    }

    // Getter and setter methods
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getCurrentLong() {
        return currentLong;
    }

    public void setCurrentLong(String currentLong) {
        this.currentLong = currentLong;
    }

    public String getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(String currentLat) {
        this.currentLat = currentLat;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public VehicleReg getVehicleReg() {
        return vehicleReg;
    }

    public void setVehicleReg(VehicleReg vehicleReg) {
        this.vehicleReg = vehicleReg;
    }

    @Override
    public String toString() {
        return "VehicleLocation [logId=" + logId + ", currentLong=" + currentLong + ", currentLat=" + currentLat
                + ", currentTime=" + currentTime + ", vehicleReg=" + vehicleReg + "]";
    }
}
