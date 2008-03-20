package com.ixonos.alfresco.search.demo2;

import java.util.Date;

import com.ixonos.alfresco.search.annotation.Content;
import com.ixonos.alfresco.search.annotation.Property;

/**
 * A class that represents a car.
 * 
 * @author Marko Asplund
 */
@Content(qName="{http://www.ixonos.com/model/alfrescosearchdemo/1.0}car", useParentMapper=true)
public class Car extends MotorVehicle {
	@Property(qName="{http://www.ixonos.com/model/alfrescosearchdemo/1.0}carDoors")
	private Integer doors;
	
	@Property(qName="{http://www.alfresco.org/model/content/1.0}modified")
	private Date modified;

	public Integer getDoors() {
		return doors;
	}

	public void setDoors(Integer doors) {
		this.doors = doors;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}
	
}
