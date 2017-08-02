package org.oceanoxygen.tnr.model;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.oceanoxygen.tnr.solr.Client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SolrCore {
	
	private String serverUrl;
	private SolrClient client = null;
	private StringProperty name = null;
	
	private static SolrCore core = null;

	/**
	 * Default constructor.
	 */
	private SolrCore(){
	}
	
	/**
	 * Constructor with initial data.
	 * @param name
	 */
	private SolrCore (String coreName) {
		
		name = new SimpleStringProperty(coreName);
		serverUrl = Client.getServerUrl() +  coreName;
		client = new HttpSolrClient(serverUrl);
	}
	
	public static SolrCore getInstance(String name) {
		if (SolrCore.core == null) {
			SolrCore.core = new SolrCore(name);
		}
		return SolrCore.core;
	}
	

	public String getName() {
		return name.get();
	}

	public void setCore(String coreName) {
		name.set(coreName);
		serverUrl = Client.getServerUrl() + coreName;
		client = new HttpSolrClient(serverUrl);
	}
	
	public SolrClient getClient() {
		return client;
	}
	
}
