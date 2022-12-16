package com.iandw.musicplayerjavafx;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Parameter;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    @FXML private Button playButton;
    @FXML private Button pauseButton;
    @FXML private TextField trackNameTextField;
    @FXML private TextField albumNameTextField;
    private MediaPlayer mediaPlayer;
    private String currentPath;
    private String rootMusicDirectory;

    private String trackTitleString;
    private String artistNameString;

    private String albumTitleString;
    private boolean playing = false;

    public MusicPlayerController() {
    }

    public void initialize() throws IOException {
        // Load data from music folders into app
        artistNameListView.setItems(MusicLibrary.loadArtistNameCollection());
        rootMusicDirectory = MusicLibrary.getMusicRootDirectory();
//        if (trackNameString == null) {
//            trackNameString = MusicLibrary.getInitialFileName();
//        }

//        path = MusicLibrary.getLibraryPath() + '\\' + trackNameString + ".mp3";
//
//        Media media = new Media(new File(path).toURI().toString());
//        mediaPlayer = new MediaPlayer(media);
    }



    // Toggle media playback and the text on the playPauseButton
    @FXML
    private void playButtonPressed(ActionEvent mouseClick) {
        if (!playing) {
            mediaPlayer.play();
            playing = true;
        }
    }

    @FXML
    private void pauseButtonPressed(ActionEvent mouseClick) {
        if (playing) {
            mediaPlayer.stop();
            playing = false;
        }
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
                colTrackLength.setCellValueFactory(new PropertyValueFactory<Track, String>("trackLengthStr"));
                colTrackGenre.setCellValueFactory(new PropertyValueFactory<Track, String>("trackGenreStr"));
            }


        }
    }

    @FXML
    private void handleTableViewMouseClick(MouseEvent mouseClick) {

        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseClick.getClickCount() == 1) {
                albumTitleString = trackTableView.getSelectionModel().getSelectedItem().getAlbumTitleStr();
                trackTitleString = trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr();
                currentPath = rootMusicDirectory + '\\' + artistNameString + '\\' + albumTitleString +
                                '\\' + trackTitleString + ".mp3";
                //        System.out.printf("%nTrack selected: %s", trackNameString);
                //        System.out.printf("%nPath: %s", path);
            }
        }

        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseClick.getClickCount() == 2) {
                mediaPlayer.stop();
                trackTitleString = artistNameListView.getSelectionModel().getSelectedItem();
                trackNameTextField.setText(trackTitleString);
                currentPath = rootMusicDirectory + '\\' + trackTitleString + ".mp3";
                Media media = new Media(new File(currentPath).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                playing = true;

            }
        }
    }




}