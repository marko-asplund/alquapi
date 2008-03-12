package com.ixonos.alfresco.search.demo;

import java.util.Map;

import org.alfresco.webservice.util.Constants;

import com.ixonos.alfresco.search.cm.Content;
import com.ixonos.alfresco.search.cm.ContentResultAdapter;

/**
 * Maps car content objects to instances of Car class.
 * 
 * @author Marko Asplund
 */
public class CarMapper extends MotorVehicleMapper {
	public static final String PROP_DOORS = Constants.createQNameString(
			MotorVehicleConstants.NAMESPACE_VEHICLE, "carDoors");

	@Override
	public void setContentProperties(Content content, ContentResultAdapter result) {
		super.setContentProperties(content, result);
		Car car = (Car)content;
		Map<String, String> p = result.getProperties();
		car.setDoors(new Integer(p.get(PROP_DOORS)));
	}

}
