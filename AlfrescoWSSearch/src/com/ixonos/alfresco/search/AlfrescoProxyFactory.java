package com.ixonos.alfresco.search;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ixonos.alfresco.search.cm.MappingConfigEntry;

/**
 * Factory that creates proxy classes for communicating with an Alfresco server.
 * The factory instance is configured with the Alfresco server address, credentials and
 * content mapping information. 
 * It can create UserDirectory, RepositoryDictionary and AlfrescoRepository interface implementations
 * for that particular Alfresco server. The accessor methods refresh these implementations
 * when they get expired.
 * 
 * @author Marko Asplund
 */
public class AlfrescoProxyFactory implements ProxyFactory {
	private URL repositoryAddress;
	private Credentials adminCredentials;
	private Credentials searchCredentials;
	private Map<String, MappingConfigEntry> documentBuilderMappingConfig;

	private AlfrescoUserDirectoryProxy userDirectory;
	private RepositoryDictionary dataDictionary;
	private AlfrescoRepositoryProxy repository;
	private static AtomicBoolean initialized = new AtomicBoolean();

	
	public AlfrescoProxyFactory(URL repositoryAddress, Credentials adminCredentials,
			Credentials searchCredentials, Map<String, MappingConfigEntry> userBuilderMappingConfig) {
		assert(repositoryAddress != null && adminCredentials != null &&
				searchCredentials != null);
		if(!initialized.get())
			initialize(repositoryAddress);
		// make local copies of all the input parameters
		try {
			this.repositoryAddress = new URL(repositoryAddress.toString());
		} catch (MalformedURLException e) {
			throw new AlfrescoFaultException("invalid server URL", e);
		}
		this.adminCredentials = new Credentials(adminCredentials);
		this.searchCredentials = new Credentials(searchCredentials);
		if(userBuilderMappingConfig == null)
			userBuilderMappingConfig = Collections.emptyMap();
		documentBuilderMappingConfig = new HashMap<String, MappingConfigEntry>();
		for(String type : userBuilderMappingConfig.keySet())
			documentBuilderMappingConfig.put(type, userBuilderMappingConfig.get(type));
	}
	
	private void initialize(URL repositoryAddress) {
		// see AlfrescoUtils for more information.
		AlfrescoUtils.initializeWSAPI(repositoryAddress);
		initialized.set(true);
	}
	
	public synchronized UserDirectory getUserDirectoryProxy() {
		if(userDirectory != null && !userDirectory.isExpired())
			return userDirectory;
		userDirectory = new AlfrescoUserDirectoryProxy(repositoryAddress, adminCredentials);
		return userDirectory;
	}
	
	public synchronized RepositoryDictionary getDictionaryProxy() {
		if(dataDictionary != null && !dataDictionary.isExpired())
			return dataDictionary;
		dataDictionary = new AlfrescoDictionaryProxy(repositoryAddress, adminCredentials);
		return dataDictionary;
	}
	
	public synchronized AlfrescoRepository getRepositoryProxy() {
		if(repository == null)
			repository = new AlfrescoRepositoryProxy(this);
		return repository;
	}

	public URL getRepositoryAddress() {
		return repositoryAddress;
	}

	public Credentials getAdminCredentials() {
		return adminCredentials;
	}

	public Credentials getSearchCredentials() {
		return searchCredentials;
	}

	public Map<String, MappingConfigEntry> getDocumentBuilderMappingConfig() {
		return documentBuilderMappingConfig;
	}

}
