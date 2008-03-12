package com.ixonos.alfresco.search.demo;

import javax.xml.namespace.QName;


/**
 * Alfresco search demo related constants.
 * 
 * @author Marko Asplund
 */
public class MotorVehicleConstants {

	public static final String NAMESPACE_VEHICLE =
		"http://www.ixonos.com/model/alfrescosearchdemo/1.0";
	public static final String NAMESPACE_VEHICLE_PREFIX = "asd";

	public static final QName TYPE_CAR = new QName(NAMESPACE_VEHICLE, "car",
			NAMESPACE_VEHICLE_PREFIX);
	public static final QName TYPE_VEHICLE = new QName(NAMESPACE_VEHICLE, "motorVehicle",
			NAMESPACE_VEHICLE_PREFIX);

	public static final QName PROP_MANUFACTURER = new QName(NAMESPACE_VEHICLE,
			"vehicleManufacturer", NAMESPACE_VEHICLE_PREFIX);
	public static final QName PROP_MODEL = new QName(NAMESPACE_VEHICLE,
			"vehicleModel", NAMESPACE_VEHICLE_PREFIX);
	public static final QName PROP_REGISTRATION = new QName(NAMESPACE_VEHICLE,
			"vehicleRegistrationNumber", NAMESPACE_VEHICLE_PREFIX);
}
