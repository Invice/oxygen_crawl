package org.oceanoxygen.tnr.model;

import java.net.URI;
import java.net.URISyntaxException;

public class Homepage {
	
	private static final String homeAddress = "https://www.ocean-oxygen.org/";
	
	public static String getHomeAddress() {
		return homeAddress;
	}
	
	public static URI getHomeURL() throws URISyntaxException {
		URI uri = new URI(homeAddress);
		return uri;
	}

}
