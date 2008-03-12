package com.ixonos.alfresco.search;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.alfresco.webservice.administration.AdministrationFault;
import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.administration.UserDetails;
import org.alfresco.webservice.administration.UserFilter;
import org.alfresco.webservice.administration.UserQueryResults;
import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.log4j.Logger;

/**
 * Alfresco implementation of the UserDirectory interface.
 * 
 * @author Marko Asplund
 */
class AlfrescoUserDirectoryProxy implements UserDirectory {
	private static final Logger logger = Logger
			.getLogger(AlfrescoUserDirectoryProxy.class.getName());
	private int reloadIntervalSeconds = 10800; // 3h
	
	@SuppressWarnings("unused")
	private URL repositoryUrl; // unused for now
	private Credentials adminCredentials;
	private Map<String, User> users;
	private Date loadTime;

	
	public AlfrescoUserDirectoryProxy(URL repositoryUrl, Credentials adminCredentials) {
		this.repositoryUrl = repositoryUrl;
		this.adminCredentials = adminCredentials;
		users = getAllUsers();
		loadTime = new Date();
	}

	public boolean isExpired() {
		if (((new Date().getTime() - loadTime.getTime()) / 1000) > reloadIntervalSeconds) {
			logger.debug("expired: " + loadTime + ", " + new Date());
			return true;
		}
		return false;
	}

	private Map<String, User> getAllUsers() {
		logger.debug("** loading users");
		Map<String, User> users = new HashMap<String, User>();
		try {
			AuthenticationUtils.startSession(adminCredentials.getUserName(),
					adminCredentials.getPassword());

			AdministrationServiceSoapBindingStub adminService = WebServiceFactory
					.getAdministrationService();
			UserQueryResults results = adminService.queryUsers(new UserFilter(
					""));
			for (UserDetails userInfo : results.getUserDetails()) {
				String userName = null, firstName = null, lastName = null;
				for (NamedValue nv : userInfo.getProperties()) {
					if (Constants.PROP_USERNAME.equals(nv.getName()))
						userName = nv.getValue();
					else if (Constants.PROP_USER_FIRSTNAME.equals(nv.getName()))
						firstName = nv.getValue();
					else if (Constants.PROP_USER_LASTNAME.equals(nv.getName()))
						lastName = nv.getValue();
					//		    log.debug(nv.getName()+"# "+nv.getValue());
				}
				User user = new User(userName, firstName, lastName);
				users.put(userName, user);
			}
		} catch (AuthenticationFault ex) {
			logger.error("authentication failed", ex);
			throw new AlfrescoFaultException(ex);
		} catch (AdministrationFault ex) {
			logger.error("repository operation failed", ex);
			throw new AlfrescoFaultException(ex);
		} catch (RemoteException ex) {
			logger.error("network connection failed", ex);
			throw new AlfrescoFaultException(ex);
		} finally {
			AuthenticationUtils.endSession();
		}
		return Collections.unmodifiableMap(users);
	}

	public User getUser(String userName) {
		User u = users.get(userName);
		if (u == null)
			u = new User(userName, "Unknown", "[" + userName + "]");
		return u;
	}

}
