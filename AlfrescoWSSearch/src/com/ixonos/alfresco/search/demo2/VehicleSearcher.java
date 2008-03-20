package com.ixonos.alfresco.search.demo2;

import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import com.ixonos.alfresco.search.AlfrescoProxyFactory;
import com.ixonos.alfresco.search.AlfrescoRepository;
import com.ixonos.alfresco.search.Credentials;
import com.ixonos.alfresco.search.ProxyFactory;
import com.ixonos.alfresco.search.cm.Content;
import com.ixonos.alfresco.search.cm.MappingConfig;
import com.ixonos.alfresco.search.demo.MotorVehicleSearchCriteria;


/**
 * Class for demonstrating how to execute a query using the Alfresco WS Java Query API.
 * 
 * @author Marko Asplund
 */
public class VehicleSearcher {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(VehicleSearcher.class);
	private ProxyFactory factory;
	
    private void setUp() throws Exception {
    	URL repositoryUrl = new URL("http://localhost:8080");
    	Credentials adminCredentials = new Credentials("admin", "admin");
    	Credentials searchCredentials = new Credentials("search", "search");

    	// configure custom content mapping
    	MappingConfig config = new MappingConfig();
    	config.addMapping(new MotorVehicle());
    	config.addMapping(new Car());

    	factory = new AlfrescoProxyFactory(repositoryUrl, adminCredentials,
    			searchCredentials, config);
    }
    
    public void testDocumentSearch() throws Exception {
    	setUp();
    	
    	AlfrescoRepository repository = factory.getRepositoryProxy();
    	MotorVehicleSearchCriteria c = new MotorVehicleSearchCriteria();
    	c.setKeywords("*");
    	c.setRegistration("abc-123");
    	List<Content> docs = repository.query(c);
    	for(Content d : docs) {
    		StringBuilder sb = new StringBuilder();
    		if(d instanceof MotorVehicle) {
    			MotorVehicle mv = (MotorVehicle)d;
    			sb.append("vehicle: "+mv.getRegistration()+","+mv.getManufacturer()+","+mv.getModel());
    		}
    		if(d instanceof Car) {
    			Car md = (Car)d;
    			sb.append(", doors: "+md.getDoors()+","+md.getId());
    		}
    		if(sb.length()>0)
    			logger.info(sb.toString());
    	}
    }
    
    public static void main(String ... args) throws Exception {
    	VehicleSearcher s = new VehicleSearcher();
    	s.testDocumentSearch();
    }
    
    
}
