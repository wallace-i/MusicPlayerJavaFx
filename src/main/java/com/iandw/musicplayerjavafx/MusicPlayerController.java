/**
 *      Author: Ian Wallace, copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: MusicPlayerController
 *      Notes: Contains FXML member variables and controls program interface
 */
package com.iandw.musicplayerjavafx;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.image.ImageView;

public class MusicPlayerController {
    // FXML ids
    @FXML
    private ListView<String> artistListView;
    @FXML
    private ListView<String> playlistListView;
    @FXML
    private TableView<TrackMetadata> trackTableView;
    @FXML
    private TableColumn<TrackMetadata, String> colTrackTitle;
    @FXML
    public TableColumn<TrackMetadata, String> colAlbumTitle;
    @FXML
    public TableColumn<TrackMetadata, String> colTrackLength;
    @FXML
    public TableColumn<TrackMetadata, String> colTrackGenre;
    @FXML
    public TableColumn<TrackMetadata, String> colTrackFileNameInvisible;
    @FXML
    private TableColumn<TrackMetadata, String> colArtistNameInvisible;
    @FXML
    private TableColumn<TrackMetadata, String> colPlaylistInvisible;
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
    private Label volumeLevelLabel;
    @FXML
    private Label volumeIconLabel;
    @FXML
    private RadioButton autoButton;
    @FXML
    private RadioButton shuffleButton;
    @FXML
    private RadioButton repeatButton;
    @FXML
    private CheckBox mute;
    @FXML
    private FontIcon playIcon;
    @FXML
    private FontIcon pauseIcon;
    @FXML
    private FontIcon volumeUp;
    @FXML
    private FontIcon volumeDown;
    @FXML
    private FontIcon volumeOff;
    @FXML
    private FontIcon volumeMute;
    @FXML
    private FontIcon albumIcon;
    @FXML
    private FontIcon artistIcon;
    @FXML
    private ImageView imageView;

    private Image defaultAlbumImage;
    private ImageFileLogic imageFileLogic;
    private MediaPlayer mediaPlayer;
    private MusicLibrary musicLibrary;
    private final TableViewLibrary tableViewLibrary;
    private final ListViewLibrary listViewLibrary;
    private SearchTableView searchTableView;
    private AutoPlay autoPlay;
    private TrackIndex trackIndex;
    private final UserSettings userSettings;
    private final ExecutorService executorService;
    private ByteArrayOutputStream consoleOutput;

    private final Stage stage;
    private String artistNameString;
    private String playlistTitleString;
    private String previousArtistNameString;
    private String currentTheme;
    private double volumeDouble;
    private boolean playing;
    private boolean stopped;
    private boolean artistsListSelected;
    private int albumImageWidth;

