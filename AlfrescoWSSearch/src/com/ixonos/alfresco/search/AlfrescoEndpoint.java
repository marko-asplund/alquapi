package com.ixonos.alfresco.search;

import java.net.URL;

/**
 * Encapsulate Alfresco server address and user credentials used for accessing the server.
 * 
 * @author Marko Asplund
 *
 */
public class AlfrescoEndpoint {
	private URL alfrescoAddress;
	private String userName;
	private String password;

	public URL getAlfrescoAddress() {
		return alfrescoAddress;
	}
	public void setAlfrescoAddress(URL alfrescoAddress) {
		this.alfrescoAddress = alfrescoAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
