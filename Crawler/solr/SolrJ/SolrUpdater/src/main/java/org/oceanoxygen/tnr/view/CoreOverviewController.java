package org.oceanoxygen.tnr.view;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.oceanoxygen.tnr.MainApp;
import org.oceanoxygen.tnr.model.solr.SolrCore;
import org.oceanoxygen.tnr.model.solr.SolrEntry;
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
	
	private boolean showPosted = false;
	
	public void setShowPosted(boolean showPosted) {
		this.showPosted = showPosted;
	}

	@FXML
	private TextField rowAmount;
	
	@FXML
	private TextField customQuery;

	@FXML
	private TableView<SolrEntry> entryTable;
	
	@FXML
	private TableColumn<SolrEntry, String> entryColumn;
	
	@FXML
	private TextArea url;
	
	@FXML
	private TextArea title;	

	@FXML
	private TextArea score;	

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
	
	@FXML
	private Button createDummyButton;
	
	private ObservableList<SolrEntry> documentList = FXCollections.observableArrayList();
	
	
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
		if (entry != null) {
			setArea(entry, url, "url");			
			setArea(entry, title, "title");
			setArea(entry, posted, "posted");
			setArea(entry, score, "documentScore");
			setArea(entry, content, "content");
			setArea(entry, date, "date");
			setArea(entry, lang, "lang");
		} 
		else {
			url.setText("");
			title.setText("");
			posted.setText("");
			score.setText("");
			content.setText("");
			date.setText("");
			lang.setText("");
		}
	}
	
	private void setArea(SolrEntry entry, TextArea field, String fieldName) {
		String tmp = entry.requestProperty(fieldName);
			field.setText(tmp);
	}
	
	/**
	 * Implementation for CoreChangeListener.
	 */
	public void onCoreChange() {
		showDocumentDetails(null);
		updateDocumentList();
	}
	
	
	/**
	 * Called upon clicking the fetch button.
	 */
	@FXML
	private void fetchHandler() {
		updateDocumentList();
	}
	
	/**
	 * TODO: move query & client to SolrCore
	 * Performs a fetch query with the solr server.
	 * @param posted if true only fetch documents that have been posted.
	 */
	public void updateDocumentList() {
		if (SolrCore.getInstance().getName() == null) {
			System.err.println("Please select a Solr Core before trying to fetch results.");
			return;
		}
		int rowCount = Integer.parseInt(rowAmount.getText());
		
		SolrClient client = SolrCore.getInstance().getClient();
		SolrQuery sQuery = new SolrQuery();
		
		sQuery.setQuery("*:*");
		sQuery.addFilterQuery((showPosted ? "posted:true" : "-posted:true")
				+ (customQuery.getText().equals("") ? "" : (" AND " + customQuery.getText())));
		sQuery.setRows(rowCount);
		sQuery.addOrUpdateSort(new SortClause("documentScore", SolrQuery.ORDER.desc));
		
		entryColumn.setText("Documents (" + rowCount + "/" + SolrCore.getInstance().getDocumentCount() + ")");
		try {
			QueryResponse response = client.query(sQuery);
			SolrDocumentList documents = response.getResults();
			
			documentList.clear();
			
			for (SolrDocument doc : documents) {
				documentList.add(new SolrEntry(doc));
			}
			entryTable.setItems(documentList);
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
			int pos = documentList.indexOf(selectedDocument);
			SolrCore.getInstance().deleteDocument(selectedDocument.getId());
			updateDocumentList();
			entryTable.getSelectionModel().select(pos > 0 ? pos-1 : pos);			
		}
	}
	
	@FXML
	public void createDummyDocument() {
		SolrCore.getInstance().createDummyDocument();
		updateDocumentList();
		entryTable.getSelectionModel().select(documentList.size()-1);
	}
	
	@FXML
	public void markSelectedDocumentAsPosted() {
		SolrEntry selectedDocument = entryTable.getSelectionModel().getSelectedItem();
		if (selectedDocument != null) {
			int pos = documentList.indexOf(selectedDocument);
			SolrCore.getInstance().markDocumentAsPosted(selectedDocument.getDocument());
			updateDocumentList();
			entryTable.getSelectionModel().select((pos==documentList.size()&&documentList.size()<20)?pos-1:pos);
		}
	}
	
	@FXML
	public void markSelectedDocumentAsNotPosted() {
		SolrEntry selectedDocument = entryTable.getSelectionModel().getSelectedItem();
		if (selectedDocument != null) {
			int pos = documentList.indexOf(selectedDocument);
			SolrCore.getInstance().markDocumentAsNotPosted(selectedDocument.getDocument());
			updateDocumentList();
			entryTable.getSelectionModel().select((pos==documentList.size()&&documentList.size()<20)?pos-1:pos);
		}
	}
}
