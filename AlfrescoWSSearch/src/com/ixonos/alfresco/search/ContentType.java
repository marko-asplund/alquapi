package com.ixonos.alfresco.search;

import javax.xml.namespace.QName;

/**
 * Alfresco content type definition.
 * 
 * @author Marko Asplund
 */
public class ContentType {
	private QName typeName;
	private String title;
	private QName superTypeName;
	private ContentType superType;

	public ContentType(QName typeName, String title, QName superTypeName) {
		this.typeName = typeName;
		this.title = title;
		this.superTypeName = superTypeName;
	}

	public ContentType(QName typeName, String title, ContentType superType) {
		this.typeName = typeName;
		this.title = title;
		this.superType = superType;
	}
	
	public QName getTypeName() {
		return typeName;
	}

	public String getTitle() {
		return title;
	}
	
	public QName getSuperTypeName() {
		return superTypeName;
	}

	public ContentType getSuperType() {
		return superType;
	}
	
	void setSuperType(ContentType st) {
		this.superType = st;
		this.superTypeName = null;
	}

	@Override
	public int hashCode() {
		return typeName.hashCode();
	}

	@Override
	public boolean equals(Object o2) {
		if (this == o2)
			return true;
		if (!(o2 instanceof ContentType))
			return false;
		return typeName.equals(((ContentType) o2).typeName);
	}

	@Override
	public String toString() {
		return "[type=" + typeName + "]";
	}

}
