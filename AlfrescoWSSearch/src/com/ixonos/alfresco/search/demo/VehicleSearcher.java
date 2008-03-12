package com.ixonos.alfresco.search.demo;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ixonos.alfresco.search.AlfrescoProxyFactory;
import com.ixonos.alfresco.search.AlfrescoRepository;
import com.ixonos.alfresco.search.Credentials;
import com.ixonos.alfresco.search.ProxyFactory;
import com.ixonos.alfresco.search.cm.Content;
import com.ixonos.alfresco.search.cm.MappingConfigEntry;


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
    	Map<String, MappingConfigEntry> config = new HashMap<String, MappingConfigEntry>();
    	config.put(MotorVehicleConstants.TYPE_VEHICLE.toString(),
    			new MappingConfigEntry(new MotorVehicle(), new MotorVehicleMapper()));
    	config.put(MotorVehicleConstants.TYPE_CAR.toString(),
    			new MappingConfigEntry(new Car(), new CarMapper()));

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
    			sb.append("vehicle: "+mv.getRegistration());
    		}
    		if(d instanceof Car) {
    			Car md = (Car)d;
    			sb.append(", doors: "+md.getDoors());
    		}
    		if(sb.length()>0)
    			logger.debug(sb.toString());
    	}
    }
    
    public static void main(String ... args) throws Exception {
    	VehicleSearcher s = new VehicleSearcher();
    	s.testDocumentSearch();
    }
    
    
}
