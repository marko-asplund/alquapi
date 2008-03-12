package com.ixonos.alfresco.search;

/**
 * Base class for recoverable error conditions.
 * 
 * @author Marko Asplund
 */
public class AlfrescoException extends Exception {
	private static final long serialVersionUID = 8512988179065461842L;

	public AlfrescoException() {
        super();
    }
    
    public AlfrescoException(Exception cause) {
        super(cause);
    }
	
}
