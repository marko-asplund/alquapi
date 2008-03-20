package com.ixonos.alfresco.search.cm;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.webservice.types.NamedValue;
import org.apache.log4j.Logger;

/**
 * An adapter base class that makes different result objects returned by Alfresco behave similarly.
 * 
 * @author Marko Asplund
 */
public abstract class ContentResultAdapter {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ContentResultAdapter.class);
	protected Map<String, String> properties;
	
	ContentResultAdapter(NamedValue[] props) {
		properties = new HashMap<String, String>(props.length);
		logger.trace("PROPS:");
		for (NamedValue nv : props) {
			properties.put(nv.getName(), nv.getValue());
			if(logger.isTraceEnabled())
				logger.trace("prop: "+nv.getName()+"="+nv.getValue());
		}
	}
	
	public abstract String getId();

	public abstract String getType();

	public Map<String, String> getProperties() {
		return properties;
	}
	
}