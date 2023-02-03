/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: MusicPlayerController
 *      Notes: Contains FXML member variables and controls program interface
 */
package com.iandw.musicplayerjavafx;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
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
    public TableColumn<Track, String> colTrackFileNameInvisible;
    @FXML
    private TableColumn<Track, String> colArtistNameInvisible;
    @FXML
    private TableColumn<Track, String> colPlaylistInvisible;
    @FXML
    private TextField searchField;
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
    private RadioButton autoButton;
    @FXML
    private RadioButton shuffleButton;
    @FXML
    private RadioButton repeatButton;
    @FXML
    private CheckBox mute;
    private MediaPlayer mediaPlayer;
    private String artistNameString;
    private String previousArtistNameString;
    private int currentTrackIndex;
    private int nextTrackIndex;
    private int previousTrackIndex;
    private int tableSize;
    private double volumeDouble;
    private boolean playing;
    private boolean stopped;
    private List<Integer> shuffleArray;
    private ObservableList<Track> trackList;
    private FilteredList<Track> filteredList;
    private ArrayList<String> playlistArray;
    private MusicLibrary musicLibrary;
    private TableViewLibrary tableViewLibrary;
    private ListViewLibrary listViewLibrary;
    private AutoPlay autoPlay;

    public MusicPlayerController() {
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          INITIALIZE CONTROLLER & LISTENERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void initialize() throws IOException {
        // Initialize variables
        playing = false;
        stopped = true;
        volumeDouble = 0.25;
        shuffleArray = new ArrayList<>();
        playlistArray = new ArrayList<>();
        trackList = FXCollections.observableArrayList();
        autoPlay = AutoPlay.OFF;

//        // Initialize root directory path for controller
//        rootMusicDirectoryString = SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL());

        // Input user playlists
        if (Files.size(Path.of(ResourceURLs.getPlaylistsURL())) > 0) {
            playlistArray.addAll(PlaylistsFileIO.inputPlaylists());
        }

        // Load data from root directory into app's List View
       // artistNameListView.setItems(ListViewLibrary.loadArtistNameCollection(rootMusicDirectoryString, playlistArray));


        // Initialization logic
//        musicLibrary = new MusicLibrary();
//        musicLibrary.initializeMusicLibrary();
//        artistNameListView.setItems(musicLibrary.getArtistNameObservableList());
//        trackList = musicLibrary.getTrackObservableList();

        // File I/O logic
        // Artist names / Playlists
        listViewLibrary = new ListViewLibrary();
        artistNameListView.setItems(listViewLibrary.loadArtistNameObservableList(playlistArray));

        // Track data
        tableViewLibrary = new TableViewLibrary();
        tableViewLibrary.inputTrackObservableList();
        trackList.addAll(tableViewLibrary.getTrackObservableList());


        // Test Logic => for file R/W every run
//        tableViewLibrary.initializeTrackObservableList();
//        tableViewLibrary.outputTrackObservableList();
//        tableViewLibrary.outputArtistNameObservableList();
//        tableViewLibrary.clearObservableList();
//        tableViewLibrary.inputTrackObservableList();
//        tableViewLibrary.inputArtistNameObservableList();




        // File input logic TODO => make better/less dumb
//        if (Files.size(Paths.get(ResourceURLs.getTrackListURL())) != 0) {
//            // Input from file
//            tableViewLibrary.inputTrackObservableList();
//        } else {
//            // Initialize track data directly from audio files
//            tableViewLibrary.initializeTrackObservableList();
//            tableViewLibrary.outputTrackObservableList();
//        }

        // Populate trackList
        //trackList = tableViewLibrary.getTrackObservableList();

        // Initialize table view
        artistNameListView.getSelectionModel().select(0);
        trackTableView.refresh();
        listViewSelected();

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

        // Toggle Logic
        autoButton.selectedProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        autoPlay = AutoPlay.AUTO_PLAY;

                        if (shuffleButton.isSelected() || repeatButton.isSelected()) {
                            radioButtonActive();
                        } else {
                            radioButtonOff();
                        }
                    } else {
                        autoPlay = AutoPlay.OFF;
                        deselectRadioButton();
                        shuffleArray.clear();
                    }
                }
        );

        shuffleButton.selectedProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        autoPlay = AutoPlay.SHUFFLE;
                        if (autoButton.isSelected() || repeatButton.isSelected()) {
                            radioButtonActive();

                        } else {
                            radioButtonOff();
                        }
                    } else {
                        autoPlay = AutoPlay.OFF;
                        deselectRadioButton();

                    }
                }
        );

        repeatButton.selectedProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (newValue) {
                        autoPlay = AutoPlay.REPEAT;

                        if (shuffleButton.isSelected() || autoButton.isSelected()) {
                            radioButtonActive();

                        } else {
                            radioButtonOff();
                        }
                    } else {
                        autoPlay = AutoPlay.OFF;
                        deselectRadioButton();
                    }
                }
        );

        // SearchField Listener
        searchField.textProperty().addListener(
                ((observableValue, oldValue, newValue) -> {
                    filteredList.setPredicate(createSearchPredicate(newValue));
                    tableSize = filteredList.size();
                    shuffleArray.clear();
                })
        );

    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          LIST VIEW MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    private void handleListViewMouseClick(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            listViewSelected();
        }
    }

    private void listViewSelected() {
        // Get selected artist name (from directory name)
        previousArtistNameString = artistNameString;

        artistNameString = artistNameListView.getSelectionModel().getSelectedItem();

        trackTableView.getSortOrder().add(colTrackFileNameInvisible);

        // Create a filtered list for trackTableView
        filteredList = new FilteredList<>(FXCollections.observableArrayList(trackList));
        filteredList.setPredicate(createListPredicate());
        trackTableView.setItems(filteredList);
        trackTableView.setVisible(true);
        tableSize = filteredList.size();

        // Populate trackTableView with track object data
        colArtistNameInvisible.setCellValueFactory(new PropertyValueFactory<>("artistNameStr"));
        colTrackFileNameInvisible.setCellValueFactory(new PropertyValueFactory<>("trackFileNameStr"));
        colTrackTitle.setCellValueFactory(new PropertyValueFactory<>("trackTitleStr"));
        colAlbumTitle.setCellValueFactory(new PropertyValueFactory<>("albumTitleStr"));
        colTrackLength.setCellValueFactory(new PropertyValueFactory<>("trackDurationStr"));
        colTrackGenre.setCellValueFactory(new PropertyValueFactory<>("trackGenreStr"));
        colPlaylistInvisible.setCellValueFactory(new PropertyValueFactory<>("playlistStr"));

//              Debugger
//              System.out.printf("currentPath: %s%n", currentPath);

    }

    @FXML
    private void handleListViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("View in Explorer");
        MenuItem createPlaylist = new MenuItem("Create Playlist");

        item1.setOnAction(event -> viewInFileExplorer());
        createPlaylist.setOnAction(event -> {
            try {
                createPlaylist();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // TODO => Set more context menu options

        contextMenu.getItems().addAll(item1);

        artistNameListView.setContextMenu(contextMenu);

    }

    private void createPlaylist() throws IOException {
//        PlaylistController playlistController = new PlaylistController();
//        playlistController.showPlaylistInputWindow(playlistArray);

    }


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          TABLE VIEW MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    private void handleTableViewContextMenu() {
        // TODO => create context menu options
        ContextMenu contextMenu = new ContextMenu();
        Menu addTrackToPlaylist = new Menu("Add to playlist");
        ArrayList<MenuItem> playlistMenuList = new ArrayList<>();

        for (String playlist : playlistArray) {
            playlistMenuList.add(new MenuItem(playlist));
        }

        addTrackToPlaylist.getItems().addAll(playlistMenuList);

        addTrackToPlaylist.setOnAction(event ->  {
            System.out.printf("Add track to playlist %s%n", ((MenuItem)event.getTarget()).getText());
            trackTableView.getSelectionModel().getSelectedItem().setPlaylistStr(((MenuItem)event.getTarget()).getText());

        });

        contextMenu.getItems().addAll(addTrackToPlaylist);

        trackTableView.setContextMenu(contextMenu);

    }

//    private void addTrackToPlaylist() {
//        System.out.println("Add track to playlist x");
//        trackTableView.getSelectionModel().getSelectedItem().setPlaylistStr();
//    }

    private void viewInFileExplorer() {
//        String filePath = rootMusicDirectoryString + File.separator +
//                artistNameListView.getSelectionModel().getSelectedItem();
//        System.out.println(filePath);
//        System.out.println(Desktop.isDesktopSupported());
        // TODO => fix?

    }

    @FXML
    private void handleTableViewMouseClick(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
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

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          SEARCH BAR/LISTVIEW SEARCH MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    private Predicate<Track> createListPredicate() {
        return this::searchTracks;
    }

    private boolean searchTracks(Track track) {
        return track.getArtistNameStr().contains(artistNameString);
    }

    private Predicate<Track> createSearchPredicate(String searchText) {
        return track -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return searchMetadata(track, searchText);
        };
    }

    private boolean searchMetadata(Track track, String searchText) {
        return (track.getTrackTitleStr().toLowerCase().contains(searchText.toLowerCase()) ||
                track.getAlbumTitleStr().toLowerCase().contains(searchText.toLowerCase()) ||
                track.getArtistNameStr().toLowerCase().contains(searchText.toLowerCase()) ||
                track.getTrackGenreStr().toLowerCase().contains(searchText.toLowerCase()) ||
                track.getPlaylistStr().toLowerCase().contains(searchText.toLowerCase())
        );
    }

    @FXML
    private void handleClearSearchText(MouseEvent mouseClick) throws IOException {
        searchField.setText("");
        listViewSelected();
        mouseClick.consume();
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                 PLAY/PAUSE/PREVIOUS/NEXT/STOP BUTTON MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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
            if (shuffleButton.isSelected()) {
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


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                         PLAY/STOP/PAUSE MEDIA MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    private void playMedia() {

        String currentPath = trackTableView.getSelectionModel().getSelectedItem().getTrackPathStr();

        // Debugger
        System.out.printf("currentPath: %s%n", currentPath);

        // Create Media Object
        Media audioFile = new Media(new File(currentPath).toURI().toString());
        mediaPlayer = new MediaPlayer(audioFile);

        // Track current track index for prev, next and autoplay functions
        trackIndexTracker();

        // Set Seeker slider
        mediaPlayer.currentTimeProperty().addListener(observable -> seekValueUpdate());

        // Play media
        mediaPlayer.setVolume(volumeDouble);

        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
            playPauseButton.setText("Pause");
            playing = true;
            stopped = false;

            // Set text to currently playing text fields
            setNowPlayingText();
        });

        // Autoplay or stop media player after current track is finished
        mediaPlayer.setOnEndOfMedia(() -> {
            if (trackTableView.getSelectionModel().getSelectedItem() != null) {
                System.out.println(autoPlay);
                if (autoButton.isSelected()) {
                    autoPlaySelected();
                } else if (repeatButton.isSelected()) {
                    repeatSelected();
                } else if (shuffleButton.isSelected()) {
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

    private void seekValueUpdate() {
        if (mediaPlayer == null) {
            trackDurationLabel.setText("");
            trackCurrentTimeLabel.setText("");
        }
        trackDurationLabel.setText(Utils.formatSeconds((int) mediaPlayer.getTotalDuration().toSeconds()));
        trackCurrentTimeLabel.setText(Utils.formatSeconds((int) mediaPlayer.getCurrentTime().toSeconds()));
        seekSlider.valueProperty().setValue(mediaPlayer.getCurrentTime().toMillis() /
                mediaPlayer.getTotalDuration().toMillis() * 100);
    }

    private void setNowPlayingText() {
        if (stopped) {
            playingLabel.setText("Playing: -");
            albumLabel.setText("");
            byLabel.setText("");
        } else {
            playingLabel.setText("Playing: " + trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr());
            albumLabel.setText("From: " + trackTableView.getSelectionModel().getSelectedItem().getAlbumTitleStr());
            byLabel.setText("By: " + trackTableView.getSelectionModel().getSelectedItem().getArtistNameStr());
        }
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                         TRACK AUTOPLAY/SHUFFLE/REPEAT MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private void radioButtonActive() {
        switch (autoPlay) {
            case AUTO_PLAY -> {
                shuffleButton.setSelected(false);
                repeatButton.setSelected(false);
                autoButton.setSelected(true);
            }
            case SHUFFLE -> {
                shuffleButton.setSelected(true);
                repeatButton.setSelected(false);
                autoButton.setSelected(false);
            }
            case REPEAT -> {
                shuffleButton.setSelected(false);
                repeatButton.setSelected(true);
                autoButton.setSelected(false);
            }
            case OFF -> {
                shuffleButton.setSelected(false);
                repeatButton.setSelected(false);
                autoButton.setSelected(false);
            }

        }
    }
    private void radioButtonOff() {
        switch (autoPlay) {
            case AUTO_PLAY -> {
                autoButton.setSelected(true);
            }
            case SHUFFLE -> {
                shuffleButton.setSelected(true);
            }
            case REPEAT -> {
                repeatButton.setSelected(true);
            }
        }
    }

    private void deselectRadioButton() {
        switch (autoPlay) {
            case AUTO_PLAY -> {
                autoButton.setSelected(false);
            }
            case SHUFFLE -> {
                shuffleButton.setSelected(false);
            }
            case REPEAT -> {
                repeatButton.setSelected(false);
            }
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
        trackTableView.scrollTo(nextTrackIndex); //TODO => funky scroll logic
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
                if (shuffleArray.size() >= tableSize) { // while loop fail-safe
                    shuffleArray.clear();
                    break;
                }
            }
            shuffleArray.add(randomIndex);
        }

        trackTableView.getSelectionModel().select(randomIndex);
        trackTableView.scrollTo(randomIndex);
        stopMedia(true);
        playMedia();
    }

    private void repeatSelected() {
        trackTableView.getSelectionModel().select(currentTrackIndex);
        stopMedia(true);
        playMedia();
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                         FILE MENU MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    private void closeClicked() {
        System.exit(0);
    }

    @FXML
    private void settingsClicked() throws IOException {
       SettingsController settingsController = new SettingsController();
       settingsController.showSettingsWindow(artistNameListView, trackTableView, tableViewLibrary,
               trackList, playlistArray);
    }


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                         GETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
//    private ListView<String> getArtistNameListView() { return artistNameListView; }
//    private TableView<Track> getTrackTableView() { return trackTableView; }
//    private TableViewLibrary getTableViewLibrary() { return tableViewLibrary; }
//    private ObservableList<Track> getTrackList() { return trackList; }
//    private ArrayList<String> getPlaylistArray() { return playlistArray; }

}

