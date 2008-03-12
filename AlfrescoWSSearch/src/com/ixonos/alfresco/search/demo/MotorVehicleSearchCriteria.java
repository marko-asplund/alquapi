package com.ixonos.alfresco.search.demo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.ixonos.alfresco.search.cm.CMQueryBuilder;
import com.ixonos.alfresco.search.cm.CMSearchCriteria;
import com.ixonos.alfresco.search.cm.QueryBuilder;
import com.ixonos.alfresco.search.cm.SearchCriteria;

/**
 * Encapsulates a set of search criterion for searching MotorVehicles.
 * 
 * @author Marko Asplund
 */
public class MotorVehicleSearchCriteria extends CMSearchCriteria {
	private static final long serialVersionUID = 1L;
	private String manufacturer;
	private String model;
	private String registration;
	
	private MotorVehicleQueryBuilder mvQueryBuilder = new MotorVehicleQueryBuilder();

	public QueryBuilder getQueryBuilder() {
		return mvQueryBuilder;
	}
	
	
	private static class MotorVehicleQueryBuilder implements QueryBuilder {
		private CMQueryBuilder cmQueryBuilder = new CMQueryBuilder();

		@Override
		public String buildLuceneQuery(SearchCriteria criteria) {
			MotorVehicleSearchCriteria mvCriteria = (MotorVehicleSearchCriteria)criteria;
			List<String> search = new ArrayList<String>();
			String query1 = cmQueryBuilder.buildLuceneQuery(criteria);
			if(query1 != null) search.add(query1);
			if(mvCriteria.getManufacturer() != null) {
				QName prop = MotorVehicleConstants.PROP_MANUFACTURER;
				search.add(cmQueryBuilder.getCustomPropertyCriteria(
						prop.getPrefix(), prop.getLocalPart(), mvCriteria.getManufacturer()));
			}
			if(mvCriteria.getModel() != null) {
				QName prop = MotorVehicleConstants.PROP_MODEL;
				search.add(cmQueryBuilder.getCustomPropertyCriteria(
						prop.getPrefix(), prop.getLocalPart(), mvCriteria.getModel()));
			}
			if(mvCriteria.getRegistration() != null) {
				QName prop = MotorVehicleConstants.PROP_REGISTRATION;
				search.add(cmQueryBuilder.getCustomPropertyCriteria(
						prop.getPrefix(), prop.getLocalPart(), mvCriteria.getRegistration()));
			}
			if(search.size() == 0) return null;
			
			StringBuilder sb = cmQueryBuilder.getCombinedCriteria(search);
			
			return sb.toString();
		}
	}


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public String getRegistration() {
		return registration;
	}


	public void setRegistration(String registration) {
		this.registration = registration;
	}

}
