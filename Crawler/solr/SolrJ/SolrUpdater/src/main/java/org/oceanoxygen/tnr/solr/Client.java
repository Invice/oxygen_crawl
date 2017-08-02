package org.oceanoxygen.tnr.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class Client {
	
	private static String serverUrl = "http://localhost:8983/solr/";
	
	private static SolrClient singletonClient = null;
	
	private Client () {
	}
	
	/**
	 * Create a new SolrClient as Singleton.
	 * @return
	 */
	public static SolrClient getSolrClient() {
		if (singletonClient == null) {
			Client.singletonClient = new HttpSolrClient(serverUrl);
		}
		
		return Client.singletonClient;
	}
	
	/**
	 * Create a new SolrClient as Singleton.
	 * @param url: The url of the Solr server.
	 * @return
	 */
	public static SolrClient getSolrClient(String url) {
		if (singletonClient == null) {
			Client.singletonClient = new HttpSolrClient(url);
		}
		
		return Client.singletonClient;
	}
	
	public static String getServerUrl() {
		return serverUrl;
	}
	
}
