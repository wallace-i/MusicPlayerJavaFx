package com.iandw.musicplayerjavafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class EditTrackController {
    @FXML
    private AnchorPane anchorPane;
    @FXML private TextField editTextField;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private ListView<String> artistPlaylistListView;
    private ListViewLibrary listViewLibrary;
    private TableViewLibrary tableViewLibrary;
    private TableView<Track> trackTableView;
    private ObservableList<Track> trackList;
    private String columnName;

    private void initializeData(String columnName, String mutableTrackData, ObservableList<Track> trackList,
                                TableView<Track> trackTableView, ListView<String> artistPlaylistListView,
                                ListViewLibrary listViewLibrary, TableViewLibrary tableViewLibrary)
    {
        this.columnName = columnName;
        this.artistPlaylistListView = artistPlaylistListView;
        this.trackList = trackList;
        this.trackTableView = trackTableView;
        this.listViewLibrary = listViewLibrary;
        this.tableViewLibrary = tableViewLibrary;
        editTextField.setText(mutableTrackData);
        editTextField.setFocusTraversable(false);
    }

    public void showEditWindow(String columnName, String mutableTrackData, ObservableList<Track> trackList,
                               TableView<Track> trackTableView, ListView<String> artistPlaylistListView,
                               ListViewLibrary listViewLibrary, TableViewLibrary tableViewLibrary) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("edittrack.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        EditTrackController controller = loader.getController();
        controller.initializeData(columnName, mutableTrackData, trackList, trackTableView, artistPlaylistListView,
                listViewLibrary, tableViewLibrary);
        stage.setTitle("Edit");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void okButtonClicked(MouseEvent mouseClick) throws IOException {
        final String artistName = "Artist Name";
        final String trackTitle = "Track Title";
        final String albumTitle = "Album Title";
        final String genre = "Genre";

        Stage stage = (Stage) okButton.getScene().getWindow();

        switch (columnName) {
            case artistName -> {
                String newArtistName = editTextField.getText();
                trackTableView.getSelectionModel().getSelectedItem().setArtistNameStr(newArtistName);

                if (!artistPlaylistListView.getItems().contains(newArtistName)) {
                    listViewLibrary.addArtist(newArtistName);
                    artistPlaylistListView.getItems().clear();
                    artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());
                }
            }

            case trackTitle -> {
                trackTableView.getSelectionModel().getSelectedItem().setTrackTitleStr(editTextField.getText());
            }

            case albumTitle -> {
                trackTableView.getSelectionModel().getSelectedItem().setAlbumTitleStr(editTextField.getText());
            }

            case genre -> {
                trackTableView.getSelectionModel().getSelectedItem().setTrackGenreStr(editTextField.getText());
            }
        }


        System.out.printf("Update %s %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(),
                columnName);

        tableViewLibrary.setTrackObservableList(trackList);

        stage.close();
    }

    @FXML
    private void cancelButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }
}
