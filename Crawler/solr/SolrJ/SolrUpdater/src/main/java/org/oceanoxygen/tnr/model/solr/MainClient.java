package org.oceanoxygen.tnr.model.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

public class MainClient {
	
	private static String serverUrl = "http://localhost:8983/solr/";
	
	private static SolrClient singletonClient = null;
	
	private MainClient () {
	}
	
	/**
	 * Create a new SolrClient as Singleton.
	 * @return
	 */
	public static SolrClient getSolrClient() {
		if (singletonClient == null) {
			MainClient.singletonClient = new HttpSolrClient(serverUrl);
		}
		
		return MainClient.singletonClient;
	}
	
	/**
	 * Create a new SolrClient as Singleton.
	 * @param url: The url of the Solr server.
	 * @return
	 */
	public static SolrClient getSolrClient(String url) {
		if (singletonClient == null) {
			MainClient.singletonClient = new HttpSolrClient(url);
		}
		
		return MainClient.singletonClient;
	}
	
	/**
	 * Closes the connection to the SolrClient. This method should be called on exit.
	 */
	public static void closeInstance() {
		if (singletonClient != null) {
			try {
				MainClient.singletonClient.close();
				System.out.println("Closing main client...");
			} catch (IOException e) {
				System.err.println("Error closing SolrClient.");
				e.printStackTrace();
			}
		}
	}
	
	public static String getServerUrl() {
		return serverUrl;
	}
	
}
