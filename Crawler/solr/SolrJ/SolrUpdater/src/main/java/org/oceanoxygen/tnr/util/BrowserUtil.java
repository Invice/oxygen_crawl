package org.oceanoxygen.tnr.util;

import javafx.application.HostServices;

public class BrowserUtil {
	
	/**
	 * Opens a url a String in the default browser using HostServices.
	 * @param service
	 * @param url
	 */
	public static void openUrl(HostServices service, String url) {
        service.showDocument(url);
    }
	
}
