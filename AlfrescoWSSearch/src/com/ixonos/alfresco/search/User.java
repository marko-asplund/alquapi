package com.ixonos.alfresco.search;

/**
 * An Alfresco user.
 * 
 * @author Marko Asplund
 */
public class User {
	private String userName;
	private String firstName;
	private String lastName;

	public User(String userName, String firstName, String lastName) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}

	@Override
	public int hashCode() {
		return userName.hashCode();
	}

	@Override
	public boolean equals(Object o2) {
		if (this == o2) return true;
		if (!(o2 instanceof User)) return false;
		return userName.equals(((User)o2).userName);
	}

}
