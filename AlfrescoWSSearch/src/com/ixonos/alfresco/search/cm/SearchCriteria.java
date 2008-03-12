package com.ixonos.alfresco.search.cm;

import java.util.Date;

/**
 * Encapsulate search criteria.
 * 
 * @author Marko Asplund
 */
public interface SearchCriteria {
	QueryBuilder getQueryBuilder();
	String getType();
	String getKeywords();
	Date getModifiedBegin();
	Date getModifiedEnd();
	String getPath();
}