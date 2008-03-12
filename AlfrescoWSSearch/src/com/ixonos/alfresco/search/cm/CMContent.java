package com.ixonos.alfresco.search.cm;

import java.util.Date;

import com.ixonos.alfresco.search.ContentType;
import com.ixonos.alfresco.search.User;

/**
 * Alfresco standard content model content object.
 * 
 * @author Marko Asplund
 */
public class CMContent implements Content {
    private String id;
    private ContentType type;
    private String name;
    private String title;
    private String description;
    private User modifier;
    private Date modified;
    private String author;
    
    public CMContent() {
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getModifier() {
        return modifier;
    }

    public void setModifier(User modifier) {
        this.modifier = modifier;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
    	final int prime = 31;
    	int result = 1;
    	result = prime * result + ((id == null) ? 0 : id.hashCode());
    	return result;
    }

    @Override
    public boolean equals(Object o2) {
    	if (this == o2) return true;
    	if (!(o2 instanceof CMContent)) return false;
    	CMContent doc2 = (CMContent) o2;
    	return id != null ? id.equals(doc2.id) : doc2.id==null;
    }

    @Override
    public String toString() {
    	return type+":"+name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
