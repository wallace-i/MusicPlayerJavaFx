package com.iandw.musicplayerjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class AddToListViewController {
    @FXML private AnchorPane anchorPane;
    @FXML private TextField playlistNameTextInput;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private ArrayList<String> playlistArray;
    private ListView<String> artistNameListView;
    private ListViewLibrary listViewLibrary;
    private boolean addPlaylist;


    public void initialize() {}

    private void initializeData(ArrayList<String> playlistArray, ListView<String> artistNameListView,
                               ListViewLibrary listViewLibrary) {
        this.playlistArray = playlistArray;
        this.artistNameListView = artistNameListView;
        this.listViewLibrary = listViewLibrary;
        if (addPlaylist) {
            playlistNameTextInput.setPromptText("add playlist");
        } else {
            playlistNameTextInput.setPromptText("add Artist");
        }

        playlistNameTextInput.setFocusTraversable(false);

    }

    public void showPlaylistInputWindow(ArrayList<String> playlistArray, ListView<String> artistNameListView,
                                        ListViewLibrary listViewLibrary) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addtolistview.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        AddToListViewController controller = loader.getController();
        addPlaylist = true;
        controller.initializeData(playlistArray, artistNameListView, listViewLibrary);
        stage.setTitle("Playlist");
        stage.setResizable(false);
        stage.show();
    }

    public void showArtistInputWindow(ArrayList<String> playlistArray, ListView<String> artistNameListView,
                                        ListViewLibrary listViewLibrary) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addtolistview.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        AddToListViewController controller = loader.getController();
        addPlaylist = false;
        controller.initializeData(playlistArray, artistNameListView, listViewLibrary);
        stage.setTitle("Artist");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void okButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        String userInput = playlistNameTextInput.getText();

        if (addPlaylist) {
            // Add playlist
            try {


                // Throw exception if playlist is the same name as an Artist
                // otherwise may cause bugs when choosing to remove an artist or playlist
                if (listViewLibrary.getArtistList().contains(userInput) || playlistArray.contains(userInput)) {
                    throw new Exception();
                }

                playlistArray.add(userInput);
                Collections.sort(playlistArray);
                PlaylistsFileIO.outputPlaylists(playlistArray);
                artistNameListView.getItems().clear();
                artistNameListView.setItems(listViewLibrary.loadListViewObservableList(playlistArray));
                stage.close();

            } catch (Exception e) {
                playlistNameTextInput.clear();
                playlistNameTextInput.setPromptText("Playlist must be unique");
                playlistNameTextInput.setFocusTraversable(false);
            }

        } else {
            // Add Artist
            try {
                // Throw exception if playlist is the same name as an Artist
                // otherwise may cause bugs when choosing to remove an artist or playlist
                if (listViewLibrary.getArtistList().contains(userInput) || playlistArray.contains(userInput)) {
                    throw new Exception();
                }

                listViewLibrary.addArtist(userInput);
                artistNameListView.getItems().clear();
                artistNameListView.setItems(listViewLibrary.loadListViewObservableList(playlistArray));

                stage.close();

            } catch (Exception e) {
                playlistNameTextInput.clear();
                playlistNameTextInput.setPromptText("Artist must be unique");
                playlistNameTextInput.setFocusTraversable(false);
            }

        }


    }

    @FXML
    private void cancelButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

}
