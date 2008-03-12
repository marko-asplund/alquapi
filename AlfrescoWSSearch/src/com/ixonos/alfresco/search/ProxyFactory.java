package com.ixonos.alfresco.search;

import java.net.URL;
import java.util.Map;

import com.ixonos.alfresco.search.cm.MappingConfigEntry;

/**
 * Interface for factories that create proxy classes for communicating with an Alfresco server.
 * The factories can create UserDirectory, RepositoryDictionary and AlfrescoRepository interface implementations
 * for a particular Alfresco server.
 * 
 * @author Marko Asplund
 */
public interface ProxyFactory {
	UserDirectory getUserDirectoryProxy();
	RepositoryDictionary getDictionaryProxy();
	AlfrescoRepository getRepositoryProxy();
	URL getRepositoryAddress();
	Credentials getAdminCredentials();
	Credentials getSearchCredentials();
	Map<String, MappingConfigEntry> getDocumentBuilderMappingConfig();
}