package com.ixonos.alfresco.search.demo2;

import com.ixonos.alfresco.search.annotation.Content;
import com.ixonos.alfresco.search.annotation.Property;
import com.ixonos.alfresco.search.cm.CMContent;

/**
 * A class that represents a motor vehicle.
 * 
 * @author Marko Asplund
 */
@Content(qName="{http://www.ixonos.com/model/alfrescosearchdemo/1.0}motorVehicle", useParentMapper=true)
public class MotorVehicle extends CMContent {
	@Property(qName="{http://www.ixonos.com/model/alfrescosearchdemo/1.0}vehicleManufacturer")
	private String manufacturer;
	
	@Property(qName="{http://www.ixonos.com/model/alfrescosearchdemo/1.0}vehicleModel")
	private String model;
	
	@Property(qName="{http://www.ixonos.com/model/alfrescosearchdemo/1.0}vehicleRegistrationNumber")
	private String registration;


	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

}
