package com.ixonos.alfresco.search.cm;

import java.util.Date;

import com.ixonos.alfresco.search.ContentType;
import com.ixonos.alfresco.search.User;

/**
 * An interface for all content model objects.
 * 
 * @author Marko Asplund
 */
public interface Content {
	String getId();
	void setId(String id);
	String getName();
	void setName(String name);
	String getTitle();
	void setTitle(String title);
	String getDescription();
	void setDescription(String description);
	User getModifier();
	void setModifier(User modifier);
	Date getModified();
	void setModified(Date modified);
	ContentType getType();
	void setType(ContentType type);
	String getAuthor();
	void setAuthor(String author);
}
