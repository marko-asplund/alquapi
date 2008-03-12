package com.ixonos.alfresco.search;


import java.net.URL;

import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.log4j.Logger;

/**
 * Utility class for Alfresco related operations.
 * 
 * @author Marko Asplund
 */
class AlfrescoUtils {
	private static final Logger logger = Logger.getLogger(AlfrescoUtils.class);
	private static String repositoryAddress;
	private static final String ALFRESCO_WS_CONTEXT = "/alfresco/api";
	static final String STORE_ID = "SpacesStore";
	
	/**
	 * Initialize the library. Done from inside the library so the user doesn't need to explicitly call this method.
	 * 
	 * <p>The Alfresco Web Services Client 2.1 only allows a single authenticated server session at a given time.
	 * The initialization method below is used to make sure that the library is used only with one Alfresco server.
	 * Other possible solutions would include:
	 * <ol>
	 * <li> create a lock (or queue operations) to serialize access to the methods that communicate with Alfresco
	 * <li> make each Alfresco data access method happen in a separate thread (apply patch found in AR-2169)
	 * <li> remove the limitation in Alfresco WS Client
	 * </ol>
	 * This restriction results from AuthenticationUtils.startSession and endSession using
	 * the WebServiceFactory.getAuthenticationService method for obtaining an authentication service reference.
	 * getAuthenticationService always binds to the global Alfresco server address retrieved with
	 * WebServiceFactory.getEndpointAddress.
	 */
	public static synchronized void  initializeWSAPI(URL address) {
		if(repositoryAddress == null) {
			logger.debug("initializing");
			repositoryAddress = address.toString();
			WebServiceFactory.setEndpointAddress(address.toString() + ALFRESCO_WS_CONTEXT);
		} else if (!repositoryAddress.equals(address.toString())) {
			String msg = "trying to reinitialize WS API with different endpoint address: "+
			repositoryAddress + " != " + address;
			logger.error(msg);
			throw new AlfrescoFaultException(msg);
		}		
	}
	
	static Store getStore() {
		return new Store(Constants.WORKSPACE_STORE, STORE_ID);
	}
	
}
