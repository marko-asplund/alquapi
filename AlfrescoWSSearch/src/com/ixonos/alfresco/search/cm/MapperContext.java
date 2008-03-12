package com.ixonos.alfresco.search.cm;

import com.ixonos.alfresco.search.RepositoryDictionary;
import com.ixonos.alfresco.search.UserDirectory;

/**
 * Encapsulate the context required by the ContentMapper implementation classes.
 * 
 * @author Marko Asplund
 */
public class MapperContext {
	private RepositoryDictionary dictionary;
	private UserDirectory userDirectory;
	
	public MapperContext(RepositoryDictionary dictionary, UserDirectory userDirectory) {
		this.dictionary = dictionary;
		this.userDirectory = userDirectory;
	}

	public RepositoryDictionary getDictionary() {
		return dictionary;
	}

	public UserDirectory getUserDirectory() {
		return userDirectory;
	}

}
