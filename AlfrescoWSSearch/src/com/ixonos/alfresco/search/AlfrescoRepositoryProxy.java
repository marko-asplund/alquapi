package com.ixonos.alfresco.search;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.WebServiceException;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.log4j.Logger;

import com.ixonos.alfresco.search.cm.Content;
import com.ixonos.alfresco.search.cm.SearchCriteria;


/**
 * An implementation of the AlfrescoRepository interface.
 * 
 * @author Marko Asplund
 */
public class AlfrescoRepositoryProxy implements AlfrescoRepository {
	private static final Logger logger = Logger
			.getLogger(AlfrescoRepositoryProxy.class.getName());
	private ProxyFactory factory;
	
	public AlfrescoRepositoryProxy(ProxyFactory factory) {
		this.factory = factory;
	}	
	
	public List<Content> query(SearchCriteria criteria)
			throws AlfrescoRepositoryException {
		ContentSearchCommand searchCommand = new ContentSearchCommand(criteria);
		return executeDocumentListCommand(searchCommand);
	}

	public List<Content> getDocuments(String[] ids)
			throws AlfrescoRepositoryException {
		ContentGetCommand command = new ContentGetCommand(ids);
		return executeDocumentListCommand(command);
	}

	private List<Content> executeDocumentListCommand(
			ContentListCommand command) throws AlfrescoRepositoryException {
		List<Content> docs = new ArrayList<Content>();

		UserDirectory userDirectory = factory.getUserDirectoryProxy();
		RepositoryDictionary dictionary = factory.getDictionaryProxy();
		ObjectContentMappings documentBuilderMapping = factory.getObjectContentMapping();

		try {
			// - authentication sessions are per-thread so ending a session in one thread only affects
			//   the thread itself
			// - it would be possible to start one authentication session per-thread and never end the session.
			//   AuthenticationUtils.getTicket() returns non-null if there's a session, null otherwise.
			AuthenticationUtils.startSession(factory.getSearchCredentials().getUserName(),
					factory.getSearchCredentials().getPassword());
			RepositoryServiceSoapBindingStub repositoryService =
				WebServiceFactory.getRepositoryService();

			command.setUserDirectory(userDirectory);
			command.setDictionary(dictionary);
			command.setDocumentBuilderMapping(documentBuilderMapping);
			docs = command.execute(repositoryService);

		} catch (AuthenticationFault ex) {
			logger.fatal("authentication failed", ex);
			throw new AlfrescoRepositoryException(ex);
		} catch (RepositoryFault ex) {
			logger.error("repository operation failed", ex);
			throw new AlfrescoRepositoryException(ex);
		} catch (WebServiceException ex) {
			// thrown e.g. when Alfresco is down
			logger.fatal("Failed to communicate with document repository", ex);
			throw new AlfrescoRepositoryException(ex);
		} catch (RemoteException ex) {
			logger.fatal("network connection failed", ex);
			throw new AlfrescoFaultException(ex);
		} finally {
			AuthenticationUtils.endSession();
		}
		logger.debug("command executed");

		return docs;
	}

}
