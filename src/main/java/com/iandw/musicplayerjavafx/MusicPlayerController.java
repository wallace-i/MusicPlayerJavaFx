/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: MusicPlayerController
 *      Notes: Contains FXML member variables and controls program interface
 */
package com.iandw.musicplayerjavafx;

import java.io.File;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;



public class MusicPlayerController {
    @FXML private MediaView mediaView;
    @FXML private ListView<String> artistNameListView;
    @FXML private TableView<Track> trackTableView;
    @FXML private TableColumn<Track, String> colTrackTitle;
    @FXML public TableColumn<Track, String> colAlbumTitle;
    @FXML public TableColumn<Track, String> colTrackLength;
    @FXML public TableColumn<Track, String> colTrackGenre;
    @FXML private Button playPauseButton;
    @FXML private Button stopButton;
    @FXML private Slider volumeSlider;

    @FXML private Slider trackDurationSlider;
    @FXML private TextField trackTitleTextField;
    @FXML private TextField artistNameTextField;
    @FXML private Label trackCurrentTimeLabel;
    @FXML private Label trackDurationLabel;
    private MediaPlayer mediaPlayer;
    private Media media;
    private String currentPath;
    private String rootMusicDirectory;

    private String trackTitleString;
    private String artistNameString;

    private boolean playing = false;


    public void initialize() throws IOException {
        // Load data from music folders into app
        artistNameListView.setItems(MusicLibrary.loadArtistNameCollection());
        rootMusicDirectory = MusicLibrary.getMusicRootDirectory();

        // listener for changes to volumeSlider's value
        volumeSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> mediaPlayer.setVolume(newValue.doubleValue())
        );

        // listener for changes to trackDurationSlider's value
        trackDurationSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    trackDurationLabel.setText(trackTableView.getSelectionModel().getSelectedItem().getTrackDurationStr());
                    trackCurrentTimeLabel.setText(mediaPlayer.getCurrentTime().toString());
                }
        );

    }

    @FXML
    private void handleListViewMouseClick(MouseEvent mouseClick) throws IOException {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseClick.getClickCount() == 2) {

                artistNameString = artistNameListView.getSelectionModel().getSelectedItem();
                currentPath = rootMusicDirectory + '\\' + artistNameString;

                trackTableView.setEditable(true);
                trackTableView.setItems(ArtistLibrary.loadArtistTableView(currentPath));
                trackTableView.setVisible(true);

                colTrackTitle.setCellValueFactory(new PropertyValueFactory<Track, String>("trackTitleStr"));
                colAlbumTitle.setCellValueFactory(new PropertyValueFactory<Track, String>("albumTitleStr"));
                colTrackLength.setCellValueFactory(new PropertyValueFactory<Track, String>("trackDurationStr"));
                colTrackGenre.setCellValueFactory(new PropertyValueFactory<Track, String>("trackGenreStr"));

                System.out.printf("currentPath: %s%n", currentPath);

            }
        }
    }

    @FXML
    private void handleTableViewMouseClick(MouseEvent mouseClick) {

        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {

            String albumDirectoryString;
            //String trackContainerType;
            String trackFileName;

            if (mouseClick.getClickCount() == 1) {
                trackFileName = trackTableView.getSelectionModel().getSelectedItem().getTrackFileNameStr();
                trackTitleString = trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr();
                //trackContainerType = trackTableView.getSelectionModel().getSelectedItem().getTrackContainerTypeStr();
                albumDirectoryString = trackTableView.getSelectionModel().getSelectedItem().getAlbumDirectoryStr();
                currentPath = rootMusicDirectory + '\\' + artistNameString + '\\' + albumDirectoryString +
                        '\\' + trackFileName;

//                media = new Media(new File(currentPath).toURI().toString());
//                mediaPlayer = new MediaPlayer(media);

            }

            if (mouseClick.getClickCount() == 2) {

                if (playing) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                    playing = false;
                }

                trackFileName = trackTableView.getSelectionModel().getSelectedItem().getTrackFileNameStr();
                trackTitleString = trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr();
                //trackContainerType = trackTableView.getSelectionModel().getSelectedItem().getTrackContainerTypeStr();
                albumDirectoryString = trackTableView.getSelectionModel().getSelectedItem().getAlbumDirectoryStr();
                currentPath = rootMusicDirectory + '\\' + artistNameString + '\\' + albumDirectoryString +
                        '\\' + trackFileName;

                trackTitleTextField.setText(trackTitleString);
                artistNameTextField.setText(artistNameString);

                System.out.printf("currentPath: %s%n", currentPath);
                media = new Media(new File(currentPath).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                playPauseButton.setText("Pause");
                playing = true;

            }
        }
    }

    // Toggle media playback and the text on the playPauseButton
    @FXML
    private void playPauseButtonPressed(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            if (playing) {
                mediaPlayer.pause();
                playPauseButton.setText("Play");
                playing = false;
            } else if (this.mediaPlayer == null){
                trackTitleTextField.setText(trackTitleString);
                artistNameTextField.setText(artistNameString);
                System.out.printf("currentPath: %s%n", currentPath);
                media = new Media(new File(currentPath).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                playPauseButton.setText("Pause");
                playing = true;
            } else {
                trackTitleTextField.setText(trackTitleString);
                artistNameTextField.setText(artistNameString);
                mediaPlayer.play();
                playPauseButton.setText("Pause");
                playing = true;

            }
        }
    }

    @FXML
    private void stopButtonPressed(MouseEvent mouseClick) {
        if (playing && (artistNameString == artistNameListView.getSelectionModel().getSelectedItem())) {
            mediaPlayer.stop();
            playPauseButton.setText("Play");
            playing = false;
        } else if (playing) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            playPauseButton.setText("Play");
            playing = false;
        }
    }

    @FXML
    private void setVolumeSlider(MouseEvent mouseDrag) {

    }




}