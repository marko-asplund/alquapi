package com.ixonos.alfresco.search.cm;

import java.util.HashMap;
import java.util.Map;

import com.ixonos.alfresco.search.annotation.AnnotationUtils;

/**
 * Encapsulate user defined content mapping configuration.
 * 
 * @author Marko Asplund
 */
public class MappingConfig {
	private Map<String, MappingConfigEntry> userMapping = new HashMap<String, MappingConfigEntry>();
	
	public void addMapping(String typeName, MappingConfigEntry mapping) {
		assert(mapping != null && mapping.getContentType() != null && mapping.getContentBuilder() != null);
		userMapping.put(typeName, mapping);
	}
	
	public void addMapping(Content content) {
		if(AnnotationUtils.getContentAnnotation(content) == null)
			throw new IllegalArgumentException("Parameter must have the Content annotation");
		String typeName = (String)AnnotationUtils.getContentAttributeValue(content, "qName");
		userMapping.put(typeName, new MappingConfigEntry(content));
	}

	public Map<String, MappingConfigEntry> getMappings() {
		return userMapping;
	}
	
}
