package org.oceanoxygen.tnr.view;

import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;

import org.oceanoxygen.tnr.MainApp;
import org.oceanoxygen.tnr.model.Homepage;
import org.oceanoxygen.tnr.model.solr.SolrCore;
import org.oceanoxygen.tnr.util.BrowserUtil;

public class MenuController {
	
	@FXML 
	public CheckMenuItem toggleShowPostedDocuments;
	
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
	
	/**
	 * Adds the cores of the SolrClient instance to the combobox.
	 * @param coreList
	 */
	public void fillComboBox(ObservableList<String> coreList) {
		coreComboBox.getItems().clear();
		
		if (coreList != null) {
			coreComboBox.setItems(coreList);
			coreComboBox.getSelectionModel().select(0);
			SolrCore.getInstance().setCore(coreComboBox.getSelectionModel().getSelectedItem());	
		} else {
			coreComboBox.getItems().add("No SolrCores available.");
			coreComboBox.getSelectionModel().selectFirst();
		}
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
	 * Called when the user clicks the go to homepage option. Opens the website 
	 * declared in Homepage.getHomeName().
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
	private void createDummyDocument() {
		if (mainApp != null) {
			mainApp.getCoreOverviewController().createDummyDocument();
		}
	}
	
	@FXML
	private void exitApplication() {
		if (mainApp != null) {
			mainApp.getPrimaryStage().close();
		}
	}
	
	@FXML
	private void markSelectedDocumentAsPosted() {
		if (mainApp != null) {
			mainApp.getCoreOverviewController().markSelectedDocumentAsPosted();;
		}
	}
	
	@FXML
	private void markSelectedDocumentAsNotPosted() {
		if (mainApp != null) {
			mainApp.getCoreOverviewController().markSelectedDocumentAsNotPosted();;
		}
	}
	
	@FXML
	private void cleanIndexByLang() {
		if (mainApp != null) {
			SolrCore.getInstance().cleanIndex("-lang", "en");
			mainApp.getCoreOverviewController().updateDocumentList();
		}
	}
	
	@FXML
	private void deleteDummyDocuments() {
		if (mainApp != null) {
			SolrCore.getInstance().cleanIndex("title", "dummy#");
			mainApp.getCoreOverviewController().updateDocumentList();
		}
	}
	
	@FXML 
	private void showPostedDocuments() {
		if (mainApp !=  null) {
			mainApp.getCoreOverviewController().setShowPosted(
					toggleShowPostedDocuments.selectedProperty().get());
			mainApp.getCoreOverviewController().updateDocumentList();
		}
	}
	
	@FXML private void updatePostedStatus() {
		if (mainApp != null) {
			SolrCore.getInstance().updatePostedStatus();
			mainApp.getCoreOverviewController().updateDocumentList();
		}
	}
}
