package org.oceanoxygen.tnr.view;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.oceanoxygen.tnr.MainApp;
import org.oceanoxygen.tnr.model.SolrCore;
import org.oceanoxygen.tnr.model.SolrEntry;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


public class CoreOverviewController {
	
	@FXML
	private TextField rowAmount;

	@FXML
	private TableView<SolrEntry> entryTable;
	
	@FXML
	private TableColumn<SolrEntry, String> entryColumn;
	
	@FXML
	private Label coreName;
	
	private ObservableList<SolrEntry> entries = FXCollections.observableArrayList();
	
	
	
	//Reference to the main application.
	@SuppressWarnings("unused")
	private MainApp mainApp;
	
	/**
	 * The constructor.
	 * This is called before the initialize() method.
	 */
	public CoreOverviewController() {
		
	}
	
	@FXML
	private void initialize() {
		//Initialize the entry table with the entry column.
		entryColumn.setCellValueFactory(entry -> entry.getValue().titleProperty());

		//Allow only numeric values for the number of rows requested.
		rowAmount.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                rowAmount.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });
	}
	
	
	@FXML
	public void FetchHandler() {
		SolrClient client = SolrCore.getInstance("OxygenCrawl").getClient();
		SolrQuery query = new SolrQuery();
		
		query.setQuery("*:*");
		query.setRows(Integer.parseInt(rowAmount.getText()));
		
		
		try {
			QueryResponse response = client.query(query);
			SolrDocumentList documents = response.getResults();
			
			entries.clear();
			
			for (SolrDocument doc : documents) {
				entries.add(new SolrEntry(doc));
			}
			
			entryTable.setItems(entries);
			
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
	}
	

}
