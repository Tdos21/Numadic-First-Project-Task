package com.trackingsystem.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
public class VehicleOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ownerId;

    @Column(nullable = false)
    private String ownerFullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String ownerAddress;

    @Column(nullable = false, unique = true)
    private String ownerCellNumber;

    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "vehicleOwner", cascade = CascadeType.ALL)
    private List<VehicleReg> vehicles;

    // Default No-Args Constructor (Required by JPA)
    public VehicleOwner() {}

    // All-Args Constructor
    public VehicleOwner(Long ownerId, String ownerFullName, String email, String ownerAddress,
                        String ownerCellNumber, String password) {
        this.ownerId = ownerId;
        this.ownerFullName = ownerFullName;
        this.email = email;
        this.ownerAddress = ownerAddress;
        this.ownerCellNumber = ownerCellNumber;
        this.password = password;
    }

    // Getters and Setters
    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getOwnerCellNumber() {
        return ownerCellNumber;
    }

    public void setOwnerCellNumber(String ownerCellNumber) {
        this.ownerCellNumber = ownerCellNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}