package org.oceanoxygen.tnr.view;

import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import org.oceanoxygen.tnr.MainApp;
import org.oceanoxygen.tnr.model.Homepage;
import org.oceanoxygen.tnr.model.SolrCore;
import org.oceanoxygen.tnr.model.SolrEntry;
import org.oceanoxygen.tnr.util.BrowserUtil;

public class MenuController {
	
	@FXML
	public ComboBox<String> coreComboBox;
	
	private HostServices hostServices = null;
	
	//Reference to the main Application
	private MainApp mainApp = null;
	
	/**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
	public MenuController() {
	}
	
	public void fillComboBox(ObservableList<String> coreList) {
		
		coreComboBox.getItems().clear();
		
		coreComboBox.getItems().add("Select Solr core...");
		coreComboBox.getSelectionModel().selectFirst();
		
		coreComboBox.setItems(coreList);
		
		coreComboBox.getSelectionModel().select(0);
		SolrCore.getInstance().setCore(coreComboBox.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	public void initialize(){
	}
    
   
	@FXML
	public void onCoreChange() {
		SolrCore.getInstance().setCore(coreComboBox.getSelectionModel().getSelectedItem());
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
	 * Called when the user clicks the go to homepage option. Opens the website declared in Homepage.getHomeName().
	 */
	@FXML
	private void handleOpenHomepage() {
		if (mainApp != null) {
			BrowserUtil.openUrl(hostServices, Homepage.getHomeName());
		}
	}
	
	
	@FXML
	private void deleteSelectedDocument() {
		if (mainApp != null) {
			mainApp.getCoreOverviewController().deleteSelectedDocument();
		}
	}
	
	@FXML
	public void createDummyDocument() {
		if (mainApp != null) {
			mainApp.getCoreOverviewController().createDummyDocument();
		}
	}
	
	@FXML
	public void markSelectedDocumentAsPosted() {
		if (mainApp != null) {
			mainApp.getCoreOverviewController().markSelectedDocumentAsPosted();;
		}
	}
	
	@FXML
	public void markSelectedDocumentAsNotPosted() {
		if (mainApp != null) {
			mainApp.getCoreOverviewController().markSelectedDocumentAsNotPosted();;
		}
	}
}
