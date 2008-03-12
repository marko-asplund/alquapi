package com.ixonos.alfresco.search;

/**
 * User credentials.
 * 
 * @author Marko Asplund
 */
public class Credentials {
	private String userName;
	private String password;
	
	public Credentials(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public Credentials(Credentials credentials) {
		this(credentials.getUserName(), credentials.getPassword());
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

}
