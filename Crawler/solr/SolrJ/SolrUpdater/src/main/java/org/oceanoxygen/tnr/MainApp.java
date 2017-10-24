package org.oceanoxygen.tnr;

import java.io.IOException;

import org.oceanoxygen.tnr.view.MenuController;
import org.apache.solr.client.solrj.SolrServerException;
import org.oceanoxygen.tnr.solr.SolrCoreHandler;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private SolrCoreHandler coreHandler = new SolrCoreHandler();
	
	
	
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}	
	
	public SolrCoreHandler getCoreHandler() {
		return coreHandler;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("OxygenCrawl: SolrUpdater");
	
		initRootLayout();
		addMenuBarToRoot();
		addCoreTableOverviewToRoot();
		
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
		
			//Set url bar into the center of root layout.
			rootLayout.setTop(menuBar);
			
			//Give the Controller Access to the main app.
			MenuController controller = loader.getController();
			controller.setMainApp(this);
			controller.fillComboBox(coreHandler.getCoreList());
			
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
		
			//Set url bar into the center of root layout.
			rootLayout.setCenter(coreTable);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
