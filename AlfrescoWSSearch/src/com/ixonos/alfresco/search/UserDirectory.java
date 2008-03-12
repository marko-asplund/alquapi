package com.ixonos.alfresco.search;

/**
 * Represent an interface to a user directory service.
 * 
 * @author Marko Asplund
 */
public interface UserDirectory {
    User getUser(String userName);
}
