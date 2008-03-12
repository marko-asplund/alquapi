package com.ixonos.alfresco.search;


/**
 * General document repository failure exception.
 * 
 * @author Marko Asplund
 */
public class AlfrescoRepositoryException extends AlfrescoException {
	private static final long serialVersionUID = 8450207117226762561L;

	public AlfrescoRepositoryException(Exception cause) {
		super(cause);
	}

}
