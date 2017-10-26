package org.oceanoxygen.tnr.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
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
	
	
	
	public void createDummyDocument() {
		long num = getDocumentCount() + 1;
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", num);
		document.addField("title", "name#" + num);
		document.addField("posted", "false");
		try {
			client.add(document);
			client.commit();
		} catch (SolrServerException | IOException e) {
			System.err.println("Could not create dummy document:");
			System.err.println(e.getMessage());
		}
	}
	
	
	public long getDocumentCount() {
		SolrQuery q = new SolrQuery("*:*");
	    q.setRows(0);  // don't actually request any data
	    long numDocs = 0;
	    try {
			numDocs = client.query(q).getResults().getNumFound();
		} catch (SolrServerException | IOException e) {
			System.err.println("Could not retrieve number of documents:");
			System.err.println(e.getMessage());
		}
	    return numDocs;
	}
	
	public void deleteDocument(String documentId) {
		
			try {
				client.deleteById(documentId);
				client.commit();
			} catch (SolrServerException | IOException e) {
				System.err.println("Could not delete document:");
				System.err.println(e.getMessage());
			}
	}
	
	public void markDocumentAsPosted(SolrDocument document) {
		try {
			
			if (document.getFieldNames().contains("posted")) {
				document.setField("posted", "true");
			} else {
				document.addField("posted", "true");
			}
			SolrInputDocument doc = new SolrInputDocument();
			
			for (String fieldName : document.getFieldNames()) {
				doc.addField(fieldName, document.getFieldValue(fieldName));
			}
			client.add(doc);
			client.commit();
		} catch (SolrServerException | IOException e) {
			System.err.println("Could not create dummy document:");
			System.err.println(e.getMessage());
		}
	}
	
	public void markDocumentAsNotPosted(SolrDocument document) {
		try {
			
			if (document.getFieldNames().contains("posted")) {
				document.setField("posted", "false");
			} else {
				document.addField("posted", "false");
			}
			SolrInputDocument doc = new SolrInputDocument();
			
			for (String fieldName : document.getFieldNames()) {
				doc.addField(fieldName, document.getFieldValue(fieldName));
			}
			client.add(doc);
			client.commit();
		} catch (SolrServerException | IOException e) {
			System.err.println("Could not create dummy document:");
			System.err.println(e.getMessage());
		}
	}
}
