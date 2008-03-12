package com.ixonos.alfresco.search.demo;

import com.ixonos.alfresco.search.cm.CMContent;

/**
 * A class that represents a motor vehicle.
 * 
 * @author Marko Asplund
 */
public class MotorVehicle extends CMContent {
	private String manufacturer;
	private String model;
	private String registration;

	public String getManufacturer() {
		return manufacturer;
	}
	public String getModel() {
		return model;
	}
	public String getRegistration() {
		return registration;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}

}
