package com.example.pji.mapspji.database.localisation;

public class Position {
	private double longitude;
	private double latitude;
	public Position(double longitude,double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public String toString() {
		return "Position [longitude=" + longitude + ", latitude=" + latitude + "]";
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
}
