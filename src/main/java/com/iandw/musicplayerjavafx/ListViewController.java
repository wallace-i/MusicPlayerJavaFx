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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class ListViewController {
    @FXML private AnchorPane anchorPane;
    @FXML private TextField playlistNameTextInput;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private TableView<Track> trackTableView;
    private ListView<String> artistPlaylistListView;
    private ListViewLibrary listViewLibrary;
    private String windowTitle;
    private String userInput;
    private String menuSelection;
    final private String playlist = "Playlist";
    final private String artist = "Artist";
    final private String edit = "Edit";
    private int tableSize;

    public void initialize() {}

    private void initializeData(TableView<Track> trackTableView, ListView<String> artistPlaylistListView,
                                ListViewLibrary listViewLibrary, String windowTitle, String menuSelection,
                                int tableSize)
    {
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

    public void showListViewInputWindow(TableView<Track> trackTableView, ListView<String> artistPlaylistListView,
                                        ListViewLibrary listViewLibrary, String windowTitle, String menuSelection,
                                        int tableSize) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listview.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        ListViewController controller = loader.getController();
        controller.initializeData(trackTableView, artistPlaylistListView, listViewLibrary, windowTitle, menuSelection, tableSize);
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
            case playlist -> {
                // Add playlist
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

            case artist -> {
                // Add Artist
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
                // Add Artist
                try {
                    // Throw exception if playlist is the same name as an Artist
                    // otherwise may cause bugs when choosing to remove an artist or playlist
                    if (listViewLibrary.getArtistList().contains(userInput) ||
                            listViewLibrary.getPlaylistArray().contains(userInput)) {
                        throw new Exception();
                    }

                    // Edit playlist
                    if (listViewLibrary.getPlaylistArray().contains(menuSelection)) {

                    } else if (listViewLibrary.getArtistList().contains(menuSelection)) {
                        // Edit Artist
                        // Remove old artist, add user change
                        listViewLibrary.removeArtist(menuSelection);
                        listViewLibrary.addArtist(userInput);
                        artistPlaylistListView.getItems().clear();
                        artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

                        editArtist();

                        stage.close();

                    }




                } catch (Exception e) {
                    playlistNameTextInput.clear();
                    playlistNameTextInput.setPromptText("Name must be unique");
                    playlistNameTextInput.setFocusTraversable(false);
                }


            }
        }
    }

    @FXML
    private void cancelButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          EDIT / REMOVE MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private void editArtist() throws IOException {

        // Create new Artist Folder
        Utils.createDirectory(SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL()), userInput);

        String newArtistDirPathStr = SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL()) +
                File.separator + userInput;

        for (int trackIndex = 0; trackIndex < tableSize; trackIndex++) {
            String albumTitle = trackTableView.getItems().get(trackIndex).getAlbumTitleStr();

            if (albumTitle == null || Objects.equals(albumTitle, "")) {
                albumTitle = "Unknown";
            }

            String albumDirStr = newArtistDirPathStr + File.separator + albumTitle;

            // Create new albumFolder if album doesn't exist
            if (!Files.exists(Path.of(albumDirStr))) {
                Utils.createDirectory(albumDirStr, albumTitle);
            }

            // Move File to new artist/album folder
            String trackFileNameStr = trackTableView.getItems().get(trackIndex).getTrackFileNameStr();
            String source = trackTableView.getItems().get(trackIndex).getTrackPathStr();
            String destination = newArtistDirPathStr + File.separator + albumTitle + File.separator +
                    trackFileNameStr;

            //TODO => fix file move
            Utils.moveFile(source, destination);
            System.out.printf("Editing %s artist to %s%n", trackTableView.getItems().get(trackIndex).getTrackTitleStr(), userInput);

            // Update track metadata
            trackTableView.getItems().get(trackIndex).setArtistNameStr(userInput);
            trackTableView.getItems().get(trackIndex).setTrackPathStr(destination);
            trackTableView.refresh();
        }

    }

    public String getUserInput() { return userInput; }
}
