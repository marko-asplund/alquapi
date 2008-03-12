package com.ixonos.alfresco.search.cm;

import org.alfresco.webservice.util.Constants;

/**
 * Constants related to the Alfresco standard content model.
 * 
 * @author Marko Asplund
 */
public class ContentModelConstants {
	public static final String NAMESPACE_PREFIX_CONTENT_MODEL = "cm";
	
	public static final String PROP_STORE_IDENTIFIER = Constants.createQNameString(
			Constants.NAMESPACE_SYSTEM_MODEL, "store-identifier");
	public static final String PROP_STORE_PROTOCOL = Constants.createQNameString(
			Constants.NAMESPACE_SYSTEM_MODEL, "store-protocol");
	public static final String PROP_MODIFIED = Constants.createQNameString(
			Constants.NAMESPACE_CONTENT_MODEL, "modified");
	public static final String PROP_MODIFIER = Constants.createQNameString(
			Constants.NAMESPACE_CONTENT_MODEL, "modifier");
	public static final String PROP_AUTHOR = Constants.createQNameString(
			Constants.NAMESPACE_CONTENT_MODEL, "author");
	public static final String CONTENT_MODEL_DOCUMENT_TYPE_NAME = "content";

	public static final String CM_DOCUMENT_TYPE_NAME = Constants.createQNameString(
			Constants.NAMESPACE_CONTENT_MODEL, CONTENT_MODEL_DOCUMENT_TYPE_NAME);
}
