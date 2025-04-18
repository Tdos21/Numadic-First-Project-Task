package com.trackingsystem.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.trackingsystem.models.VehicleOwner;

public class OwnerDetails implements UserDetails {

    private final VehicleOwner owner;

    public OwnerDetails(VehicleOwner owner) {
        this.owner = owner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You can customize roles here, for now we give a basic role
        return List.of(new SimpleGrantedAuthority("ROLE_OWNER"));
    }

    @Override
    public String getPassword() {
        return owner.getPassword();
    }

    @Override
    public String getUsername() {
        return owner.getEmail(); // email is your username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public VehicleOwner getOwner() {
        return owner;
    }
}

