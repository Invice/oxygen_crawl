package org.oceanoxygen.tnr.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SolrCore {
	
	private StringProperty name;

	/**
	 * Default constructor.
	 */
	SolrCore(){
		this(null);
	}
	
	/**
	 * Constructor with initial data.
	 * @param name
	 */
	public SolrCore (String name) {
		this.name = new SimpleStringProperty(name);
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(StringProperty name) {
		this.name = name;
	}
	
}
