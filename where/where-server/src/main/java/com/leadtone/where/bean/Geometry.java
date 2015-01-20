package com.leadtone.where.bean;

import java.util.List;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;


@Embedded
public class Geometry {
	 
	@Property("type")
	private String type;
	
	@Property("coordinates")
	private List<Number> coordinates ;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Number> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Number> coordinates) {
		this.coordinates = coordinates;
	}
	
	
}
