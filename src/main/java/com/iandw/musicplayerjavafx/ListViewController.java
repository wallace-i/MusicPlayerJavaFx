package com.iandw.musicplayerjavafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

public class ListViewController {
    @FXML private AnchorPane anchorPane;
    @FXML private TextField playlistNameTextInput;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private TableView<Track> trackTableView;
    private ListView<String> artistPlaylistListView;
    private ListViewLibrary listViewLibrary;
    private ObservableList<Track> trackList;
    private TableViewLibrary tableViewLibrary;
    private String windowTitle;
    private String userInput;
    private String menuSelection;
    final private String playlist = "Playlist";
    final private String artist = "Artist";
    final private String edit = "Edit";
    private int tableSize;

    public void initialize() {}

    private void initializeData(ObservableList<Track> trackList, TableViewLibrary tableViewLibrary,
                                TableView<Track> trackTableView, ListView<String> artistPlaylistListView,
                                ListViewLibrary listViewLibrary, String windowTitle, String menuSelection,
                                int tableSize)
    {
        this.trackList = trackList;
        this.tableViewLibrary = tableViewLibrary;
        this.trackTableView = trackTableView;
        this.artistPlaylistListView = artistPlaylistListView;
        this.listViewLibrary = listViewLibrary;
        this.windowTitle = windowTitle;
        this.menuSelection = menuSelection;
        this.tableSize = tableSize;

        switch (windowTitle) {
            case playlist -> {
                playlistNameTextInput.setPromptText("create Playlist");
            }
            case artist -> {
                playlistNameTextInput.setPromptText("add Artist");
            }
            case edit -> {
                playlistNameTextInput.setText(menuSelection);
            }
        }

        playlistNameTextInput.setFocusTraversable(false);

    }

    public void showListViewInputWindow(ObservableList<Track> trackList, TableViewLibrary tableViewLibrary,
                                        TableView<Track> trackTableView, ListView<String> artistPlaylistListView,
                                        ListViewLibrary listViewLibrary, String windowTitle, String menuSelection,
                                        int tableSize) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listview.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        ListViewController controller = loader.getController();
        controller.initializeData(trackList, tableViewLibrary, trackTableView, artistPlaylistListView,
                listViewLibrary, windowTitle, menuSelection, tableSize);
        stage.setTitle(windowTitle);
        stage.setResizable(false);
        stage.show();
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          OK / CANCEL BUTTONS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    private void okButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        this.userInput = playlistNameTextInput.getText();

        switch (windowTitle) {

            // Add playlist
            case playlist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistList().contains(userInput) ||
                            listViewLibrary.getPlaylistArray().contains(userInput)) {
                        throw new Exception();
                    }

                    listViewLibrary.addPlaylist(userInput);
                    artistPlaylistListView.getItems().clear();
                    artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

                    stage.close();

                } catch (Exception e) {
                    playlistNameTextInput.clear();
                    playlistNameTextInput.setPromptText("Playlist must be unique");
                    playlistNameTextInput.setFocusTraversable(false);
                }
            }

            // Add Artist
            case artist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistList().contains(userInput) ||
                            listViewLibrary.getPlaylistArray().contains(userInput)) {
                        throw new Exception();
                    }

                    listViewLibrary.addArtist(userInput);
                    artistPlaylistListView.getItems().clear();
                    artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

                    stage.close();

                } catch (Exception e) {
                    playlistNameTextInput.clear();
                    playlistNameTextInput.setPromptText("Artist must be unique");
                    playlistNameTextInput.setFocusTraversable(false);
                }
            }

            case edit -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistList().contains(userInput) ||
                            listViewLibrary.getPlaylistArray().contains(userInput)) {
                        throw new Exception();
                    }

                    // Edit playlist
                    if (listViewLibrary.getPlaylistArray().contains(menuSelection)) {
                        listViewLibrary.removePlaylist(menuSelection);
                        listViewLibrary.addPlaylist(userInput);
                        artistPlaylistListView.getItems().clear();
                        artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

                        //TODO => edit playlist wipes out tracklist
                        editPlaylist();

                    } else if (listViewLibrary.getArtistList().contains(menuSelection)) {
                        // Edit Artist
                        listViewLibrary.removeArtist(menuSelection);
                        listViewLibrary.addArtist(userInput);
                        artistPlaylistListView.getItems().clear();
                        artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

                        editArtist();
                    }

                } catch (Exception e) {
                    playlistNameTextInput.clear();
                    playlistNameTextInput.setPromptText("Name must be unique");
                    playlistNameTextInput.setFocusTraversable(false);

                }
            }
        }

        stage.close();
    }


    private void editArtist() {
        if (tableSize > 0) {
            for (int trackIndex = 0; trackIndex < tableSize; trackIndex++) {
                System.out.printf("Editing %s artist to %s%n", trackTableView.getItems().get(trackIndex).getTrackTitleStr(),
                        userInput);
                trackTableView.getItems().get(trackIndex).setArtistNameStr(userInput);
                trackTableView.refresh();
            }

            // Write to file
            try {
                trackList.clear();
                trackList.setAll(tableViewLibrary.getTrackObservableList());
                TracklistFileIO.outputTrackObservableList(trackList);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void editPlaylist() {
        if (tableSize > 0) {
            for (int trackIndex = 0; trackIndex < tableSize; trackIndex++) {
                System.out.printf("Editing %s playlist to %s%n", trackTableView.getItems().get(trackIndex).getTrackTitleStr(),
                        userInput);
                trackTableView.getItems().get(trackIndex).setPlaylistStr(userInput);
                trackTableView.refresh();
            }

            // Write to file
            try {
                trackList.clear();
                trackList.setAll(tableViewLibrary.getTrackObservableList());
                TracklistFileIO.outputTrackObservableList(trackList);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void cancelButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
