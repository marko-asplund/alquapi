package com.ixonos.alfresco.search.cm;

import static com.ixonos.alfresco.search.cm.ContentModelConstants.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.xml.namespace.QName;

import org.alfresco.webservice.util.Constants;
import org.apache.log4j.Logger;

import com.ixonos.alfresco.search.AlfrescoFaultException;
import com.ixonos.alfresco.search.ContentType;

/**
 * Base class for objects that map content objects to Java objects.
 * 
 * @author Marko Asplund
 */
public class ContentMapperBase implements ContentMapper {
	private static final Logger logger = Logger.getLogger(ContentMapperBase.class);
	protected MapperContext context;
	
	
	public ContentMapperBase() {
	}

	public void setContentProperties(Content doc, ContentResultAdapter result) {
		doc.setId(result.getId());
		QName typeName = QName.valueOf(result.getType());
		ContentType type = context.getDictionary().getType(typeName);
		doc.setType(type);

		Map<String, String> p = result.getProperties();
		doc.setTitle(p.get(Constants.PROP_TITLE));
		doc.setName(p.get(Constants.PROP_NAME));
		doc.setDescription(p.get(Constants.PROP_DESCRIPTION));
		doc.setModifier(context.getUserDirectory().getUser(p.get(PROP_MODIFIER)));
		doc.setModified(parseAlfrescoDate(p.get(PROP_MODIFIED)));
		doc.setAuthor(p.get(PROP_AUTHOR));
		// save store protocol and store id in content item?
	}

	public static Date parseAlfrescoDate(String date) {
		date = date.substring(0, 26) + date.substring(27);
		final String fmt = "yyyy-MM-dd'T'hh:mm:ss.SSSZ";
		DateFormat df = new SimpleDateFormat(fmt);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			logger.error("Alfresco returned an invalid date: " + date, e);
			throw new AlfrescoFaultException(e);
		}
	}
	
	@Override
	public MapperContext getMapperContext() {
		return context;
	}

	@Override
	public void setMapperContext(MapperContext context) {
		this.context = context;
	}
	

}
