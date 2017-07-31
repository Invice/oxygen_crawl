package org.oceanoxygen.tnr;

import java.io.IOException;

import org.oceanoxygen.tnr.model.Homepage;
import org.oceanoxygen.tnr.view.ButtonController;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;



/**
 * Hello world!
 *
 */
public class MainApp extends Application {
	
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}	
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("OxygenCrawl: SolrUpdater");
	
		initRootLayout();
		addButtonBarToRoot();
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
	 * Adds the url bar to root layout.
	 */
	public void addButtonBarToRoot() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ButtonBar.fxml"));
			AnchorPane UrlBar = (AnchorPane) loader.load();
		
			//Set url bar into the center of root layout.
			rootLayout.setCenter(UrlBar);
			
			//Give the Controller Access to the main app.
			ButtonController controller = loader.getController();
			controller.setMainApp(this);
			getHostServices();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
