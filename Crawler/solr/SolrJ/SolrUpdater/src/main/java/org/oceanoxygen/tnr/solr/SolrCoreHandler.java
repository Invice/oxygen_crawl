package org.oceanoxygen.tnr.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
//import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.common.params.CoreAdminParams.CoreAdminAction;

import org.oceanoxygen.tnr.solr.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SolrCoreHandler {
	
//	private String serverUrl = "http://localhost:8983/solr";
	private ObservableList <String> coreList = FXCollections.observableArrayList();
	
	/**
	 * Processes the current list of cores and returns their Names in a list.
	 * @return
	 */
	public ObservableList<String> getCoreList(){
		
		coreList.clear();
		
//		HttpSolrClient client = new HttpSolrClient(serverUrl);
		CoreAdminRequest request = new CoreAdminRequest();
		request.setAction(CoreAdminAction.STATUS);
		CoreAdminResponse cores = null;
		
		try {
			cores = request.process(Client.getSolrClient());
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < cores.getCoreStatus().size(); i++) {
			coreList.add(cores.getCoreStatus().getName(i));
		}
		
		return coreList;
	}
	
	
	
	
	
}