    // Constructor
    public MusicPlayerController(Stage stage, ExecutorService executorService, ByteArrayOutputStream consoleOutput,
                                 UserSettings userSettings, ListViewLibrary listViewLibrary, TableViewLibrary tableViewLibrary)
    {
        this.stage = stage;
        this.executorService = executorService;
        this.consoleOutput = consoleOutput;
        this.userSettings = userSettings;
        this.listViewLibrary = listViewLibrary;
        this.tableViewLibrary = tableViewLibrary;
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          INITIALIZE CONTROLLER ON STARTUP
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void initialize() throws IOException, InterruptedException {
        // Initialize variables
        playing = false;
        stopped = true;
        artistsListSelected = true;
        searchTableView = new SearchTableView();
        currentTheme = userSettings.getThemeFileNameString();
        imageFileLogic = new ImageFileLogic(currentTheme);
        playPauseButton.setGraphic(playIcon);
        volumeIconLabel.setGraphic(volumeUp);
        albumIcon.setOpacity(0);
        artistIcon.setOpacity(0);
        albumImageWidth = 55;
        volumeDouble = 0.25;
        autoPlay = AutoPlay.OFF;
        trackIndex = new TrackIndex();
        artistNameString = "";
        playlistTitleString = "";

        // Set TableView column widths
        colTrackTitle.setMaxWidth( 1f * Integer.MAX_VALUE * 40 ); // 40% width
        colAlbumTitle.setMaxWidth( 1f * Integer.MAX_VALUE * 40 );
        colTrackLength.setMaxWidth( 1f * Integer.MAX_VALUE * 6 );
        colTrackGenre.setMaxWidth( 1f * Integer.MAX_VALUE * 14 );

        // Autoplay Icon (all other icons are from bootstrapicons -> musiclibrary.fxml)
        ImageView autoPlayIcon = new ImageView(ResourceURLs.getAutoplayiconURL());

        // Change color to match theme
        autoPlayIcon.setEffect(imageFileLogic.getLighting());
        autoButton.setGraphic(autoPlayIcon);
        autoButton.getGraphic().setTranslateX(2.0);

        // Album Art default graphic
        defaultAlbumImage = new Image(imageFileLogic.getAlbumImage());
        imageView.setImage(defaultAlbumImage);
        imageView.setOpacity(.9);
        imageView.setCache(true);
        imageView.setVisible(true);

        // Initialize main app objects for Music Library, ListView, and TableView
        musicLibrary = new MusicLibrary(userSettings);
//        musicLibrary.initializeMusicLibrary();  // Hard re-initialize music library

        // Initialize Library if tracklist.ser is empty
        if (Files.size(Paths.get(ResourceURLs.getTrackListURL())) == 0) {
            // Choose Root Directory for Music Library
            String directoryLabel = "Welcome, press 'Music Folder' to initialize.";

            SettingsController settingsController = new SettingsController();
            settingsController.showSettingsWindow(artistListView, playlistListView, trackTableView, listViewLibrary,
                    tableViewLibrary, musicLibrary, userSettings, directoryLabel);

            listViewLibrary.setOutputListsOnClose();
            tableViewLibrary.setOutputTrackListOnClose();

        } else {
            // Else initialize data normally from .ser files
            // Playlist and Artist List Data => artistPlaylistListView
            artistListView.setItems(listViewLibrary.getArtistObservableList());
            playlistListView.setItems(listViewLibrary.getPlaylistObservableList());

            // Track Metadata => trackTableView
            trackTableView.setItems(tableViewLibrary.getTrackObservableList());

            // Initialize table view after files are read in via executorService
            // Max wait for 10 seconds (for reference, 200gb music files should take ~4 seconds)
            if (executorService.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                artistListView.getSelectionModel().select(0);
                trackTableView.refresh();
                listViewSelected();
            }

        }

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *
         *                        LISTENERS
         *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

        // listener for changes to volumeSlider's value
        volumeSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    int volumeInt = newValue.intValue();
                    volumeLevelLabel.setText(Integer.toString(volumeInt));

                    try {
                        volumeDouble = Math.pow(newValue.doubleValue(), 2) / 10000;
                        mediaPlayer.setVolume(volumeDouble);

                    } catch (NullPointerException e) {
                        System.out.println("mediaPlayer is null");
                    }

                    if (volumeSlider.getValue() >= 50) {
                        volumeIconLabel.setGraphic(volumeUp);

                    } else if (volumeSlider.getValue() > 0) {
                        volumeIconLabel.setGraphic(volumeDown);

                    } else {
                        volumeIconLabel.setGraphic(volumeOff);
                    }
                }
        );

