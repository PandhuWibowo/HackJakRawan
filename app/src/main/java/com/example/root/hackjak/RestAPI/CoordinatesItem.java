package com.example.root.hackjak.RestAPI;

import com.google.gson.annotations.SerializedName;

public class CoordinatesItem{

	@SerializedName("long")
	private double jsonMemberLong;

	@SerializedName("lat")
	private double lat;

	public void setJsonMemberLong(double jsonMemberLong){
		this.jsonMemberLong = jsonMemberLong;
	}

	public double getJsonMemberLong(){
		return jsonMemberLong;
	}

	public void setLat(double lat){
		this.lat = lat;
	}

	public double getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"CoordinatesItem{" + 
			"long = '" + jsonMemberLong + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}
}