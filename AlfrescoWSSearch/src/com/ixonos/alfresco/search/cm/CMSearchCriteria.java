package com.ixonos.alfresco.search.cm;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;


/**
 * Represents a set of Alfresco standard content model search criterion.
 * 
 * @author Marko Asplund
 */
public class CMSearchCriteria implements Serializable, SearchCriteria {
	private static final long serialVersionUID = -8777088031600754019L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CMSearchCriteria.class);
	
	private QueryBuilder queryBuilder = new CMQueryBuilder();
	
	private String keywords;
    private String type;
    private Date modifiedBegin;
    private Date modifiedEnd;
    private String path;

    
    public String getType() {
        return type; 
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Date getModifiedBegin() {
        return modifiedBegin;
    }

    public void setPublishBegin(Date modifiedBegin) {
        this.modifiedBegin = modifiedBegin;
    }

    public Date getModifiedEnd() {
        return modifiedEnd;
    }

    public void setModifiedEnd(Date modifiedEnd) {
        this.modifiedEnd = modifiedEnd;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public QueryBuilder getQueryBuilder() {
    	return queryBuilder;
    }
    
}
