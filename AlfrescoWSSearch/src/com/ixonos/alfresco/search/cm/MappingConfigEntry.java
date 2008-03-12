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
		this.contentType = contentType;
		this.contentBuilder = contentBuilder;
	}

	public Content getContentType() {
		return contentType;
	}

	public ContentMapper getContentBuilder() {
		return contentBuilder;
	}
}
