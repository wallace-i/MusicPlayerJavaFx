/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: MusicPlayerController
 *      Notes: Contains FXML member variables and controls program interface
 */
package com.iandw.musicplayerjavafx;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayerController {
    @FXML
    private ListView<String> artistNameListView;
    @FXML
    private TableView<Track> trackTableView;
    @FXML
    private TableColumn<Track, String> colTrackTitle;
    @FXML
    public TableColumn<Track, String> colAlbumTitle;
    @FXML
    public TableColumn<Track, String> colTrackLength;
    @FXML
    public TableColumn<Track, String> colTrackGenre;
    @FXML
    private Button playPauseButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider seekSlider;
    @FXML
    private Label playingLabel;
    @FXML
    private Label albumLabel;
    @FXML
    private Label byLabel;
    @FXML
    private Label trackCurrentTimeLabel;
    @FXML
    private Label trackDurationLabel;
    @FXML
    private Label volumeLabel;
    @FXML
    private RadioButton autoPlay;
    @FXML
    private RadioButton shuffle;
    @FXML
    private RadioButton repeat;
    @FXML
    private CheckBox mute;
    private MediaPlayer mediaPlayer;
    private String currentPath;
    private String trackTitleString;
    private String artistNameString;
    private String albumTitleString;
    private String previousArtistNameString;
    private String rootMusicDirectoryString;
    private int currentTrackIndex;
    private int nextTrackIndex;
    private int previousTrackIndex;
    private int tableSize;
    private double volumeDouble;
    private boolean playing = false;
    private boolean stopped = true;
    List<Integer> shuffleArray = new ArrayList<>();
    String settingsURL = SettingsURL.getSettingsURL();

    public void initialize() throws IOException {
        // Get JSON file URL
        System.out.println(settingsURL);

        // Initialize root directory path for controller
        rootMusicDirectoryString = JsonReadWrite.readMusicDirectoryString(settingsURL);

        // Load data from music folders into app
        artistNameListView.setItems(MusicLibrary.loadArtistNameCollection(rootMusicDirectoryString));
        previousTrackIndex = 0;
        artistNameString = "";
        volumeDouble = 0.25;


        // listener for changes to volumeSlider's value
        volumeSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    int volumeInt = newValue.intValue();
                    volumeLabel.setText(Integer.toString(volumeInt));

                    try {
                        volumeDouble = Math.pow(newValue.doubleValue(), 2) / 10000;
                        mediaPlayer.setVolume(volumeDouble);
                    } catch (NullPointerException e) {
                        System.out.println("mediaPlayer is null");
                    }
                }
        );

        // Mute checkbox
        mute.selectedProperty().addListener(
                (observableValue, oldValue, newValue) -> mediaPlayer.setMute(newValue)
        );

        // Seek time during track duration, and updating current duration on seekSlider
        seekSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (seekSlider.isPressed()) {
                        mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(seekSlider.getValue() / 100));
                    }

                    double percentage = 100.0 * newValue.doubleValue() / seekSlider.getMax();
                    if (Double.isNaN(percentage)) {
                        percentage = 0.0;
                    }
                    String style = String.format(
                            "-track-color: linear-gradient(to right, " +
                                    "-fx-accent 0%%, " +
                                    "-fx-accent %1$.1f%%, " +
                                    "-default-track-color %1$.1f%%, " +
                                    "-default-track-color 100%%);",
                            percentage);
                    //System.out.println(percentage);
                    seekSlider.setStyle(style);
                }
        );

        // Toggle Group Listeners Logic
        autoPlay.selectedProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        if (shuffle.isSelected() || repeat.isSelected()) {
                            shuffle.setSelected(false);
                            repeat.setSelected(false);
                            autoPlay.setSelected(true);
                        } else {
                            autoPlay.setSelected(true);
                        }
                    } else {
                        autoPlay.setSelected(false);
                        shuffleArray.clear();
                    }
                }
        );

        shuffle.selectedProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        if (autoPlay.isSelected() || repeat.isSelected()) {
                            shuffle.setSelected(true);
                            repeat.setSelected(false);
                            autoPlay.setSelected(false);
                        } else {
                            shuffle.setSelected(true);
                        }
                    } else {
                        shuffle.setSelected(false);

                    }
                }
        );

        repeat.selectedProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        if (shuffle.isSelected() || autoPlay.isSelected()) {
                            shuffle.setSelected(false);
                            repeat.setSelected(true);
                            autoPlay.setSelected(false);
                        } else {
                            repeat.setSelected(true);
                        }
                    } else {
                        repeat.setSelected(false);
                    }
                }
        );

    }

    @FXML
    private void handleListViewMouseClick(MouseEvent mouseClick) throws IOException {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseClick.getClickCount() == 2) {
                rootMusicDirectoryString = JsonReadWrite.readMusicDirectoryString(settingsURL);
                previousArtistNameString = artistNameString;
                artistNameString = artistNameListView.getSelectionModel().getSelectedItem();
                currentPath = rootMusicDirectoryString + File.separator + artistNameString;
                trackTableView.setEditable(true);
                trackTableView.getSortOrder().add(colAlbumTitle);
                trackTableView.setItems(ArtistLibrary.loadArtistTableView(currentPath));
                trackTableView.setVisible(true);
                tableSize = ArtistLibrary.getTableSize();

                colTrackTitle.setCellValueFactory(new PropertyValueFactory<>("trackTitleStr"));
                colAlbumTitle.setCellValueFactory(new PropertyValueFactory<>("albumTitleStr"));
                colTrackLength.setCellValueFactory(new PropertyValueFactory<>("trackDurationStr"));
                colTrackGenre.setCellValueFactory(new PropertyValueFactory<>("trackGenreStr"));

//              Debugger
//              System.out.printf("currentPath: %s%n", currentPath);

            }
        }

        if (mouseClick.getButton().equals(MouseButton.SECONDARY)) {
            System.out.println("right click");
            // TODO => fix right click
//            ContextMenu contextMenu = new ContextMenu();
//            MenuItem item1 = new MenuItem("Open in Explorer");
//            MenuItem item2 = new MenuItem("Rename");
//            contextMenu.getItems().addAll(item1, item2);
//            item1.setOnAction(event -> System.out.println("Open"));
//            item2.setOnAction(event -> System.out.println("Rename"));

        }

    }

    @FXML
    private void handleTableViewMouseClick(MouseEvent mouseClick) {

        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseClick.getClickCount() == 1 && !mouseClick.isDragDetect()) {
                filePath();
            }

            if (mouseClick.getClickCount() == 2) {
                if (playing) {
                    mediaPlayer.stop();
                    mediaPlayer.dispose();
                    playing = false;
                }
                // Load currentPath and associated variables
                playMedia();

            }
        }
    }

    // Toggle media playback and the text on the playPauseButton
    @FXML
    private void playPauseButtonPressed(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            try {
                if (playing) {
                    mediaPlayer.pause();
                    playPauseButton.setText("Play");
                    playing = false;
                } else if (this.mediaPlayer == null) {
                    playMedia();
                } else {
                    mediaPlayer.play();
                    playPauseButton.setText("Pause");
                    playing = true;
                    stopped = false;
                    setNowPlayingText();

                }
            } catch (NullPointerException e) {
                System.out.println("No track selected.");
            }
        }
    }

    @FXML
    private void seekSliderPressed(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            try {
                if (!playing) {
                    mediaPlayer.play();
                    playPauseButton.setText("Pause");
                } else if (this.mediaPlayer == null) {
                    playMedia();
                }
            } catch (NullPointerException e) {
                System.out.println("No track selected.");
            }
        }

    }

    @FXML
    private void stopButtonPressed(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            try {
                if (playing && (Objects.equals(artistNameString, artistNameListView.getSelectionModel().getSelectedItem()))) {
                    stopMedia(false);
                } else {
                    stopMedia(playing);
                }
            } catch (NullPointerException e) {
                System.out.println("No track selected.");
            }
        }
    }

    @FXML
    private void nextButtonPressed(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY) && mediaPlayer != null) {
            if (shuffle.isSelected()) {
                shuffleSelected();
            } else if (trackTableView.getSelectionModel().getSelectedItem() != null) {
                trackTableView.getSelectionModel().select(nextTrackIndex);
                stopMedia(true);
                playMedia();
            }
        }
    }

    @FXML
    private void previousButtonPressed(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY) && mediaPlayer != null &&
        trackTableView.getSelectionModel().getSelectedItem() != null) {
            trackTableView.getSelectionModel().select(previousTrackIndex);
            stopMedia(true);
            playMedia();
        }
    }

    private void playMedia() {
        // Update current path for media object
        filePath();

        // Debugger
        System.out.printf("currentPath: %s%n", currentPath);

        // Create Media Object
        Media media = new Media(new File(currentPath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        // Track current track index for prev, next and autoplay functions
        trackIndexTracker();

        // Set Seeker slider
        mediaPlayer.currentTimeProperty().addListener(observable -> seekValueUpdate());

        // Play media
        mediaPlayer.setVolume(volumeDouble);
        mediaPlayer.play();
        playPauseButton.setText("Pause");
        playing = true;
        stopped = false;

        // Set text to currently playing text fields
        setNowPlayingText();

        // Autoplay or stop media player after current track is finished
        mediaPlayer.setOnEndOfMedia(() -> {
            if (trackTableView.getSelectionModel().getSelectedItem() != null) {
                if (autoPlay.isSelected()) {
                    autoPlaySelected();
                } else if (repeat.isSelected()) {
                    repeatSelected();
                } else if (shuffle.isSelected()) {
                    shuffleSelected();
                } else {
                    stopMedia(false);
                }
            } else {
                stopMedia(true);
            }
        });
    }

    private void stopMedia(boolean dispose) {
        if (dispose) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            playPauseButton.setText("Play");
            playing = false;
            stopped = true;
            setNowPlayingText();
        } else {
            mediaPlayer.stop();
            playPauseButton.setText("Play");
            playing = false;
            stopped = true;
            setNowPlayingText();
        }
    }

    private void filePath() {
        String trackFileName = trackTableView.getSelectionModel().getSelectedItem().getTrackFileNameStr();
        trackTitleString = trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr();
        albumTitleString = trackTableView.getSelectionModel().getSelectedItem().getAlbumTitleStr();
        String albumDirectoryString = trackTableView.getSelectionModel().getSelectedItem().getAlbumDirectoryStr();
        currentPath = rootMusicDirectoryString + File.separator + artistNameString + File.separator + albumDirectoryString +
                File.separator + trackFileName;
    }

    private void seekValueUpdate() {
        if (mediaPlayer == null) {
            trackDurationLabel.setText("");
            trackCurrentTimeLabel.setText("");
        }
        trackDurationLabel.setText(Track.formatSeconds((int) mediaPlayer.getTotalDuration().toSeconds()));
        trackCurrentTimeLabel.setText(Track.formatSeconds((int) mediaPlayer.getCurrentTime().toSeconds()));
        seekSlider.valueProperty().setValue(mediaPlayer.getCurrentTime().toMillis() /
                mediaPlayer.getTotalDuration().toMillis() * 100);
    }

    private void setNowPlayingText() {
        if (stopped) {
            playingLabel.setText("Playing: -");
            albumLabel.setText("");
            byLabel.setText("");
        } else {
            playingLabel.setText("Playing: " + trackTitleString);
            albumLabel.setText("From: " + albumTitleString);
            byLabel.setText("By: " + artistNameString);

        }
    }

    private void trackIndexTracker() {
        previousTrackIndex = currentTrackIndex;
        currentTrackIndex = trackTableView.getItems().indexOf(trackTableView.getSelectionModel().getSelectedItem());

        if (currentTrackIndex == tableSize - 1) {
            nextTrackIndex = 0;
        } else {
            nextTrackIndex = currentTrackIndex + 1;
        }
//        Index Debugger
//        System.out.printf("curIndex:%d%n", currentTrackIndex);
//        System.out.printf("nextIndex:%d%n", nextTrackIndex);
//        System.out.printf("prevIndex:%d%n", previousTrackIndex);
//        System.out.printf("lastIndex:%d%n", tableSize - 1);
    }

    private void autoPlaySelected() {
        trackTableView.getSelectionModel().select(nextTrackIndex);
        stopMedia(true);
        playMedia();
    }

    private void shuffleSelected() {
        if (shuffleArray == null || shuffleArray.isEmpty()) {
            assert false;
            shuffleArray.add(currentTrackIndex);
        }

        if (!Objects.equals(previousArtistNameString, artistNameString)) {
            shuffleArray.clear();
            previousArtistNameString = artistNameString;
        }

        SecureRandom randNum = new SecureRandom();
        int randomIndex = randNum.nextInt(0, tableSize);

        if (!shuffleArray.contains(randomIndex)) { // If index is not present play next
            shuffleArray.add(randomIndex);
        } else if (shuffleArray.size() >= tableSize) { // reset table if array is larger than table size
            shuffleArray.clear();
            shuffleArray.add(randomIndex);
        } else {
            while (shuffleArray.contains(randomIndex)) { // if index present, find new index
                randomIndex = randNum.nextInt(0, tableSize);
                if (shuffleArray.size() == tableSize) {
                    shuffleArray.clear();
                    break;
                }
            }
            shuffleArray.add(randomIndex);
        }

        trackTableView.getSelectionModel().select(randomIndex);
        stopMedia(true);
        playMedia();
    }

    private void repeatSelected() {
        trackTableView.getSelectionModel().select(currentTrackIndex);
        stopMedia(true);
        playMedia();
    }

    @FXML
    private void closeClicked() {
        System.exit(0);
    }

    @FXML
    private void settingsClicked() throws IOException {
       SettingsController settingsController = new SettingsController();
       settingsController.showSettingsWindow(getArtistNameListView(), getTrackTableView());
    }

    // Getters
    private ListView<String> getArtistNameListView() { return artistNameListView; }
    private TableView<Track> getTrackTableView() { return trackTableView; }

}

