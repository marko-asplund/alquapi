package com.ixonos.alfresco.search;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.apache.log4j.Logger;

import com.ixonos.alfresco.search.cm.Content;
import com.ixonos.alfresco.search.cm.ContentNodeAdapter;
import com.ixonos.alfresco.search.cm.ContentResultAdapter;
import com.ixonos.alfresco.search.cm.MappingConfigEntry;

/**
 * Load a list of content items specified by item ids from an Alfresco content repository.
 * The content items are mapped to Java objects.
 * 
 * @author Marko Asplund
 */
class ContentGetCommand extends ContentListCommand {
	private final Logger logger = Logger.getLogger(ContentGetCommand.class);
	private String[] ids;

	public ContentGetCommand(String[] ids) {
		this.ids = ids;
	}

	public List<Content> execute(RepositoryServiceSoapBindingStub repositoryService)
			throws RepositoryFault, RemoteException {
		List<Content> docs = new ArrayList<Content>();
		if (ids == null) {
			logger.debug("id list empty, skipping query");
			return docs;
		}
		Reference[] refs = new Reference[ids.length];
		for (int i = 0; i < ids.length; i++)
			refs[i] = new Reference(AlfrescoUtils.getStore(), ids[i], null);
		Predicate pr = new Predicate(refs, AlfrescoUtils.getStore(), null);
		Node[] nodes = repositoryService.get(pr);
		for (Node node : nodes) {
			MappingConfigEntry mapping = documentBuilderMapping.getMapping(node);
			ContentResultAdapter result = new ContentNodeAdapter(node);
			if(mapping == null) {
				logger.warn("no mapping found, skipping content item: "+result.getId());
				continue;
			}
			Content doc = buildDocument(mapping, result);
			docs.add(doc);
		}
		return docs;
	}
}
