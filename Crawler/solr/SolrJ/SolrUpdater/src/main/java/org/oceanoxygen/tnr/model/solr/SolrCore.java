package org.oceanoxygen.tnr.model.solr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.oceanoxygen.tnr.util.CoreChangeListener;
import org.oceanoxygen.tnr.util.MyRandom;

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
		closeClient();
		System.out.println("Creating connection with core " + coreName + "...");
		name.set(coreName);
		serverUrl = MainClient.getServerUrl() + coreName;
		client = new HttpSolrClient(serverUrl);
		notifyCoreChangeListeners();
	}
	
	
	private void notifyCoreChangeListeners() {
		for (CoreChangeListener listener : coreChangeListeners) {
			listener.onCoreChange();
		}
	}
	
	/**
	 * Creates a dummy document in the selected core.
	 */
	public void createDummyDocument() {
		SolrInputDocument document = new SolrInputDocument();
		long num = getFreshDummyId();
		document.addField("id", num);
		document.addField("title", "dummy#" + num);
		document.addField("posted", "false");
		document.addField("lang", MyRandom.rndLang());
		
		try {
			client.add(document);
			client.commit();
		} catch (SolrServerException | IOException e) {
			System.err.println("Could not create dummy document:");
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Calculates the current number of documents in the selected core. 
	 * @return
	 */
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
	
	/**
	 * Deletes the document belonging to the id from the selected core.
	 * @param documentId
	 */
	public void deleteDocument(String documentId) {
		
			try {
				client.deleteById(documentId);
				client.commit();
			} catch (SolrServerException | IOException e) {
				System.err.println("Could not delete document:");
				System.err.println(e.getMessage());
			}
	}

	/**
	 * Marks the document as posted by setting the corresponding field to true. 
	 * @param document
	 */
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
	
	/**
	 * Marks the document as not posted by setting the corresponding field to false. 
	 * @param document
	 */
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
	
	/**
	 * Delete all documents with the value fieldVal in their field.
	 * 
	 * NOTE: if both parameter equal "*" the whole index will be deleted. 
	 * @param field
	 * @param fieldVal
	 */
	public void cleanIndex (String field, String fieldVal) {
		try {
			client.deleteByQuery(field + ":" + fieldVal);
			client.commit();
		} catch (SolrServerException | IOException e) {
			System.err.println("Could not clean the index by \"" + field + ":" + fieldVal + "\".");
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the connection to the SolrClient. This method should be called on exit.
	 */
	private void closeClient() {
		if (client != null) {
			System.out.println("Closing connection with core " + this.name.get() + "...");
			try {
				client.close();
			} catch (IOException e) {
				System.err.println("Error closing the SolrCore connection.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Closes the connection to the SolrClient. This method should be called on exit.
	 */
	public static void closeInstance() {
		if (SolrCore.core != null) {
			SolrCore.core.closeClient();
			SolrCore.core = null;
		}
	}
	
	private boolean isFreshDummyId(String id) {
		boolean fresh = false;

		try {
			if (client.getById(id) == null) {
				fresh = true;
			}
		} catch (SolrServerException | IOException e) {
			System.err.println("Could not check if id: \"" + id + "\" is fresh.");
			System.err.println(e.getMessage());
		}
		return fresh;
	}
	
	private long getFreshDummyId() {
		long i = 0;
		while (!isFreshDummyId(Long.toString(i))) {
			i++;
		}
		return i;
	}
}