        // Mute checkbox
        mute.selectedProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (mediaPlayer != null) {
                        mediaPlayer.setMute(newValue);

                        if (mute.isSelected()) {
                            volumeIconLabel.setGraphic(volumeMute);

                        } else {
                            if (volumeSlider.getValue() >= 50) {
                                volumeIconLabel.setGraphic(volumeUp);

                            } else if (volumeSlider.getValue() > 0) {
                                volumeIconLabel.setGraphic(volumeDown);

                            } else {
                                volumeIconLabel.setGraphic(volumeOff);
                            }
                        }
                    }

                }
        );

        // Seek time during track duration, and updating current duration on seekSlider
        seekSlider.valueProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if (seekSlider.isPressed() && !stopped) {
                        mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(seekSlider.getValue() / 100));
                    }

                    double percentage = 100.0 * newValue.doubleValue() / seekSlider.getMax();

                    if (Double.isNaN(percentage)) { percentage = 0.0; }

                    // Set slideSeeker css based on current style sheet
                    String style = String.format(SeekSliderColor.getStyle(currentTheme), percentage);

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
                        trackIndex.getShuffleArray().clear();
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
                    tableViewLibrary.getFilteredList().setPredicate(searchTableView.createSearchPredicate(newValue));
                    trackIndex.setTableSize(tableViewLibrary.getFilteredList().size());
                    trackIndex.clearShuffleArray();
                    trackIndex.clearPreviousIndexStack();
                })
        );


    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          TABLE VIEW
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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
     *                          LIST VIEW MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    private void handleArtistsListViewMouseClick(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY) && artistListView != null &&
                artistListView.getSelectionModel().getSelectedItem() != null)
        {
            playlistListView.getSelectionModel().clearSelection();
            artistListView.requestFocus();
            artistsListSelected = true;
            artistNameString = artistListView.getSelectionModel().getSelectedItem();
            listViewSelected();
        }
    }

    @FXML
    private void handlePlaylistsListViewClick(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY) && playlistListView != null &&
                playlistListView.getSelectionModel().getSelectedItem() != null)
        {
            artistListView.getSelectionModel().clearSelection();
            playlistListView.requestFocus();
            artistsListSelected = false;
            playlistTitleString = playlistListView.getSelectionModel().getSelectedItem();
            listViewSelected();
        }
    }

    private void listViewSelected() {
        // Get selected artist name (from directory name)
        previousArtistNameString = artistNameString;

        // Create a filtered list for trackTableView
        tableViewLibrary.createFilteredList();

        // Check artistsObservableList for artist name, call artist list predicate if true.
        // Else call the playlistListView predicate
        if (artistsListSelected && artistNameString != null) {
            tableViewLibrary.getFilteredList().setPredicate(searchTableView.createArtistsListPredicate(
                    artistNameString, artistListView));

        } else if (playlistTitleString != null) {
            // Remove null pointer exceptions from predicate search
            tableViewLibrary.getFilteredList().setPredicate(searchTableView.createPlaylistsListPredicate(
                    playlistTitleString, playlistListView));

        }

        trackTableView.setItems(tableViewLibrary.getFilteredList());
        trackTableView.setVisible(true);
        trackIndex.setTableSize(tableViewLibrary.getFilteredList().size());

        // Sort Table
        trackTableView.getSortOrder().add(colTrackFileNameInvisible);

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

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          CONTEXT MENU MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    private void handleArtistListViewContextMenu() {

        artistNameString = artistListView.getSelectionModel().getSelectedItem();

        if (artistNameString != null) {
            // Right-clicking will update the tableview based on selection
            trackTableView.refresh();
            listViewSelected();

            ArtistListContextMenu.getContextMenu(artistListView, playlistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex, userSettings);
        }

    }

    @FXML
    private void handlePlaylistsListViewContextMenu() {

        playlistTitleString = playlistListView.getSelectionModel().getSelectedItem();

        // Right-clicking will update the tableview based on selection
        if (playlistTitleString != null) {
            trackTableView.refresh();
            listViewSelected();

            PlaylistContextMenu.getContextMenu(artistListView, playlistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex);
        }

    }

    @FXML
    private void handleTableViewContextMenu()  {
        TableViewContexMenu.getContextMenu(artistListView, trackTableView,
                listViewLibrary, tableViewLibrary, trackIndex);

        // Refresh TableView
        listViewSelected();
    }


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                         SEARCH BAR
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    @FXML
    private void handleClearSearchText(MouseEvent mouseClick) {
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
            trackIndex.setPushCurrentTrackToStack(true);

            try {
                // Pause currently playing track
                if (playing) {
                    mediaPlayer.pause();
                    playPauseButton.setGraphic(playIcon);
                    playing = false;

                // Play from selected track if stopped or null
                } else if (mediaPlayer == null || stopped) {
                    playMedia();

                // Play from currently paused track
                } else {
                    mediaPlayer.play();
                    playPauseButton.setGraphic(pauseIcon);
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
            if (!stopped) {
                try {
                    if (!playing) {
                        mediaPlayer.play();
                        playPauseButton.setGraphic(pauseIcon);
                    }

                } catch (NullPointerException e) {
                    System.out.println("No track selected.");
                }
            }
        }
    }

    @FXML
    private void stopButtonPressed(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY)) {
            try {
                if (playing && (Objects.equals(artistNameString, artistListView.getSelectionModel().getSelectedItem()))) {
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
            trackIndex.setPushCurrentTrackToStack(true);
            if (shuffleButton.isSelected()) {
                shuffleSelected();

            } else if (trackTableView.getSelectionModel().getSelectedItem() != null) {
                trackTableView.getSelectionModel().select(trackIndex.getNextTrackIndex());
                stopMedia(true);
                playMedia();
            }
        }
    }

    @FXML
    private void previousButtonPressed(MouseEvent mouseClick) {
        if (mouseClick.getButton().equals(MouseButton.PRIMARY) && mediaPlayer != null &&
            trackTableView.getSelectionModel().getSelectedItem() != null)
        {
            trackIndex.setPushCurrentTrackToStack(false);

            trackTableView.scrollTo(trackIndex.peekPreviousIndexArray());

            if (!trackIndex.getPreviousIndexStack().empty()) {
                trackTableView.getSelectionModel().select(trackIndex.popPreviousIndexArray());
            }

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
        final String currentPath = trackTableView.getSelectionModel().getSelectedItem().getTrackPathStr();

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

            if (mediaPlayer.getMedia().getMetadata().get("image") == null) {
                imageView.setImage(defaultAlbumImage);
            } else {
                imageView.setImage((Image) mediaPlayer.getMedia().getMetadata().get("image"));
            }

            imageView.setPreserveRatio(true);
            imageView.setFitWidth(albumImageWidth);
            playPauseButton.setGraphic(pauseIcon);
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
        mediaPlayer.stop();

        if (dispose) {
            mediaPlayer.dispose();
        }

        playPauseButton.setGraphic(playIcon);
        playing = false;
        stopped = true;
        setNowPlayingText();

    }

    private void seekValueUpdate() {
        if (mediaPlayer == null) {
            trackDurationLabel.setText("");
            trackCurrentTimeLabel.setText("");
        }
        trackDurationLabel.setText(Utils.formatSeconds(
                (int) mediaPlayer.getTotalDuration().toSeconds() - (int) mediaPlayer.getCurrentTime().toSeconds()));
        trackCurrentTimeLabel.setText(Utils.formatSeconds((int) mediaPlayer.getCurrentTime().toSeconds()));
        seekSlider.valueProperty().setValue(mediaPlayer.getCurrentTime().toMillis() /
                mediaPlayer.getTotalDuration().toMillis() * 100);
    }

    private void setNowPlayingText() {
        if (stopped) {
            playingLabel.setText("-");
            albumLabel.setText("");
            byLabel.setText("");
            albumIcon.setOpacity(0);
            artistIcon.setOpacity(0);

        } else if (trackTableView.getSelectionModel().getSelectedIndex() != trackIndex.getCurrentTrackIndex()) {
            albumIcon.setOpacity(100);
            artistIcon.setOpacity(100);
            playingLabel.setText(" " + trackTableView.getItems().get(trackIndex.getCurrentTrackIndex()).getTrackTitleStr());
            albumLabel.setText(" " + trackTableView.getItems().get(trackIndex.getCurrentTrackIndex()).getAlbumTitleStr());
            byLabel.setText(" " + trackTableView.getItems().get(trackIndex.getCurrentTrackIndex()).getArtistNameStr());

        } else {
            albumIcon.setOpacity(100);
            artistIcon.setOpacity(100);
            playingLabel.setText(" " + trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr());
            albumLabel.setText(" " + trackTableView.getSelectionModel().getSelectedItem().getAlbumTitleStr());
            byLabel.setText(" " + trackTableView.getSelectionModel().getSelectedItem().getArtistNameStr());
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
            case AUTO_PLAY  -> autoButton.setSelected(true);
            case SHUFFLE    -> shuffleButton.setSelected(true);
            case REPEAT     -> repeatButton.setSelected(true);
        }
    }

    private void deselectRadioButton() {
        switch (autoPlay) {
            case AUTO_PLAY  -> autoButton.setSelected(false);
            case SHUFFLE    -> shuffleButton.setSelected(false);
            case REPEAT     -> repeatButton.setSelected(false);

        }
    }

    private void trackIndexTracker() {
        // If prev button is continually pressed, do not add current track to stack.
        // Will add current track to stack when true, while next, auto, user selection, or shuffle are selected.
        if (trackIndex.getPushCurrentTrackToStack()) {
            trackIndex.pushToPreviousIndexArray(trackIndex.getCurrentTrackIndex());
        }

        trackIndex.setCurrentTrackIndex(trackTableView.getItems().indexOf(
                trackTableView.getSelectionModel().getSelectedItem()));

        if (trackIndex.getCurrentTrackIndex() == trackIndex.getTableSize() - 1) {
            trackIndex.setNextTrackIndex(0);
        } else {
            trackIndex.setNextTrackIndex(trackIndex.getCurrentTrackIndex() + 1);
        }
//        Index Debugger
//        System.out.printf("curIndex:%d%n", trackIndex.getCurrentTrackIndex());
//        System.out.printf("nextIndex:%d%n", trackIndex.getNextTrackIndex());
//        System.out.printf("prevIndex:%d%n", trackIndex.peekPreviousIndexArray());
//        System.out.printf("lastIndex:%d%n", trackIndex.getTableSize() - 1);
    }

    private void autoPlaySelected() {
        trackIndex.setPushCurrentTrackToStack(true);
        trackTableView.getSelectionModel().select(trackIndex.getNextTrackIndex());
        trackTableView.scrollTo(trackIndex.getNextTrackIndex());
        stopMedia(true);
        playMedia();
    }

    private void shuffleSelected() {
        int tableSize = trackIndex.getTableSize();
        trackIndex.setPushCurrentTrackToStack(true);

        if (trackIndex.getShuffleArray() == null || trackIndex.getShuffleArray().isEmpty()) {
            assert false;
            trackIndex.addToShuffleArray(trackIndex.getCurrentTrackIndex());
        }

        if (!Objects.equals(previousArtistNameString, artistNameString)) {
            trackIndex.clearShuffleArray();
            previousArtistNameString = artistNameString;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(0, tableSize);

        if (!trackIndex.getShuffleArray().contains(randomIndex)) { // If index is not present play next
            trackIndex.addToShuffleArray(randomIndex);

        } else if (trackIndex.getShuffleArray().size() >= tableSize) { // reset table if array is larger than table size
            trackIndex.clearShuffleArray();
            trackIndex.addToShuffleArray(randomIndex);

        } else {
            while (trackIndex.getShuffleArray().contains(randomIndex)) { // if index present, find new index
                randomIndex = random.nextInt(0, tableSize);

                if (trackIndex.getShuffleArray().size() >= tableSize) { // while loop fail-safe
                    trackIndex.clearShuffleArray();
                    break;
                }
            }
            trackIndex.getShuffleArray().add(randomIndex);
        }

        trackTableView.getSelectionModel().select(randomIndex);
        trackTableView.scrollTo(randomIndex);
        stopMedia(true);
        playMedia();
    }

    private void repeatSelected() {
        trackIndex.setPushCurrentTrackToStack(false);
        trackTableView.getSelectionModel().select(trackIndex.getCurrentTrackIndex());
        stopMedia(true);
        playMedia();
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                         FILE MENU MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    private void importArtistClicked() throws IOException {
        musicLibrary.importArtist();
        tableViewLibrary.setTrackObservableList(musicLibrary.getTrackObservableList());
        addArtistFromImport();
    }

    @FXML
    private void importAlbumClicked() throws IOException {
        musicLibrary.importAlbum();
        tableViewLibrary.setTrackObservableList(musicLibrary.getTrackObservableList());
        addArtistFromImport();
    }

    @FXML
    private void importTrackClicked() throws IOException {
        musicLibrary.importTrack();

        if (!musicLibrary.getTrackObservableList().isEmpty()) {
            tableViewLibrary.addTrack(musicLibrary.getImportedTrack());
            addArtistFromImport();
        }
    }

    // Add artist name to list view and save to file if not available
    private void addArtistFromImport() {
        if (!listViewLibrary.getArtistObservableList().contains(musicLibrary.getArtistNameStr())) {
            listViewLibrary.addArtist(musicLibrary.getArtistNameStr());
            artistListView.setItems(listViewLibrary.getArtistObservableList());
        }

        // Write to File on close
        tableViewLibrary.setOutputTrackListOnClose();
    }

    @FXML
    private void settingsClicked() throws IOException {
        String directoryLabel = userSettings.getRootMusicDirectoryString();
        SettingsController settingsController = new SettingsController();
        settingsController.showSettingsWindow(artistListView, playlistListView, trackTableView, listViewLibrary,
                tableViewLibrary, musicLibrary, userSettings, directoryLabel);
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                         EDIT MENU MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @FXML
    private void addArtistClicked() {
        ArtistListContextMenu.addArtist(artistListView, playlistListView, trackTableView,
                listViewLibrary, tableViewLibrary, trackIndex);

    }

    @FXML
    private void createPlaylistClicked() {
        PlaylistContextMenu.createPlaylist(artistListView, playlistListView, trackTableView,
                listViewLibrary, tableViewLibrary, trackIndex);

    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                         HELP MENU MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    private void aboutClicked() throws IOException {
        final String about = "About";
        ViewTextController viewTextController = new ViewTextController();
        viewTextController.showViewTextWindow(about, consoleOutput);

    }

    @FXML
    private void gitHubClicked() {
        final String gitHubUrl = "https://github.com/wallace-i/MusicPlayerJavaFx";
        HostServices hostServices = (HostServices) stage.getProperties().get("hostServices");
        hostServices.showDocument(gitHubUrl);

    }

    @FXML
    private void consoleLogClicked() throws IOException {
        final String consoleLog = "Console Log";
        ViewTextController viewTextController = new ViewTextController();
        viewTextController.showViewTextWindow(consoleLog, consoleOutput);

        // write to file on close

    }

    @FXML
    private void reportBugClicked() {
        // send email w/ console log

    }


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                         ON CLOSE
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    private void closeClicked() throws FileNotFoundException {
        // Output to file on close if files data has been altered
        if (userSettings.getWriteOnClose()) {
            SettingsFileIO.jsonFileOutput(userSettings);
        }

        listViewLibrary.onClose();
        tableViewLibrary.onClose();

        // Write console log to file
        ConsoleLogFileIO.outputConsoleLog(consoleOutput.toString());

        stage.close();
    }

}

