package com.ixonos.alfresco.search.cm;

import org.alfresco.webservice.types.Node;

/**
 * An adapter implementation class for the Node type.
 * 
 * @author Marko Asplund
 */
public class ContentNodeAdapter extends ContentResultAdapter {
	private Node node;
	
	public ContentNodeAdapter(Node node) {
		super(node.getProperties());
		this.node = node;
	}
	
	public String getId() {
		return node.getReference().getUuid();
	}
	
	public String getType() {
		return node.getType();
	}

}
