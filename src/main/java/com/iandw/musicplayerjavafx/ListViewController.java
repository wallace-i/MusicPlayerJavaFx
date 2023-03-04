package com.iandw.musicplayerjavafx;

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
import java.io.FileNotFoundException;
import java.io.IOException;

public class ListViewController {
    @FXML private AnchorPane anchorPane;
    @FXML private TextField playlistNameTextInput;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private TableView<TrackMetadata> trackTableView;
    private ListView<String> artistsListView;
    private ListView<String> playlistsListView;
    private ListViewLibrary listViewLibrary;
    private TableViewLibrary tableViewLibrary;
    private TrackIndex trackIndex;
    private String windowTitle;
    private String userInput;
    private String menuSelection;
    final private String addArtist = "Add Artist";
    final private String editArtist = "Edit Artist";
    final private String createPlaylist = "Create Playlist";
    final private String editPlaylist = "Edit Playlist";

    public void initialize() {}

    private void initializeData(ListView<String> artistsListView, ListView<String> playlistsListView,
                                TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                TableViewLibrary tableViewLibrary, TrackIndex trackIndex,
                                String windowTitle, String menuSelection)
    {
        this.artistsListView = artistsListView;
        this.playlistsListView = playlistsListView;
        this.trackTableView = trackTableView;
        this.tableViewLibrary = tableViewLibrary;
        this.listViewLibrary = listViewLibrary;
        this.trackIndex = trackIndex;
        this.windowTitle = windowTitle;
        this.menuSelection = menuSelection;

        switch (windowTitle) {
            case addArtist -> playlistNameTextInput.setPromptText("add Artist");
            case createPlaylist -> playlistNameTextInput.setPromptText("create Playlist");
            case editArtist, editPlaylist -> playlistNameTextInput.setText(menuSelection);
        }

        playlistNameTextInput.setFocusTraversable(false);

    }

    public void showListViewInputWindow(ListView<String> artistsListView, ListView<String> playlistsListView,
                                        TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                        TableViewLibrary tableViewLibrary, TrackIndex trackIndex,
                                        String windowTitle, String menuSelection) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listview.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        ListViewController controller = loader.getController();
        controller.initializeData(artistsListView, playlistsListView, trackTableView, listViewLibrary, tableViewLibrary,
                trackIndex, windowTitle, menuSelection);
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

            // Create playlist
            case createPlaylist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistObservableList().contains(userInput) ||
                            listViewLibrary.getPlaylistsObservableList().contains(userInput)) {
                        throw new Exception();
                    }

                    listViewLibrary.addPlaylist(userInput);
                    playlistsListView.getItems().clear();
                    playlistsListView.setItems(listViewLibrary.getPlaylistsObservableList());
                    playlistsListView.refresh();

                } catch (Exception e) {
                    playlistNameTextInput.clear();
                    playlistNameTextInput.setPromptText("Playlist must be unique");
                    playlistNameTextInput.setFocusTraversable(false);
                }
            }

            // Add Artist
            case addArtist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistObservableList().contains(userInput) ||
                            listViewLibrary.getPlaylistsObservableList().contains(userInput)) {
                        throw new Exception();
                    }

                    listViewLibrary.addArtist(userInput);
                    artistsListView.getItems().clear();
                    artistsListView.setItems(listViewLibrary.getArtistObservableList());
                    artistsListView.refresh();

                } catch (Exception e) {
                    playlistNameTextInput.clear();
                    playlistNameTextInput.setPromptText("Artist must be unique");
                    playlistNameTextInput.setFocusTraversable(false);
                }
            }

            case editArtist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistObservableList().contains(userInput) ||
                            listViewLibrary.getPlaylistsObservableList().contains(userInput)) {
                        throw new Exception();
                    }

                    // Edit Artist
                    if (listViewLibrary.getArtistObservableList().contains(menuSelection)) {
                        System.out.println("Editing artist");
                        listViewLibrary.removeArtist(menuSelection);
                        listViewLibrary.addArtist(userInput);
                        artistsListView.getItems().clear();
                        artistsListView.setItems(listViewLibrary.getArtistObservableList());

                        editArtist();
                    }

                } catch (Exception e) {
                    playlistNameTextInput.clear();
                    playlistNameTextInput.setPromptText("Name must be unique");
                    playlistNameTextInput.setFocusTraversable(false);

                }
            }

            case editPlaylist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistObservableList().contains(userInput) ||
                            listViewLibrary.getPlaylistsObservableList().contains(userInput)) {
                        throw new Exception();
                    }

                    // Edit playlist
                    if (listViewLibrary.getPlaylistsObservableList().contains(menuSelection)) {
                        System.out.println("Editing playlist");
                        listViewLibrary.removePlaylist(menuSelection);
                        listViewLibrary.addPlaylist(userInput);
                        playlistsListView.getItems().clear();
                        playlistsListView.setItems(listViewLibrary.getPlaylistsObservableList());

                        editPlaylist();
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

    @FXML
    private void cancelButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          EDIT PLAYLIST / ARTIST MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private void editPlaylist() {
        int tableSize = trackIndex.getTableSize();

        if (tableSize > 0) {
            for (int trackIndex = 0; trackIndex < tableSize; trackIndex++) {
                System.out.printf("Editing %s playlist to %s%n",
                        trackTableView.getItems().get(trackIndex).getTrackTitleStr(), userInput);
                trackTableView.getItems().get(trackIndex).setPlaylistStr(userInput);
                trackTableView.refresh();
            }

            try {
                // Update trackList and Write to file
                tableViewLibrary.outputTrackObservableList();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void editArtist() {
        int tableSize = trackIndex.getTableSize();

        if (tableSize > 0) {
            for (int trackIndex = 0; trackIndex < tableSize; trackIndex++) {
                System.out.printf("Editing %s artist to %s%n",
                        trackTableView.getItems().get(trackIndex).getTrackTitleStr(), userInput);
                trackTableView.getItems().get(trackIndex).setArtistNameStr(userInput);
                trackTableView.refresh();
            }


            try {
                // Update trackList and Write to file
                tableViewLibrary.outputTrackObservableList();

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
