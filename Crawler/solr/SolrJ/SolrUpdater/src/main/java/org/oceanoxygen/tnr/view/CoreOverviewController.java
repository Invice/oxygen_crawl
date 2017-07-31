package org.oceanoxygen.tnr.view;

import org.oceanoxygen.tnr.MainApp;
import org.oceanoxygen.tnr.model.SolrCore;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;


public class CoreOverviewController {

	@FXML
	private TableView<SolrCore> coreTable;
	
	//Reference to the main application.
	private MainApp mainApp;
	
	/**
	 * The constructor.
	 * This is called before the initialize() method.
	 */
	public CoreOverviewController() {
		
	}
	
	


}
