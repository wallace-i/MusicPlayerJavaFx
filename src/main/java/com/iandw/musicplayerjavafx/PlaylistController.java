package com.iandw.musicplayerjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PlaylistController {
    @FXML private AnchorPane anchorPane;
    @FXML private Button okButton;

    private String playlistInput;
    private ArrayList<String> playlistArray;

    public void initialize() {}

    public void initializeData(ArrayList<String> playlistArray) {
        this.playlistArray = playlistArray;

    }

    public void showPlaylistInputWindow(ArrayList<String> playlistArray) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playlistInput.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        PlaylistController controller = loader.getController();
        controller.initializeData(playlistArray);
        stage.setTitle("Settings");
        stage.setResizable(false);
        stage.show();
    }

    private void okButtonClicked() {

    }


}
