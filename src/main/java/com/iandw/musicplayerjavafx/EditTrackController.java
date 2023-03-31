package com.iandw.musicplayerjavafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class EditTrackController {
    @FXML
    private AnchorPane anchorPane;
    @FXML private TextField editTextField;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private ListView<String> artistListView;
    private ListViewLibrary listViewLibrary;
    private TableViewLibrary tableViewLibrary;
    private TableView<TrackMetadata> trackTableView;
    private ObservableList<TrackMetadata> trackMetadataList;
    private String columnName;


    private void initializeData(String columnName, String mutableTrackData, ObservableList<TrackMetadata> trackMetadataList,
                                TableView<TrackMetadata> trackTableView, ListView<String> artistListView,
                                ListViewLibrary listViewLibrary, TableViewLibrary tableViewLibrary, Stage stage)
    {
        this.columnName = columnName;
        this.artistListView = artistListView;
        this.trackMetadataList = trackMetadataList;
        this.trackTableView = trackTableView;
        this.listViewLibrary = listViewLibrary;
        this.tableViewLibrary = tableViewLibrary;
        editTextField.setText(mutableTrackData);
        anchorPane.requestFocus();

        // Key Bindings
        stage.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                okButton(stage);
            }
        });

        stage.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });
    }

    public void showEditWindow(String columnName, String mutableTrackData, ObservableList<TrackMetadata> trackMetadataList,
                               TableView<TrackMetadata> trackTableView, ListView<String> artistListView,
                               ListViewLibrary listViewLibrary, TableViewLibrary tableViewLibrary) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("edittrack.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        EditTrackController controller = loader.getController();

        controller.initializeData(columnName, mutableTrackData, trackMetadataList, trackTableView, artistListView,
                listViewLibrary, tableViewLibrary, stage);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void okButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        okButton(stage);
    }

    private void okButton(Stage stage) {
        String userInput = editTextField.getText();
        final String artistName = "Artist Name";
        final String trackTitle = "Track Title";
        final String albumTitle = "Album Title";
        final String genre = "Genre";

        System.out.printf("Updating %s %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(),
                columnName);

        switch (columnName) {
            case artistName -> {
                trackTableView.getSelectionModel().getSelectedItem().setArtistNameStr(userInput);

                if (!artistListView.getItems().contains(userInput)) {
                    listViewLibrary.addArtist(userInput);
                    artistListView.setItems(listViewLibrary.getArtistObservableList());
                }

                // Simulate mouse click to update tableview
                if (artistListView.getSelectionModel().getSelectedItem() != null) {
                    MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                            false, false, false, false, true, false, false, true, false, false, null);
                    artistListView.fireEvent(mouseEvent);
                }
            }

            case trackTitle -> trackTableView.getSelectionModel().getSelectedItem().setTrackTitleStr(userInput);

            case albumTitle -> trackTableView.getSelectionModel().getSelectedItem().setAlbumTitleStr(userInput);

            case genre -> trackTableView.getSelectionModel().getSelectedItem().setTrackGenreStr(userInput);
        }

        // Write to file on close
        tableViewLibrary.setOutputTrackListOnClose();

        trackTableView.refresh();

        stage.close();
    }

    @FXML
    private void cancelButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
