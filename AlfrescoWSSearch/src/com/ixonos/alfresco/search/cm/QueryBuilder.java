package com.ixonos.alfresco.search.cm;

/**
 * An interface for classes that build query strings from SearchCriteria objects.
 * 
 * @author Marko Asplund
 */
public interface QueryBuilder {
	String buildLuceneQuery(SearchCriteria criteria);
}