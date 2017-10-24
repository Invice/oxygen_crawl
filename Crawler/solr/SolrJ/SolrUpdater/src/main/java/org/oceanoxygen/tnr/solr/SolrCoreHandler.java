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
	
	private ObservableList <String> coreList = FXCollections.observableArrayList();
	private String currentCore = "";
	
	public String getCurrentCore() {
		return currentCore;
	}

	public void setCurrentCore(String currentCore) {
		this.currentCore = currentCore;
	}


	/**
	 * Processes the current list of cores and returns their Names in a list.
	 * @return
	 * @throws IOException 
	 */
	public ObservableList<String> getCoreList() throws SolrServerException, IOException{
		
		coreList.clear();
		
//		HttpSolrClient client = new HttpSolrClient(serverUrl);
		CoreAdminRequest request = new CoreAdminRequest();
		request.setAction(CoreAdminAction.STATUS);
		CoreAdminResponse cores = request.process(Client.getSolrClient());
		
		for (int i = 0; i < cores.getCoreStatus().size(); i++) {
			coreList.add(cores.getCoreStatus().getName(i));
		}
		
		return coreList;
	}
	
	
	
	
	
}
