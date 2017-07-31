package org.oceanoxygen.tnr.util;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowserUtil {
	
	/**
	 * Opens a url given as String in the default browser.
	 * @param url
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void openUrl(String url) throws IOException, URISyntaxException {
        if(Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(new URI(url));
		}
    }
	
	/**
	 * Opens a uri in the default browser. 
	 * @param uri
	 * @throws IOException
	 */
	public static void openUrl(URI uri) throws IOException {
        if(Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(uri);
		}
    }

}
