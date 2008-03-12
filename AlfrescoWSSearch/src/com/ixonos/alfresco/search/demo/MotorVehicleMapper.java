package com.ixonos.alfresco.search.demo;


import java.util.Map;

import com.ixonos.alfresco.search.cm.CMContentMapper;
import com.ixonos.alfresco.search.cm.Content;
import com.ixonos.alfresco.search.cm.ContentMapper;
import com.ixonos.alfresco.search.cm.ContentResultAdapter;
import com.ixonos.alfresco.search.cm.MapperContext;

/**
 * Maps motor vehicle content objects to instances of MotorVehicle class.
 * 
 * @author Marko Asplund
 */
public class MotorVehicleMapper implements ContentMapper {
	protected MapperContext context;
	protected CMContentMapper baseBuilder = new CMContentMapper();


	@Override
	public void setContentProperties(Content content, ContentResultAdapter result) {
		baseBuilder.setMapperContext(context);
		baseBuilder.setContentProperties(content, result);

		MotorVehicle c = (MotorVehicle)content;
		Map<String, String> p = result.getProperties();
		c.setManufacturer(p.get(MotorVehicleConstants.PROP_MANUFACTURER.toString()));
		c.setModel(p.get(MotorVehicleConstants.PROP_MODEL.toString()));
		c.setRegistration(p.get(MotorVehicleConstants.PROP_REGISTRATION.toString()));
	}

	@Override
	public MapperContext getMapperContext() {
		return context;
	}

	@Override
	public void setMapperContext(MapperContext context) {
		this.context = context;
	}

}
