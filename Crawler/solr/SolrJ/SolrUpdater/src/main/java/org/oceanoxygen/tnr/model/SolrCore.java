package org.oceanoxygen.tnr.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.oceanoxygen.tnr.solr.Client;
import org.oceanoxygen.tnr.util.CoreChangeListener;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SolrCore {
	
	private static SolrCore core = null;
	/**
	 * Return the instance of SolrCore as singleton.
	 * @return
	 */
	public static SolrCore getInstance() {
		if (SolrCore.core == null) {
			SolrCore.core = new SolrCore();
		}
		return SolrCore.core;
	}
	private SolrClient client;
	
	private List<CoreChangeListener> coreChangeListeners = new ArrayList<>();
	
	private StringProperty name;

	private String serverUrl;
	
	/**
	 * Default constructor.
	 */
	private SolrCore(){
		client = null;
		name = new SimpleStringProperty(null);
	}
	
	/**
	 * Get the client belonging to the current core.
	 * @return
	 */
	public SolrClient getClient() {
		return client;
	}

	/**
	 * Return the name of the current core.
	 * @return
	 */
	public String getName() {
		return name.get();
	}
	
	/**
	 * Register a listener to be notified on core changes.
	 * @param listener
	 */
	public void registerCoreChangeListener(CoreChangeListener listener) {
		coreChangeListeners.add(listener);
	}
	
	/**
	 * Change the current core.
	 * @param coreName: the new core.
	 */
	public void setCore(String coreName) {
		name.set(coreName);
		serverUrl = Client.getServerUrl() + coreName;
		client = new HttpSolrClient(serverUrl);
		notifyCoreChangeListeners();
	}
	
	private void notifyCoreChangeListeners() {
		for (CoreChangeListener listener : coreChangeListeners) {
			listener.onCoreChange();
		}
	}
	
}
