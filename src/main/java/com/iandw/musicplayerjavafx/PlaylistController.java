package com.iandw.musicplayerjavafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PlaylistController {
    @FXML private AnchorPane anchorPane;
    @FXML private TextField playlistNameTextInput;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    private ArrayList<String> playlistArray;
    private ListView<String> artistNameListView;
    private ListViewLibrary listViewLibrary;


    public void initialize() {}

    public void initializeData(ArrayList<String> playlistArray, ListView<String> artistNameListView,
                               ListViewLibrary listViewLibrary) {
        this.playlistArray = playlistArray;
        this.artistNameListView = artistNameListView;
        this.listViewLibrary = listViewLibrary;

    }

    public void showPlaylistInputWindow(ArrayList<String> playlistArray, ListView<String> artistNameListView,
                                        ListViewLibrary listViewLibrary) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playlistInput.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        PlaylistController controller = loader.getController();
        controller.initializeData(playlistArray, artistNameListView, listViewLibrary);
        stage.setTitle("Playlists");
        stage.setResizable(false);
        stage.show();

//        okButton.setOnKeyPressed(event -> {
//            if (event.getCode().equals(KeyCode.ENTER)) {
//                okButton.fire();
//            }
//        });
    }

    @FXML
    private void okButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        playlistArray.add(playlistNameTextInput.getText());
        PlaylistsFileIO.outputPlaylists(playlistArray);
        artistNameListView.getItems().clear();
        artistNameListView.setItems(listViewLibrary.loadArtistNameObservableList(playlistArray));
        stage.close();
    }

    @FXML
    private void cancelButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

}
