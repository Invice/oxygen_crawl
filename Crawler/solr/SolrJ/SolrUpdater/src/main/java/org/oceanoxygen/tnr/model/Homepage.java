package org.oceanoxygen.tnr.model;

import java.net.URI;
import java.net.URISyntaxException;

import javafx.scene.control.Hyperlink;

public class Homepage {
	
	private static final Hyperlink homeAddress = new Hyperlink("https://www.ocean-oxygen.org/");
	
	public static final Hyperlink getHomeAddress() {
		return homeAddress;
	}
	
	public static final String getHomeName() {
		return homeAddress.getText();
	}
	
	public static URI getHomeURL() throws URISyntaxException {
		URI uri = new URI(homeAddress.getText());
		return uri;
	}

}
