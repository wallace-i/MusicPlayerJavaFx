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
    @FXML private TextField listViewTextInput;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private TableView<TrackMetadata> trackTableView;
    private ListView<String> artistListView;
    private ListView<String> playlistListView;
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

    private void initializeData(ListView<String> artistListView, ListView<String> playlistListView,
                                TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                TableViewLibrary tableViewLibrary, TrackIndex trackIndex,
                                String windowTitle, String menuSelection)
    {
        this.artistListView = artistListView;
        this.playlistListView = playlistListView;
        this.trackTableView = trackTableView;
        this.tableViewLibrary = tableViewLibrary;
        this.listViewLibrary = listViewLibrary;
        this.trackIndex = trackIndex;
        this.windowTitle = windowTitle;
        this.menuSelection = menuSelection;

        switch (windowTitle) {
            case addArtist -> listViewTextInput.setPromptText("add Artist");
            case createPlaylist -> listViewTextInput.setPromptText("create Playlist");
            case editArtist, editPlaylist -> listViewTextInput.setText(menuSelection);
        }

        listViewTextInput.setFocusTraversable(false);

    }

    public void showListViewInputWindow(ListView<String> artistListView, ListView<String> playlistListView,
                                        TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                        TableViewLibrary tableViewLibrary, TrackIndex trackIndex,
                                        String windowTitle, String menuSelection) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listview.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        ListViewController controller = loader.getController();
        controller.initializeData(artistListView, playlistListView, trackTableView, listViewLibrary, tableViewLibrary,
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
        userInput = listViewTextInput.getText();

        switch (windowTitle) {

            // Create playlist
            case createPlaylist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistObservableList().contains(userInput) ||
                            listViewLibrary.getPlaylistObservableList().contains(userInput)) {
                        throw new Exception();
                    }

                    listViewLibrary.addPlaylist(userInput);
                    playlistListView.setItems(listViewLibrary.getPlaylistObservableList());

                } catch (Exception e) {
                    listViewTextInput.clear();
                    listViewTextInput.setPromptText("Playlist must be unique");
                    listViewTextInput.setFocusTraversable(false);
                }
            }

            // Add Artist
            case addArtist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistObservableList().contains(userInput) ||
                            listViewLibrary.getPlaylistObservableList().contains(userInput)) {
                        throw new Exception();
                    }

                    listViewLibrary.addArtist(userInput);
                    artistListView.setItems(listViewLibrary.getArtistObservableList());
                    artistListView.refresh();

                } catch (Exception e) {
                    listViewTextInput.clear();
                    listViewTextInput.setPromptText("Artist must be unique");
                    listViewTextInput.setFocusTraversable(false);
                }
            }

            case editArtist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if ( listViewLibrary.getPlaylistObservableList().contains(userInput)) {
                        throw new Exception();
                    }

                    // Edit Artist
                    if (listViewLibrary.getArtistObservableList().contains(menuSelection)) {
                        System.out.println("Editing artist");
                        listViewLibrary.removeArtist(menuSelection);

                        if (!listViewLibrary.getArtistObservableList().contains(userInput)) {
                            listViewLibrary.addArtist(userInput);
                        }

                        artistListView.setItems(listViewLibrary.getArtistObservableList());

                        editArtist();
                    }

                } catch (Exception e) {
                    listViewTextInput.clear();
                    listViewTextInput.setPromptText("Name must be unique");
                    listViewTextInput.setFocusTraversable(false);

                }
            }

            case editPlaylist -> {
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistObservableList().contains(userInput) ||
                            listViewLibrary.getPlaylistObservableList().contains(userInput)) {
                        throw new Exception();
                    }

                    // Edit playlist
                    if (listViewLibrary.getPlaylistObservableList().contains(menuSelection)) {
                        System.out.println("Editing playlist");
                        listViewLibrary.removePlaylist(menuSelection);
                        listViewLibrary.addPlaylist(userInput);
                        playlistListView.setItems(listViewLibrary.getPlaylistObservableList());

                        editPlaylist();
                    }

                } catch (Exception e) {
                    listViewTextInput.clear();
                    listViewTextInput.setPromptText("Name must be unique");
                    listViewTextInput.setFocusTraversable(false);

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

            tableViewLibrary.setOutputTrackListOnClose();
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

            tableViewLibrary.setOutputTrackListOnClose();
        }
    }
}
