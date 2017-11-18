package org.oceanoxygen.tnr.model.solr;

import org.apache.solr.common.SolrDocument;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SolrEntry {
	
	private SolrDocument document;
	private StringProperty id;
	private StringProperty title;
	
	@SuppressWarnings("unused")
	private SolrEntry() {
	}

	public SolrEntry(SolrDocument doc) {
		this.document = doc;
		
		this.id = new SimpleStringProperty(requestProperty("id"));
		this.title = new SimpleStringProperty(requestProperty("title"));
	}

	public String getId() {
		return id.get();
	}
	
	public StringProperty idProperty() {
		return id;
	}

	public StringProperty titleProperty() {
		return title;
	}
	
	public SolrDocument getDocument() {
		return document;
	}
	
	public String requestProperty(String propertyName) {
		Object property = document.getFieldValue(propertyName);
		if (property != null) {
			return property.toString();
		} else {
			return "";
		}
	}
}
