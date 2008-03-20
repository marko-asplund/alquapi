package com.ixonos.alfresco.search.cm;


/**
 * Represent a single content-object mapping configuration entry.
 * Defines the content and builder classes used for mapping a content item. 
 * 
 * @author Marko Asplund
 */
public class MappingConfigEntry {
	private Content contentType;
	private ContentMapper contentBuilder;

	public MappingConfigEntry(Content contentType, ContentMapper contentBuilder) {
		assert(contentType != null && contentBuilder != null);
		this.contentType = contentType;
		this.contentBuilder = contentBuilder;
	}

	MappingConfigEntry(Content contentType) {
		this.contentType = contentType;
	}
	
	public Content getContentType() {
		return contentType;
	}

	public ContentMapper getContentBuilder() {
		return contentBuilder;
	}
	
}
