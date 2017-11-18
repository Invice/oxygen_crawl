package org.oceanoxygen.tnr;

import java.io.IOException;

import org.oceanoxygen.tnr.view.CoreOverviewController;
import org.oceanoxygen.tnr.view.MenuController;
import org.apache.solr.client.solrj.SolrServerException;
import org.oceanoxygen.tnr.model.solr.MainClient;
import org.oceanoxygen.tnr.model.solr.SolrCore;
import org.oceanoxygen.tnr.model.solr.SolrCoreListHandler;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private SolrCoreListHandler coreListHandler = new SolrCoreListHandler();
	
	private MenuController menuController = null;
	private CoreOverviewController coreOverviewController = null;
		
	public Stage getPrimaryStage() {
		return primaryStage;
	}	
	
	public SolrCoreListHandler getCoreListHandler() {
		return coreListHandler;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("OxygenCrawl: SolrUpdater");
	
		initRootLayout();
		addMenuBarToRoot();
		addCoreTableOverviewToRoot();
	}
	
	@Override
	public void stop() {
		SolrCore.closeInstance();
		MainClient.closeInstance();
	}
	
	/*
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from xml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Adds the menu bar to root layout.
	 */
	public void addMenuBarToRoot() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/MenuBar.fxml"));
			AnchorPane menuBar = (AnchorPane) loader.load();
		
			// Set url bar into the center of root layout.
			rootLayout.setTop(menuBar);
			
			// Give the Controller Access to the main app.
			menuController = loader.getController();
			menuController.setMainApp(this);
			menuController.fillComboBox(coreListHandler.getCoreList());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException serverException) {
			System.err.println(serverException.getMessage());
			System.err.println("Please make sure to run solr before running this app.");
			System.exit(0);
		}
	}
	
	/*
	 * Adds the central table to the root layout.
	 */
	public void addCoreTableOverviewToRoot() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/CoreTableOverview.fxml"));
			AnchorPane coreTable = (AnchorPane) loader.load();
			
			// Set url bar into the center of root layout.
			rootLayout.setCenter(coreTable);

			// Save the controller as a field.
			coreOverviewController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public CoreOverviewController getCoreOverviewController () {
		return this.coreOverviewController;
	}
}
