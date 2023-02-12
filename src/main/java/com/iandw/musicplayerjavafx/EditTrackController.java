package com.iandw.musicplayerjavafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class EditTrackController {
    @FXML
    private AnchorPane anchorPane;
    @FXML private TextField editTextField;
    @FXML private Label trackDataLabel;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private ListView<String> artistNameListView;
    private ListViewLibrary listViewLibrary;
    private TableViewLibrary tableViewLibrary;
    private TableView<Track> trackTableView;
    private ObservableList<Track> trackList;
    private String columnName;
    private String mutableTrackData;

    private void initializeData( String columnName, String mutableTrackData, ObservableList<Track> trackList,
                                TableView<Track> trackTableView, TableViewLibrary tableViewLibrary)
    {
        this.columnName = columnName;
        this.trackList = trackList;
        this.trackTableView = trackTableView;
        this.tableViewLibrary = tableViewLibrary;
        editTextField.setText(mutableTrackData);
        editTextField.setFocusTraversable(false);
    }

    private void initializeDataArtistName(String columnName, String mutableTrackData, ListView<String> artistNameListView) {
        this.columnName = columnName;
        this.artistNameListView = artistNameListView;
        editTextField.setText(mutableTrackData);
        editTextField.setFocusTraversable(false);
    }

    public void showEditTrackWindow(String columnName, String mutableTrackData, ObservableList<Track> trackList,
                                    TableView<Track> trackTableView, TableViewLibrary tableViewLibrary) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("edittrack.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        EditTrackController controller = loader.getController();
        controller.initializeData(columnName, mutableTrackData, trackList, trackTableView, tableViewLibrary);
        stage.setTitle("Edit");
        stage.setResizable(false);
        stage.show();
    }

    public void showEditArtistWindow(String columnName, String mutableTrackData, ListView<String> artistNameListView) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("edittrack.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        EditTrackController controller = loader.getController();
        controller.initializeDataArtistName(columnName, mutableTrackData, artistNameListView);
        stage.setTitle("Edit");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void okButtonClicked(MouseEvent mouseClick) throws FileNotFoundException {
        final String artistName = "Artist Name";
        final String trackTitle = "Track Title";
        final String albumTitle = "Album Title";
        final String genre = "Genre";

        Stage stage = (Stage) okButton.getScene().getWindow();

        switch (columnName) {
            case artistName -> {
                String newArtistName = editTextField.getText();
                trackTableView.getSelectionModel().getSelectedItem().setArtistNameStr(newArtistName);

                if (!artistNameListView.getItems().contains(newArtistName)) {
                    artistNameListView.getItems().add(newArtistName);

                    //TODO => finish artist list edit

                    // Load user playlists into listview
//                    artistNameObservableList.add("------- Playlists -------");
//
//                    if (playlistArray.isEmpty()) {
//                        artistNameObservableList.add("* no playlists *");
//                    } else {
//                        artistNameObservableList.addAll(playlistArray);
//                    }
//                    artistNameObservableList.add("------- Artists ---------");
//
//                    // Get artist names from artist folders
//                    inputArtistNameObservableList();

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
