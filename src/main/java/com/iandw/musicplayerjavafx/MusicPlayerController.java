/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: MusicPlayerController
 *      Notes: Contains FXML member variables and controls program interface
 */
package com.iandw.musicplayerjavafx;

import java.awt.Desktop;
import java.io.*;
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
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayerController {
    @FXML
    private ListView<String> artistPlaylistListView;
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
        trackList = FXCollections.observableArrayList();
        autoPlay = AutoPlay.OFF;

        // Initialization logic
        musicLibrary = new MusicLibrary();
        musicLibrary.initializeMusicLibrary();
//        artistNameListView.setItems(musicLibrary.getArtistNameObservableList());
//        trackList.addAll(musicLibrary.getTrackObservableList());

        // File I/O logic
        // Artist names / Playlists
        listViewLibrary = new ListViewLibrary();
        artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

        // Track data
        tableViewLibrary = new TableViewLibrary(TracklistFileIO.inputTrackObservableList());
       // tableViewLibrary.setTrackObservableList(TracklistFileIO.inputTrackObservableList());
        trackList.setAll(tableViewLibrary.getTrackObservableList());


        // File input logic TODO => make better/less dumb
//        if (Files.size(Paths.get(ResourceURLs.getTrackListURL())) != 0) {
//            // Input from file
//            tableViewLibrary.inputTrackObservableList();
//        } else {
//            // Initialize track data directly from audio files
//            tableViewLibrary.initializeTrackObservableList();
//            tableViewLibrary.outputTrackObservableList();
//        }


        // Initialize table view
        artistPlaylistListView.getSelectionModel().select(0);
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

        artistNameString = artistPlaylistListView.getSelectionModel().getSelectedItem();

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

        // Right clicking will update the tableview based on selection
        if (artistPlaylistListView.getSelectionModel().getSelectedItem() != null) {
            artistPlaylistListView.getSelectionModel().select(artistPlaylistListView.getSelectionModel().getSelectedItem());
            trackTableView.refresh();
            listViewSelected();
        }

        String selectedItem = artistPlaylistListView.getSelectionModel().getSelectedItem();
        System.out.println("ListView selected: " + selectedItem);

        ContextMenu contextMenu = new ContextMenu();

        // Add to list
        MenuItem createPlaylist = new MenuItem("Create Playlist");
        MenuItem addArtist = new MenuItem("Add Artist");
        SeparatorMenuItem divider1 = new SeparatorMenuItem();

        // Edit List
        MenuItem editListView = new MenuItem("Edit");
        SeparatorMenuItem divider2 = new SeparatorMenuItem();

        // Remove from list
        MenuItem removeListView = new MenuItem("Remove");
        SeparatorMenuItem divider3 = new SeparatorMenuItem();

        // View folder in explorer
        MenuItem openInExplorer = new MenuItem("Open in Explorer");

        // Create Playlist
        createPlaylist.setOnAction(event -> {
            try {
                String windowTitle = "Playlist";
                String menuSelection = artistPlaylistListView.getSelectionModel().getSelectedItem();
                ListViewController listViewController = new ListViewController();
                listViewController.showListViewInputWindow(trackList, tableViewLibrary, trackTableView,
                        artistPlaylistListView, listViewLibrary, windowTitle, menuSelection, tableSize);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Add Artist
        addArtist.setOnAction(event -> {
            try {
                String windowTitle = "Artist";
                String menuSelection = artistPlaylistListView.getSelectionModel().getSelectedItem();
                ListViewController listViewController = new ListViewController();
                listViewController.showListViewInputWindow(trackList, tableViewLibrary, trackTableView,
                        artistPlaylistListView, listViewLibrary, windowTitle, menuSelection, tableSize);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Edit Artist or Playlist
        editListView.setOnAction(event -> {
            try {
                String windowTitle = "Edit";
                String menuSelection = artistPlaylistListView.getSelectionModel().getSelectedItem();
                ListViewController listViewController = new ListViewController();
                listViewController.showListViewInputWindow(trackList, tableViewLibrary, trackTableView,
                        artistPlaylistListView, listViewLibrary, windowTitle, menuSelection, tableSize);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Remove Artist or Playlist
        removeListView.setOnAction(event -> {
            String menuSelection = artistPlaylistListView.getSelectionModel().getSelectedItem();
            if (listViewLibrary.getPlaylistArray().contains(menuSelection)) {
                removePlaylist(menuSelection);

            } else if (listViewLibrary.getArtistList().contains(menuSelection)) {
                removeArtist(menuSelection);
            }
        });

        // Open in File Explorer
        openInExplorer.setOnAction(event -> {
            String menuSelection = artistPlaylistListView.getSelectionModel().getSelectedItem();
            File file = new File(SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL()) +
                    File.separator + menuSelection);

            openExplorer(file);
        });

        contextMenu.getItems().addAll(createPlaylist, addArtist, divider1, editListView, divider2, removeListView,
                divider3, openInExplorer);

        artistPlaylistListView.setContextMenu(contextMenu);

    }

    private void removeArtist(String removeArtistStr) {
        listViewLibrary.removeArtist(removeArtistStr);

        if (tableSize > 0) {
            // Remove Tracks from library
            for (int trackIndex = 0; trackIndex < tableSize; trackIndex++) {
                System.out.printf("Removing %s from %s%n", trackTableView.getItems().get(trackIndex).getTrackTitleStr(),
                        artistPlaylistListView.getSelectionModel().getSelectedItem());
                tableViewLibrary.removeTrack(trackTableView.getItems().get(trackIndex));
                trackTableView.refresh();
            }

            // Write to file
            try {
                trackList.clear();
                trackList.setAll(tableViewLibrary.getTrackObservableList());
                TracklistFileIO.outputTrackObservableList(trackList);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        artistPlaylistListView.getItems().clear();
        artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());
    }

    private void removePlaylist(String removePlaylistStr) {
        listViewLibrary.removePlaylist(removePlaylistStr);

        if (tableSize > 0) {
            // Alter all playlist tracks playlist to "*"
            for (int trackIndex = 0; trackIndex < tableSize; trackIndex++) {
                System.out.printf("Removing %s from %s%n", trackTableView.getItems().get(trackIndex).getTrackTitleStr(),
                        artistPlaylistListView.getSelectionModel().getSelectedItem());
                trackTableView.getItems().get(trackIndex).setPlaylistStr("*");
                trackTableView.refresh();
            }

            // Remove playlist from file
            try {
                tableViewLibrary.setTrackObservableList(trackList);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        artistPlaylistListView.getItems().clear();
        artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());
    }

    private void openExplorer(File file) {
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file.getParentFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          TABLE VIEW MODULES
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

    @FXML
    private void handleTableViewContextMenu()  {
        // TODO => create context menu options
        ContextMenu contextMenu = new ContextMenu();

        // Playlist Options
        ArrayList<MenuItem> playlistMenuList = new ArrayList<>();
        Menu addTrackToPlaylist = new Menu("Add to Playlist");
        MenuItem removeTrackFromPlaylist = new MenuItem("Remove from Playlist");
        SeparatorMenuItem divider1 = new SeparatorMenuItem();

        // Add playlists to menu
        for (String playlist : listViewLibrary.getPlaylistArray()) {
            playlistMenuList.add(new MenuItem(playlist));
        }

        addTrackToPlaylist.getItems().addAll(playlistMenuList);
        //TODO => Implement

        // Edit track data
        Menu editTrack = new Menu("Edit Track");
        MenuItem editArtistName = new MenuItem("Artist Name");
        MenuItem editTrackTitle = new MenuItem("Track Title");
        MenuItem editAlbumTitle = new MenuItem("Album Title");
        MenuItem editTrackGenre = new MenuItem("Genre");

        MenuItem deleteTrack = new MenuItem("Delete Track");
        SeparatorMenuItem divider2 = new SeparatorMenuItem();

        editTrack.getItems().addAll(editArtistName, editTrackTitle, editAlbumTitle, editTrackGenre);

        // Explorer/Properties items
        MenuItem openInExplorer = new MenuItem("Open in Explorer");

        // Add track to Playlist
        addTrackToPlaylist.setOnAction(event ->  {
            if (tableSize > 0) {
                System.out.printf("Add %s to %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(), ((MenuItem) event.getTarget()).getText());
                trackTableView.getSelectionModel().getSelectedItem().setPlaylistStr(((MenuItem) event.getTarget()).getText());
                System.out.printf("track playlist set to: %s%n", trackTableView.getSelectionModel().getSelectedItem().getPlaylistStr());
                trackTableView.refresh();
                try {
                    tableViewLibrary.setTrackObservableList(trackList);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Remove track from Playlist
        removeTrackFromPlaylist.setOnAction(event -> {
            if (tableSize > 0 && !Objects.equals(trackTableView.getSelectionModel().getSelectedItem().getPlaylistStr(), "*")) {
                System.out.printf("Removing %s from %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(),
                        trackTableView.getSelectionModel().getSelectedItem().getPlaylistStr());
                trackTableView.getSelectionModel().getSelectedItem().setPlaylistStr("*");
                trackTableView.refresh();
                try {
                    //TODO => update tableviewlibrary object?
                    tableViewLibrary.setTrackObservableList(trackList);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Edit Artist Name
        editArtistName.setOnAction(event -> {
            EditTrackController editTrackController = new EditTrackController();
            String columnName = "Artist Name";
            String currentTrackTitle = trackTableView.getSelectionModel().getSelectedItem().getArtistNameStr();
            System.out.println(currentTrackTitle);
            try {
                editTrackController.showEditWindow(columnName, currentTrackTitle, trackList, trackTableView,
                        artistPlaylistListView, listViewLibrary, tableViewLibrary);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            trackTableView.refresh();
        });

        // Edit Track Title
        editTrackTitle.setOnAction(event -> {
            EditTrackController editTrackController = new EditTrackController();
            String columnName = "Track Title";
            String currentTrackTitle = trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr();
            System.out.println(currentTrackTitle);
            try {
                editTrackController.showEditWindow(columnName, currentTrackTitle, trackList, trackTableView,
                        artistPlaylistListView, listViewLibrary, tableViewLibrary);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            trackTableView.refresh();
        });

        // Edit Album Title
        editAlbumTitle.setOnAction(event -> {
            EditTrackController editTrackController = new EditTrackController();
            String columnName = "Album Title";
            String currentTrackAlbum = trackTableView.getSelectionModel().getSelectedItem().getAlbumTitleStr();
            System.out.println(currentTrackAlbum);
            try {
                editTrackController.showEditWindow(columnName, currentTrackAlbum, trackList, trackTableView,
                        artistPlaylistListView, listViewLibrary, tableViewLibrary);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            trackTableView.refresh();

        });

        // Edit Genre
        editTrackGenre.setOnAction(event -> {
            EditTrackController editTrackController = new EditTrackController();
            String columnName = "Genre";
            String currentGenre = trackTableView.getSelectionModel().getSelectedItem().getTrackGenreStr();
            System.out.println(currentGenre);
            try {
                editTrackController.showEditWindow(columnName, currentGenre, trackList, trackTableView,
                        artistPlaylistListView, listViewLibrary, tableViewLibrary);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            trackTableView.refresh();
        });

        // Delete Track
        deleteTrack.setOnAction(event -> {
            System.out.printf("Removing %s from %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(),
                    artistPlaylistListView.getSelectionModel().getSelectedItem());
            tableViewLibrary.removeTrack(trackTableView.getSelectionModel().getSelectedItem());
            trackList.setAll(tableViewLibrary.getTrackObservableList());
            trackTableView.refresh();


        });

        // Open in File Explorer
        openInExplorer.setOnAction(event -> {
            File file = new File(trackTableView.getSelectionModel().getSelectedItem().getTrackPathStr());
            if (file.exists()) {
                openExplorer(file);
            }
        });


        contextMenu.getItems().addAll(addTrackToPlaylist, removeTrackFromPlaylist, divider1,
                editTrack, deleteTrack, divider2, openInExplorer
        );

        trackTableView.setContextMenu(contextMenu);

        trackTableView.refresh();

    }


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          SEARCH BAR/LISTVIEW SEARCH MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    private Predicate<Track> createListPredicate() {
        return this::listViewTrackSearch;
    }

    private boolean listViewTrackSearch(Track track) {
        if (track.getPlaylistStr().contains("*") || track.getArtistNameStr().contains(artistNameString)) {
            return track.getArtistNameStr().contains(artistPlaylistListView.getSelectionModel().getSelectedItem());

        } else {
            return track.getPlaylistStr().contains(artistPlaylistListView.getSelectionModel().getSelectedItem());
        }
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
                if (playing && (Objects.equals(artistNameString, artistPlaylistListView.getSelectionModel().getSelectedItem()))) {
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
        mediaPlayer.stop();

        if (dispose) {
            mediaPlayer.dispose();
        }

        playPauseButton.setText("Play");
        playing = false;
        stopped = true;
        setNowPlayingText();
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
        trackTableView.scrollTo(nextTrackIndex);
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
    private void importArtistClicked() throws IOException {
        musicLibrary.importArtist();
        tableViewLibrary.getTrackObservableList().addAll(musicLibrary.getTrackObservableList());
        trackList.setAll(tableViewLibrary.getTrackObservableList());

        addArtistFromImport();
    }

    @FXML
    private void importAlbumClicked() throws IOException {
        musicLibrary.importAlbum();
        tableViewLibrary.getTrackObservableList().addAll(musicLibrary.getTrackObservableList());
        trackList.setAll(tableViewLibrary.getTrackObservableList());

        addArtistFromImport();
    }

    @FXML
    private void importTrackClicked() throws IOException {
        musicLibrary.importTrack();
        tableViewLibrary.addTrack(musicLibrary.getImportedTrack());
        trackList.setAll(tableViewLibrary.getTrackObservableList());

        addArtistFromImport();
    }

    // Add artist name to list view and save to file if not available
    private void addArtistFromImport() throws IOException {
        if (!listViewLibrary.getArtistList().contains(musicLibrary.getArtistNameStr())) {
            listViewLibrary.addArtist(musicLibrary.getArtistNameStr());
            artistPlaylistListView.getItems().clear();
            artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());
        }
    }

    @FXML
    private void settingsClicked() throws IOException {
       SettingsController settingsController = new SettingsController();
       settingsController.showSettingsWindow(artistPlaylistListView, listViewLibrary, trackTableView,
               tableViewLibrary, trackList);
    }

    @FXML
    private void closeClicked() {
        System.exit(0);
    }

}

