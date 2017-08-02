package org.oceanoxygen.tnr.model;

import org.apache.solr.common.SolrDocument;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SolrEntry {
	
	private SolrDocument document;
	private StringProperty id;
	private StringProperty title;
	
	public SolrEntry(SolrDocument doc) {
		this.document = doc;
		this.id = new SimpleStringProperty(document.getFieldValue("id").toString());
		this.title = new SimpleStringProperty(document.getFieldValue("title").toString());
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
}
