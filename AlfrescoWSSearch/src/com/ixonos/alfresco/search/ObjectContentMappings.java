package com.ixonos.alfresco.search;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.apache.log4j.Logger;

import com.ixonos.alfresco.search.cm.CMContent;
import com.ixonos.alfresco.search.cm.CMContentMapper;
import com.ixonos.alfresco.search.cm.ContentMapper;
import com.ixonos.alfresco.search.cm.ContentModelConstants;
import com.ixonos.alfresco.search.cm.MapperGeneratorASM;
import com.ixonos.alfresco.search.cm.MappingConfigEntry;

/**
 * Hold mappings for content types found in the content repository.
 * Each content type name is mapped to a MappingConfigEntry object that contains
 * an instance of the class used to represent content items of that type and
 * a mapper object that can build objects from content items.
 * <p>
 * The mapping is initialized based on user provided as well as built-in mappings.
 * If no mapping exists for a type, the first existing super type mapping is used.
 * 
 * @author Marko Asplund
 */
class ObjectContentMappings {
	private static final Logger logger = Logger.getLogger(ObjectContentMappings.class);
	private Map<QName, MappingConfigEntry> builderMapping;
	private MapperGeneratorASM mapperGenerator = new MapperGeneratorASM();

	
	public ObjectContentMappings(Map<String, MappingConfigEntry> userMapping,
			RepositoryDictionary dictionary, UserDirectory userDirectory) {
		logger.debug("initializing content mappings");

		// merge user provided mappings with base mappings. user mappings take preference 
		builderMapping = new HashMap<QName, MappingConfigEntry>(getBaseMappingConfig());
		for(String typeName : userMapping.keySet())
			builderMapping.put(QName.valueOf(typeName), userMapping.get(typeName));
		logger.trace("userMapping keys: "+builderMapping.keySet());

		// add mapping for each type in the data dictionary.
		Map<QName, ContentType> types = dictionary.getTypeMap();
		for(ContentType sub : dictionary.getRootType().getSubTypes())
			buildMappings(types, sub);
		if(logger.isTraceEnabled())
			dumpTypeTree(dictionary.getRootType(), "");
	}
	
	private void dumpTypeTree(ContentType type, String indent) {
		logger.trace(indent+type.getTypeName().getLocalPart() + " (" +
				builderMapping.get(type.getTypeName()).getContentBuilder() + ")");
		for(ContentType sub : type.getSubTypes())
			dumpTypeTree(sub, indent + "+");
	}
	
	// build mappings bottom-up
	private void buildMappings(Map<QName, ContentType> types, ContentType type) {
		MappingConfigEntry builder = getMappingForType(type.getTypeName(), types);
		builderMapping.put(type.getTypeName(), builder);
		for(ContentType sub : type.getSubTypes())
			buildMappings(types, sub);
	}
	
	@SuppressWarnings("unchecked")
	private MappingConfigEntry getMappingForType(QName typeName, Map<QName, ContentType> types) {
		MappingConfigEntry mapping = builderMapping.get(typeName); // explicit mapping found
		StringBuilder path = new StringBuilder(typeName.getLocalPart());
		if(mapping == null) {
			// no explicitly defined mapping, fall back to the first ancestor mapping
			ContentType currentType = types.get(typeName);
			while(mapping == null) {
				currentType = currentType.getSuperType();
				path.append(" << "+currentType.getTypeName().getLocalPart());
				mapping = builderMapping.get(currentType.getTypeName());
			}
			if(mapping == null)
				throw new AlfrescoFaultException("no builder found for type "+typeName);
		}
		if(mapping.getContentBuilder() == null) {
			ContentType superType = types.get(typeName).getSuperType();
			MappingConfigEntry superMapping = builderMapping.get(superType.getTypeName());
			Class superMapperClass = superMapping.getContentBuilder().getClass();
			Class mapperClass = mapperGenerator.generateMapper(mapping.getContentType(), superMapperClass);
			ContentMapper mapper;
			try {
				mapper = (ContentMapper)mapperClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Failed to create a new instance", e);
			}
			mapping = new MappingConfigEntry(mapping.getContentType(), mapper);
		}
//		logger.trace("builder: "+path.toString());
		return mapping;
	}	

	private Map<QName, MappingConfigEntry> getBaseMappingConfig() {
		Map<String, MappingConfigEntry> config = new HashMap<String, MappingConfigEntry>();
		config.put(ContentModelConstants.CM_DOCUMENT_TYPE_NAME,
				new MappingConfigEntry(new CMContent(), new CMContentMapper()));

		Map<QName, MappingConfigEntry> baseMapping = new HashMap<QName, MappingConfigEntry>();
		for(String typeName : config.keySet())
			baseMapping.put(QName.valueOf(typeName), config.get(typeName));
		return baseMapping;
	}

	public MappingConfigEntry getMapping(ResultSetRow row) {
		ResultSetRowNode node = row.getNode();
		return getMapping(QName.valueOf(node.getType()));
	}

	public MappingConfigEntry getMapping(Node node) {
		return getMapping(QName.valueOf(node.getType()));
	}

	public MappingConfigEntry getMapping(QName typeName) {
		return builderMapping.get(typeName);
	}
	
}
