package com.ixonos.alfresco.search.cm;

/**
 * Interface for classes that map content objects to Java objects.
 * 
 * @author Marko Asplund
 */
public interface ContentMapper {
	void setMapperContext(MapperContext context);
	MapperContext getMapperContext();
	void setContentProperties(Content content, ContentResultAdapter result);
}
