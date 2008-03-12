package com.ixonos.alfresco.search;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.Constants;
import org.apache.log4j.Logger;

import com.ixonos.alfresco.search.cm.Content;
import com.ixonos.alfresco.search.cm.ContentResultAdapter;
import com.ixonos.alfresco.search.cm.ContentRowAdapter;
import com.ixonos.alfresco.search.cm.MappingConfigEntry;
import com.ixonos.alfresco.search.cm.SearchCriteria;

/**
 * Search content items stored in an Alfresco content repository. The query criteria is represented
 * by an implementation of SearchCriteria. The content items are mapped to Java objects.
 * 
 * @author Marko Asplund
 */
class ContentSearchCommand extends ContentListCommand {
	private final Logger logger = Logger.getLogger(ContentSearchCommand.class.getName());
	private SearchCriteria criteria;

	public ContentSearchCommand(SearchCriteria criteria) {
		this.criteria = criteria;
	}

	public List<Content> execute(
			RepositoryServiceSoapBindingStub repositoryService)
			throws RepositoryFault, RemoteException {
		List<Content> docs = new ArrayList<Content>();
		Store store = AlfrescoUtils.getStore();
		String queryString = criteria.getQueryBuilder().buildLuceneQuery(criteria);
		if (queryString == null) {
			logger.debug("no query criteria, skipping query");
			return docs;
		}
		logger.debug("query: '" + queryString + "'");
		Query query = new Query(Constants.QUERY_LANG_LUCENE, queryString);
		// based on the code the query method seems to return the full result set currently
		// (see AbstractQuerySession). 
		// this would mean that fetchMore will have no effect.
		QueryResult queryResult = repositoryService.query(store, query,
				true);
		ResultSet resultSet = queryResult.getResultSet();
		ResultSetRow[] rows = resultSet.getRows();

		if (rows != null) {
			logger.debug("hits: " + rows.length);

			for (ResultSetRow row : rows) {
				MappingConfigEntry mapping = documentBuilderMapping.getMapping(row);
				ContentResultAdapter result = new ContentRowAdapter(row);
				if(mapping == null) {
					logger.warn("no mapping found, skipping content item: "+result.getId());
					continue;
				}
				Content doc = buildDocument(mapping, result);
				docs.add(doc);
			}
		}
		return docs;
	}
}
	
