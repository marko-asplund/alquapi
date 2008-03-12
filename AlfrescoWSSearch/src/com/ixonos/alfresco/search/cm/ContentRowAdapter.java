package com.ixonos.alfresco.search.cm;

import org.alfresco.webservice.types.ResultSetRow;

/**
 * An adapter implementation class for the Row type.
 * 
 * @author Marko Asplund
 */
public class ContentRowAdapter extends ContentResultAdapter {
	private ResultSetRow row;
	
	public ContentRowAdapter(ResultSetRow row) {
		super(row.getColumns());
		this.row = row;
	}
	
	@Override
	public String getId() {
		return row.getNode().getId();
	}

	@Override
	public String getType() {
		return row.getNode().getType();
	}

}
