package org.oceanoxygen.tnr.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Server {
	
	
	private final StringProperty serverName;
	private final StringProperty serverAdress;
	
	
	public Server(String serverName, String serverAdress) {
		this.serverName = new SimpleStringProperty(serverName);
		this.serverAdress = new SimpleStringProperty(serverAdress);
	}
	
	
	
	
	
	
	

}
