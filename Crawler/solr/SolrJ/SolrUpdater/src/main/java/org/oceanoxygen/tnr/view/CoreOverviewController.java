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
import org.oceanoxygen.tnr.util.CoreChangeListener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class CoreOverviewController implements CoreChangeListener {
	
	@FXML
	private TextField rowAmount;

	@FXML
	private TableView<SolrEntry> entryTable;
	
	@FXML
	private TableColumn<SolrEntry, String> entryColumn;
	
	@FXML
	private TextField coreName;
	
	@FXML
	private TextArea title;	

	@FXML
	private TextArea url;	

	@FXML
	private TextArea posted;
	
	@FXML 
	private TextArea lang;
	
	@FXML 
	private TextArea content;
	
	@FXML
	private TextArea date;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private Button postedButton;
	private boolean isPosted = false;
	
	@FXML
	private Button createDummyButton;
	
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
	
	/**
	 * Fills all text fields to show details about the entry.
	 * If the specified entry is null, all text fields are cleared.
	 * 
	 * @param entry the entry or null
	 */
	private void showDocumentDetails(SolrEntry entry) {
		
//		coreName.setText(mainApp.getCoreHandler().getCurrentCore());
		
		if (entry != null) {
			
//			coreName.setText(SolrCore.getInstance().getName());
			coreName.setText(entry.getId());
			setArea(entry, title, "title");
			setArea(entry, url, "url");			
			setArea(entry, posted, "posted");
			setArea(entry, content, "content");
			setArea(entry, date, "date");
			setArea(entry, lang, "lang");
			
		} 
		else {
			coreName.setText("");
			title.setText("");
			posted.setText("");
			url.setText("");
			content.setText("");
			date.setText("");
			lang.setText("");
		}
	}
	
	private void setArea(SolrEntry entry, TextArea field, String fieldName) {
		String tmp = entry.requestProperty(fieldName);
		if (tmp == "" && fieldName.equals("posted")) {
			field.setText("false");
		} else {
			field.setText(tmp);
		}
	}
	
	public void onCoreChange() {
		showDocumentDetails(null);
		fetchDocuments();
	}
	
	
	/**
	 * Called upon clicking the fetch button.
	 */
	@FXML
	private void fetchHandler() {
		fetchDocuments();
	}
	
	/**
	 * Performs a fetch query with the solr server.
	 */
	public void fetchDocuments() {
		if (SolrCore.getInstance().getName() == null) {
			System.err.println("Please select a Solr Core before trying to fetch results.");
			return;
		}
		
		SolrClient client = SolrCore.getInstance().getClient();
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
			
		} catch (IOException | SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	
	@FXML
	private void initialize() {
		// Initialize the entry table with the entry column.
		entryColumn.setCellValueFactory(entry -> entry.getValue().titleProperty());

		// Allow only numeric values for the number of rows requested.
		rowAmount.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            if (!newValue.matches("\\d*")) {
	                rowAmount.setText(newValue.replaceAll("[^\\d]", ""));
	            }
	        }
	    });
		
		
		/*
		 * Icon made by Zurb (https://www.flaticon.com/authors/zurb)
		 */
		Image image = new Image(this.getClass().getResourceAsStream("img/trash-bin-16px.png"));
		deleteButton.setGraphic(new ImageView(image));
		ButtonBar.setButtonUniformSize(deleteButton, false);
		
		/*
		 * Icon made by Eleonor Wang (https://www.flaticon.com/authors/eleonor-wang)
		 */
		image = new Image(this.getClass().getResourceAsStream("img/checked-16px.png"));
		postedButton.setGraphic(new ImageView(image));
		
		/*
		 * Icon made by Smashicons (https://www.flaticon.com/authors/smashicons)
		 */
		image = new Image(this.getClass().getResourceAsStream("img/add-16px.png"));
		createDummyButton.setGraphic(new ImageView(image));
		
		
		// Reset entry Details.
		showDocumentDetails(null);
		
		// Listen for selection changes and show entry details when changed.
		entryTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showDocumentDetails(newValue));
		
		// Register as listener to SolrCore.
		SolrCore.getInstance().registerCoreChangeListener(this);
		
	}
	
	@FXML
	public void deleteSelectedDocument() {
		SolrEntry selectedDocument = entryTable.getSelectionModel().getSelectedItem();
		if (selectedDocument != null) {
			int pos = entries.indexOf(selectedDocument);
			SolrCore.getInstance().deleteDocument(selectedDocument.getId());
			fetchDocuments();
			entryTable.getSelectionModel().select(pos-1);
		}
	}
	
	@FXML
	public void createDummyDocument() {
		SolrCore.getInstance().createDummyDocument();
		fetchDocuments();
		entryTable.getSelectionModel().select(entries.size()-1);
	}
	
	@FXML
	public void markSelectedDocumentAsPosted() {
		SolrEntry selectedDocument = entryTable.getSelectionModel().getSelectedItem();
		if (selectedDocument != null) {
			int pos = entries.indexOf(selectedDocument);
			SolrCore.getInstance().markDocumentAsPosted(selectedDocument.getDocument());
			fetchDocuments();
			entryTable.getSelectionModel().select(pos);
		}
	}
	
	@FXML
	public void markSelectedDocumentAsNotPosted() {
		SolrEntry selectedDocument = entryTable.getSelectionModel().getSelectedItem();
		if (selectedDocument != null) {
			int pos = entries.indexOf(selectedDocument);
			SolrCore.getInstance().markDocumentAsNotPosted(selectedDocument.getDocument());
			fetchDocuments();
			entryTable.getSelectionModel().select(pos);
		}
	}

}
