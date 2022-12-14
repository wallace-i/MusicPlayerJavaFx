package com.iandw.musicplayerjavafx;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class MusicPlayerController {
    @FXML private MediaView mediaView;
    @FXML private ListView<String> audioTracksListView;
    @FXML private Button playPauseButton;
    private MediaPlayer mediaPlayer;
    private String trackNameString;
    @FXML private TextField trackNameTextField;
    private String path;
    private boolean playing = false;

    public void initialize() throws IOException {
        // Load data from music folders into app
        MusicLibrary.loadData(audioTracksListView);

        if (trackNameString == null) {
            trackNameString = MusicLibrary.getInitialFileName();
        }

        path = MusicLibrary.getLibraryPath() + '\\' + trackNameString + ".mp3";

        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        //mediaPlayer.setAutoPlay(true);
        playing = true;
    }

    // Toggle media playback and the text on the playPauseButton
    @FXML
    private void playPauseButtonPressed(ActionEvent mouseClick) {
        playing = !playing;

        if (playing) {
            playPauseButton.setText("Pause");
            mediaPlayer.play();
        } else {
            playPauseButton.setText("Play");
            mediaPlayer.pause();
        }
    }

    @FXML
    private void handleMouseClick(MouseEvent mouseClick) {

        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseClick.getClickCount() == 1) {
                trackNameString = audioTracksListView.getSelectionModel().getSelectedItem();
                path = MusicLibrary.getLibraryPath() + '\\' + trackNameString + ".mp3";
                //        System.out.printf("%nTrack selected: %s", trackNameString);
                //        System.out.printf("%nPath: %s", path);
            }
        }

        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseClick.getClickCount() == 2) {
                mediaPlayer.stop();
                trackNameString = audioTracksListView.getSelectionModel().getSelectedItem();
                trackNameTextField.setText(trackNameString);
                path = MusicLibrary.getLibraryPath() + '\\' + trackNameString + ".mp3";
                Media media = new Media(new File(path).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
            }
        }
    }




}