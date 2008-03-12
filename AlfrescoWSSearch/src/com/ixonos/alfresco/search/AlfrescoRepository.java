package com.ixonos.alfresco.search;

import java.util.List;

import com.ixonos.alfresco.search.cm.Content;
import com.ixonos.alfresco.search.cm.SearchCriteria;

/**
 * Represents an interface to an Alfresco content repository service.
 * 
 * @author Marko Asplund
 */
public interface AlfrescoRepository {
    List<Content> query(SearchCriteria criteria)
    	throws AlfrescoRepositoryException;
    List<Content> getDocuments(String[] ids)
	throws AlfrescoRepositoryException;
    
}
