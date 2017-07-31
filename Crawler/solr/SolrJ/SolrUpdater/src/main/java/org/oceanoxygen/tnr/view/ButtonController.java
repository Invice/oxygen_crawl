package org.oceanoxygen.tnr.view;

import javafx.application.HostServices;
import javafx.fxml.FXML;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.oceanoxygen.tnr.MainApp;
import org.oceanoxygen.tnr.model.Homepage;

public class ButtonController {
	
	//Reference to the main Application
	private MainApp mainApp;
	private HostServices hostServices;
	
	/**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
	public ButtonController() {
	}
	
	/**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
    }
    
    
    public void setHostServices(HostServices hostServices) {
    	this.hostServices = hostServices;
    }
    
	/**
	 * Called when the user clicks the got to homepage button. Opens the 
	 */
	@FXML
	private void handleOpenHomePage() {

		hostServices.showDocument(Homepage.getHomeAddress());
		
//		 try {
//			 Desktop.getDesktop().browse(new URI("http://www.example.com"));
//	     } catch (IOException e1) {
//	    	 e1.printStackTrace();
//	     } catch (URISyntaxException e1) {
//	    	 e1.printStackTrace();
//	     }
	     
		
		
	}
	
	
	
	
}
