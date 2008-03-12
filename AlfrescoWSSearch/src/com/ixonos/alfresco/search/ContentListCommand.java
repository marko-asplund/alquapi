package com.ixonos.alfresco.search;

import java.rmi.RemoteException;
import java.util.List;

import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;

import com.ixonos.alfresco.search.cm.Content;
import com.ixonos.alfresco.search.cm.ContentMapper;
import com.ixonos.alfresco.search.cm.ContentResultAdapter;
import com.ixonos.alfresco.search.cm.MapperContext;
import com.ixonos.alfresco.search.cm.MappingConfigEntry;

/**
 * Base class for operations that fetch a batch of content items from an Alfresco repository.
 * 
 * @author Marko Asplund
 */
abstract class ContentListCommand {
	protected UserDirectory userDirectory;
	protected RepositoryDictionary dictionary;
	protected ObjectContentMappings documentBuilderMapping;

	public void setUserDirectory(UserDirectory userDirectory) {
		this.userDirectory = userDirectory;
	}

	public void setDictionary(RepositoryDictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void setDocumentBuilderMapping(ObjectContentMappings documentBuilderMapping) {
		this.documentBuilderMapping = documentBuilderMapping;
	}

	abstract List<Content> execute(
			RepositoryServiceSoapBindingStub repositoryService)
			throws RepositoryFault, RemoteException;

	
	Content buildDocument(MappingConfigEntry mapping, ContentResultAdapter result) {
		Content doc = null;
		try {
			doc = mapping.getContentType().getClass().newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("failed to create", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("failed to create", e);
		}
		
		ContentMapper builder = mapping.getContentBuilder();
		builder.setMapperContext(new MapperContext(dictionary, userDirectory));
		builder.setContentProperties(doc, result);
		
		return doc;
	}

}
