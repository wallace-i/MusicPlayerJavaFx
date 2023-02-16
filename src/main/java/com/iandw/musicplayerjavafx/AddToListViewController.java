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

import java.io.IOException;
import java.util.Objects;

public class AddToListViewController {
    @FXML private AnchorPane anchorPane;
    @FXML private TextField playlistNameTextInput;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private ListView<String> artistPlaylistListView;
    private ListViewLibrary listViewLibrary;
    private String windowTitle;

    public void initialize() {}

    private void initializeData(ListView<String> artistPlaylistListView, ListViewLibrary listViewLibrary, String title) {
        this.artistPlaylistListView = artistPlaylistListView;
        this.listViewLibrary = listViewLibrary;
        this.windowTitle = title;

        if (Objects.equals(windowTitle, "Playlist")) {
            playlistNameTextInput.setPromptText("add Playlist");
        } else {
            playlistNameTextInput.setPromptText("add Artist");
        }

        playlistNameTextInput.setFocusTraversable(false);

    }

    public void showListViewInputWindow(ListView<String> artistPlaylistListView, ListViewLibrary listViewLibrary,
                                        String windowTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addtolistview.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        AddToListViewController controller = loader.getController();
        controller.initializeData(artistPlaylistListView, listViewLibrary, windowTitle);
        stage.setTitle(windowTitle);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void okButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        String userInput = playlistNameTextInput.getText();

        if (Objects.equals(windowTitle, "Playlist")) {
            // Add playlist
            try {
                // Throw exception if playlist is the same name as an Artist
                // otherwise may cause bugs when choosing to remove an artist or playlist
                if (listViewLibrary.getArtistList().contains(userInput) ||
                        listViewLibrary.getPlaylistArray().contains(userInput))
                {
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

        } else {
            // Add Artist
            try {
                // Throw exception if playlist is the same name as an Artist
                // otherwise may cause bugs when choosing to remove an artist or playlist
                if (listViewLibrary.getArtistList().contains(userInput) ||
                        listViewLibrary.getPlaylistArray().contains(userInput))
                {
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
    }

    @FXML
    private void cancelButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
