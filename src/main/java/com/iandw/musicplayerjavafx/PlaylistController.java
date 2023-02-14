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

    private void initializeData(ArrayList<String> playlistArray, ListView<String> artistNameListView,
                               ListViewLibrary listViewLibrary) {
        this.playlistArray = playlistArray;
        this.artistNameListView = artistNameListView;
        this.listViewLibrary = listViewLibrary;
        playlistNameTextInput.setPromptText("new playlist");
        playlistNameTextInput.setFocusTraversable(false);

    }

    public void showPlaylistInputWindow(ArrayList<String> playlistArray, ListView<String> artistNameListView,
                                        ListViewLibrary listViewLibrary) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playlistInput.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        PlaylistController controller = loader.getController();
        controller.initializeData(playlistArray, artistNameListView, listViewLibrary);
        stage.setTitle("Playlist");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void okButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        playlistArray.add(playlistNameTextInput.getText());
        PlaylistsFileIO.outputPlaylists(playlistArray);
        artistNameListView.getItems().clear();
        artistNameListView.setItems(listViewLibrary.loadListViewObservableList(playlistArray));
        stage.close();
    }

    @FXML
    private void cancelButtonClicked(MouseEvent mouseClick) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

}
