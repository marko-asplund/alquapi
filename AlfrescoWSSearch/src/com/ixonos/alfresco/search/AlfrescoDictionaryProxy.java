package com.ixonos.alfresco.search;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.alfresco.webservice.administration.AdministrationFault;
import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.dictionary.ClassPredicate;
import org.alfresco.webservice.dictionary.DictionaryServiceSoapBindingStub;
import org.alfresco.webservice.types.ClassDefinition;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.log4j.Logger;

import static com.ixonos.alfresco.search.cm.ContentModelConstants.*;


/**
 * Class for fetching repository metadata from an Alfresco data dictionary.
 * Loads content type definitions.
 * 
 * @author Marko Asplund
 */
public class AlfrescoDictionaryProxy implements RepositoryDictionary {
	private static final Logger logger = Logger
			.getLogger(AlfrescoDictionaryProxy.class.getName());
	private static final QName rootTypeName = new QName(Constants.NAMESPACE_CONTENT_MODEL,
			CONTENT_MODEL_DOCUMENT_TYPE_NAME);
	private int reloadIntervalSeconds = 10800;
	
	@SuppressWarnings("unused")
	private URL repositoryUrl; // unused for now
	private Credentials adminCredentials;
	private Map<QName, ContentType> documentTypes;
	private Date loadTime;


	public AlfrescoDictionaryProxy(URL repositoryUrl, Credentials adminCredentials) {
		this.repositoryUrl = repositoryUrl;
		this.adminCredentials = adminCredentials;
		documentTypes = getDocumentTypes();
		loadTime = new Date();
	}
	
	public boolean isExpired() {
		if (((new Date().getTime() - loadTime.getTime()) / 1000) > reloadIntervalSeconds) {
			logger.debug("expired: " + loadTime + ", " + new Date());
			return true;
		}
		return false;
	}

	private Map<QName, ContentType> getDocumentTypes() {
		logger.debug("** loading document types");
		Map<QName, ContentType> types = new HashMap<QName, ContentType>();
		
		try {
			AuthenticationUtils.startSession(adminCredentials.getUserName(),
					adminCredentials.getPassword());
            DictionaryServiceSoapBindingStub dictionaryService = WebServiceFactory
            	.getDictionaryService();

			// NB: get only types under cm:content hierarchy
			ClassPredicate classPredicate = new ClassPredicate(
					new String[] { NAMESPACE_PREFIX_CONTENT_MODEL + ":"
							+ CONTENT_MODEL_DOCUMENT_TYPE_NAME }, true, false);
			ClassPredicate aspectPredicate = new ClassPredicate(
					new String[] {}, false, false);
			ClassDefinition[] defs = dictionaryService.getClasses(classPredicate,
					aspectPredicate);
			if(defs==null) return types;

			// construct a tree of document type definitions
			List<ContentType> unprocessed = new LinkedList<ContentType>();
			for (ClassDefinition cd : defs) {
				QName typeName = QName.valueOf(cd.getName());
				QName superTypeName = QName.valueOf(cd.getSuperClass());
				
				ContentType type = null;
				ContentType superType = types.get(superTypeName);
				if(superType == null) {
					type = new ContentType(typeName, cd.getTitle(), superTypeName);
					unprocessed.add(type);
				} else {
					type = new ContentType(typeName, cd.getTitle(), superType);
				}
				types.put(type.getTypeName(), type);
			}
			// make additional passes through type list to connect every type with super type
			for(int i = 0; unprocessed.size() > 0 && i < 100; i++) {
				for(ListIterator<ContentType> li = unprocessed.listIterator(); li.hasNext(); ) {
					ContentType type = li.next();
					if(rootTypeName.equals(type.getTypeName())) {
						li.remove();
						continue;
					}
					ContentType superType = types.get(type.getSuperTypeName());
					if(superType != null) {
						type.setSuperType(superType);
						li.remove();
					}
				}
			}
			if(unprocessed.size() > 0) {
				logger.fatal("Failed to construct type hierarchy");
				throw new AlfrescoFaultException("Failed to construct type hierarchy");
			}

		} catch (AuthenticationFault ex) {
			logger.error("authentication failed", ex);
			throw new AlfrescoFaultException(ex);
		} catch (AdministrationFault ex) {
			logger.error("repository operation failed", ex);
			throw new AlfrescoFaultException(ex);
		} catch (RemoteException ex) {
			logger.error("network connection failed", ex);
			throw new AlfrescoFaultException(ex);
		} finally {
			AuthenticationUtils.endSession();
		}
		types = Collections.unmodifiableMap(types);
		if(logger.isTraceEnabled()) dumpTypes(types);
		return types;
	}
	
	// debugging aid
	private void dumpTypes(Map<QName, ContentType> types) {
		for(QName n : types.keySet()) {
			ContentType dt = types.get(n);
			StringBuilder path = new StringBuilder(dt.getTypeName().getLocalPart());
			while(dt != null) {
				path.append(" << "+(dt.getSuperType() != null ?
						dt.getSuperType().getTypeName().getLocalPart() :
							dt.getSuperTypeName()));
				dt = dt.getSuperType();
			}
			logger.debug("type: "+path.toString());
		}
	}
	
	public ContentType getType(QName typeName) {
		ContentType t = documentTypes.get(typeName);
		return t;
	}

	public Map<QName, ContentType> getTypeMap() {
		return documentTypes;
	}
	
}
