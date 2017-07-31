package org.oceanoxygen.tnr.view;



import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import org.oceanoxygen.tnr.MainApp;
import org.oceanoxygen.tnr.model.Homepage;
import org.oceanoxygen.tnr.util.BrowserUtil;
import org.oceanoxygen.tnr.model.SolrCore;

public class MenuController {
	
	@FXML
	public ComboBox<String> coreComboBox;
	
	
	//Reference to the main Application
	private MainApp mainApp = null;
	private HostServices hostServices = null;
	
	/**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
	public MenuController() {
	}
	
	/**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.hostServices = mainApp.getHostServices();
    }
    
   
	/**
	 * Called when the user clicks the go to homepage button. Opens the website declared in Homepage.getHomeName().
	 */
	@FXML
	private void handleOpenHomepage() {

		if (mainApp != null) {
			BrowserUtil.openUrl(hostServices, Homepage.getHomeName());
		}
	}
	
	public void fillComboBox(ObservableList<SolrCore> coreList) {
		
		coreComboBox.getItems().clear();
		
		coreComboBox.getItems().add("Select Solr core...");
		coreComboBox.getSelectionModel().selectFirst();
		
		for (SolrCore core : coreList) {
			String tmp = core.getName().get();
			coreComboBox.getItems().add(tmp);
		}
		
	}
	
	
}
