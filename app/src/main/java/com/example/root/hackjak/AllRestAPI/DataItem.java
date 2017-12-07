package com.example.root.hackjak.AllRestAPI;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("coordinates")
	private List<CoordinatesItem> coordinates;

	@SerializedName("properties")
	private Properties properties;

	public void setCoordinates(List<CoordinatesItem> coordinates){
		this.coordinates = coordinates;
	}

	public List<CoordinatesItem> getCoordinates(){
		return coordinates;
	}

	public void setProperties(Properties properties){
		this.properties = properties;
	}

	public Properties getProperties(){
		return properties;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"coordinates = '" + coordinates + '\'' + 
			",properties = '" + properties + '\'' + 
			"}";
		}
}