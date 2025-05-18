package com.trackingsystem.dto;

import lombok.Data;

@Data
public class OwnTracksPayload {
    private String _type;
    private float lat;
    private float lon;
    private long tst;
    private int acc;
    private int batt;
    
    //get and set
	public String get_type() {
		return _type;
	}
	public void set_type(String _type) {
		this._type = _type;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
	public long getTst() {
		return tst;
	}
	public void setTst(long tst) {
		this.tst = tst;
	}
	public int getAcc() {
		return acc;
	}
	public void setAcc(int acc) {
		this.acc = acc;
	}
	public int getBatt() {
		return batt;
	}
	public void setBatt(int batt) {
		this.batt = batt;
	}
}
