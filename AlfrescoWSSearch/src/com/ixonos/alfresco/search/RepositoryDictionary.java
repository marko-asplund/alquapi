package com.ixonos.alfresco.search;

import java.util.Map;

import javax.xml.namespace.QName;

/**
 * Defines the interface for classes encapsulating Alfresco repository content type information.
 * 
 * @author Marko Asplund
 */
public interface RepositoryDictionary {
	boolean isExpired();
	ContentType getType(QName typeName);
	Map<QName, ContentType> getTypeMap();
	ContentType getRootType();
}