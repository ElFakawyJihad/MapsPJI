package com.example.pji.mapspji.api.dom.src.user;

public class Position {
	private String longitude;
	private String latitude;

	public Position(String longitude,String latitude){
		this.longitude=longitude;
		this.latitude=latitude;
	}

	@Override
	public String toString() {
		return "Position [longitude=" + longitude + ", latitude=" + latitude + "]";
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
