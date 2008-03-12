package com.ixonos.alfresco.search.cm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.alfresco.webservice.util.Constants;

import static com.ixonos.alfresco.search.cm.ContentModelConstants.*;

/**
 * Build an Alfresco standard content model query string in Lucene syntax from a
 * search criteria object.
 * 
 * @author Marko Asplund
 */
public class CMQueryBuilder implements QueryBuilder {
	private static String fixedCriteria = null;

	public CMQueryBuilder() {
	}

	public String buildLuceneQuery(SearchCriteria criteria) {
		List<String> search = new ArrayList<String>();
		if (fixedCriteria != null && fixedCriteria.length() > 0)
			search.add(fixedCriteria);
		if (criteria.getKeywords() != null
				&& criteria.getKeywords().length() > 0)
			// this only matches content, not title and description like in web client
			search.add(getKeywordCriteria(criteria.getKeywords()));
		if (criteria.getType() != null && criteria.getType().length() > 0) {
			String qn = Constants.createQNameString(
					Constants.NAMESPACE_CONTENT_MODEL, criteria.getType());
			search.add("TYPE:\"" + qn + "\"");
		}
		if (criteria.getModifiedBegin() != null
				&& criteria.getModifiedEnd() != null) {
			search.add(getDateRangeCriteria(NAMESPACE_PREFIX_CONTENT_MODEL,
					PROP_MODIFIED, criteria.getModifiedBegin(), criteria
							.getModifiedEnd()));
		}
		if (criteria.getPath() != null && criteria.getPath().length() > 0)
			search.add("PATH:\"" + criteria.getPath() + "\"");
		if (search.size() == 0)
			return null;

		StringBuilder sb = getCombinedCriteria(search);

		return sb.toString();
	}
	
	public StringBuilder getCombinedCriteria(List<String> search) {
		StringBuilder sb = new StringBuilder();
		for(Iterator<String> i = search.iterator(); i.hasNext(); ) {
			sb.append(i.next());
			if(i.hasNext())	sb.append(" AND ");
		}
		return sb;
	}

	public String getKeywordCriteria(String kw) {
		StringBuilder sb = new StringBuilder();
		String[] kws = kw.split(" ");
		for (int i = 0; i < kws.length; i++) {
			sb.append("TEXT:" + kws[i]);
			if (i < (kws.length - 1))
				sb.append(" AND ");
		}
		if (sb.length() > 0) {
			sb.insert(0, "(");
			sb.append(")");
		}
		return sb.toString();
	}

	public String getCustomPropertyCriteria(String ns, String property,
			String value) {
		return "@" + ns + "\\:" + property + ":\"" + value + "\"";
	}

	public String getDateRangeCriteria(String ns, String property, Date begin,
			Date end) {
		DateFormat df = new SimpleDateFormat("yyyy\\-MM\\-dd'T'HH:mm:ss");
		return "@" + ns + "\\:" + property + ":[" + df.format(begin) + " TO "
				+ df.format(end) + "]";
	}

}
