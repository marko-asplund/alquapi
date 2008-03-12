package com.ixonos.alfresco.search;

/**
 * Exception class for representing non-recoverable, runtime exceptions.
 * 
 * Non-recoverable, checked exceptions can be caught and re-thrown as FaultException.
 * 
 * @author Marko Asplund
 * 
 * @see <a href="http://dev2dev.bea.com/pub/a/2006/11/effective-exceptions.html">Effective Java Exceptions</a>
 */
public class AlfrescoFaultException extends RuntimeException {
	private static final long serialVersionUID = -8369337374420864703L;

	public AlfrescoFaultException(Exception cause) {
        super(cause);
    }
    
    public AlfrescoFaultException(String msg, Exception cause) {
        super(msg, cause);
    }
    
    public AlfrescoFaultException(String msg) {
        super(msg);
    }

}
