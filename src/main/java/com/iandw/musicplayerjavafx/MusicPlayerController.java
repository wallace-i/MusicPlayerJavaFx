/**
 *      Author: Ian Wallace, copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: MusicPlayerController
 *      Notes: Contains FXML member variables and controls program interface
 */
package com.iandw.musicplayerjavafx;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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
    private ListView<String> artistPlaylistListView;
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

    private Image musicNotes;
    private MediaPlayer mediaPlayer;
    private MusicLibrary musicLibrary;
    private TableViewLibrary tableViewLibrary;
    private ListViewLibrary listViewLibrary;
    private AutoPlay autoPlay;
    private TrackIndex trackIndex;
    private UserSettings userSettings;
    private Stage stage;

    private String artistNameString;
    private String previousArtistNameString;
    private double volumeDouble;
    private boolean playing;
    private boolean stopped;
    private int albumImageWidth;


    // Default Constructor
    public MusicPlayerController(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          INITIALIZE CONTROLLER ON STARTUP
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void initialize() throws IOException {
        // Initialize variables
        playing = false;
        stopped = true;
        playPauseButton.setGraphic(playIcon);
        volumeIconLabel.setGraphic(volumeUp);
        albumIcon.setOpacity(0);
        artistIcon.setOpacity(0);
        albumImageWidth = 55;
        volumeDouble = 0.25;
        autoPlay = AutoPlay.OFF;
        trackIndex = new TrackIndex();

        // Set TableView column widths
        colTrackTitle.setMaxWidth( 1f * Integer.MAX_VALUE * 40 ); // 40% width
        colAlbumTitle.setMaxWidth( 1f * Integer.MAX_VALUE * 40 );
        colTrackLength.setMaxWidth( 1f * Integer.MAX_VALUE * 6 );
        colTrackGenre.setMaxWidth( 1f * Integer.MAX_VALUE * 14 );

        // Autoplay Icon (all other icons are from bootstrapicons -> musiclibrary.fxml)
        ImageView autoPlayIcon = new ImageView(ResourceURLs.getAutoplayiconURL());
        autoButton.setGraphic(autoPlayIcon);
        autoButton.getGraphic().setTranslateX(2.0);
        autoButton.getGraphic().setOpacity(.65);

        // Album Art default graphic
        musicNotes = new Image(ResourceURLs.getMusicnotesURL());
        imageView.setImage(musicNotes);
        imageView.setCache(true);
        imageView.setVisible(true);

        // Initialize main app objects for Music Library, ListView, and TableView
        musicLibrary = new MusicLibrary(userSettings);
//        musicLibrary.initializeMusicLibrary();
        listViewLibrary = new ListViewLibrary();
        tableViewLibrary = new TableViewLibrary();

        // Initialize Library if tracklist.ser is empty
        if (Files.size(Paths.get(ResourceURLs.getTrackListURL())) == 0) {
            // Choose Root Directory for Music Library
            String directoryLabel = "Welcome, press 'Music Folder' to initialize.";

            SettingsController settingsController = new SettingsController();
            settingsController.showSettingsWindow(artistPlaylistListView, listViewLibrary, trackTableView,
                    tableViewLibrary, tableViewLibrary.getTrackObservableList(), musicLibrary, userSettings, directoryLabel);

        } else {
            // Else initialize data normally from .ser files
            // Playlist and Artist List Data => artistPlaylistListView
            artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

            // Track Metadata => trackTableView
            trackTableView.setItems(tableViewLibrary.loadTrackObservableList());

            // Initialize table view
            artistPlaylistListView.getSelectionModel().select(0);
            trackTableView.refresh();
            listViewSelected();
        }


        /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *
         *                        LISTENERS => MusicPlayerController.java
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
                    tableViewLibrary.getFilteredList().setPredicate(createSearchPredicate(newValue));
                    trackIndex.setTableSize(tableViewLibrary.getFilteredList().size());
                    trackIndex.clearShuffleArray();
                    trackIndex.clearPreviousIndexStack();
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
        tableViewLibrary.createFilteredList();
        tableViewLibrary.getFilteredList().setPredicate(createListPredicate());
        trackTableView.setItems(tableViewLibrary.getFilteredList());
        trackTableView.setVisible(true);
        trackIndex.setTableSize(tableViewLibrary.getFilteredList().size());

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
                listViewController.showListViewInputWindow(tableViewLibrary, trackTableView, artistPlaylistListView,
                        listViewLibrary, trackIndex, windowTitle, menuSelection);

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
                listViewController.showListViewInputWindow(tableViewLibrary, trackTableView, artistPlaylistListView,
                        listViewLibrary, trackIndex, windowTitle, menuSelection);

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
                listViewController.showListViewInputWindow(tableViewLibrary, trackTableView, artistPlaylistListView,
                        listViewLibrary, trackIndex, windowTitle, menuSelection);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Remove Artist or Playlist
        removeListView.setOnAction(event -> {
            String menuSelection = artistPlaylistListView.getSelectionModel().getSelectedItem();

            // TODO => confirm remove choice

            if (listViewLibrary.getPlaylistArray().contains(menuSelection)) {
                removePlaylist(menuSelection);

            } else if (listViewLibrary.getArtistList().contains(menuSelection)) {
                removeArtist(menuSelection);
            }
        });

        // Open in File Explorer
        openInExplorer.setOnAction(event -> {
            String menuSelection = artistPlaylistListView.getSelectionModel().getSelectedItem();
            File file = new File(userSettings.getRootMusicDirectoryString() + File.separator + menuSelection);

            openExplorer(file);
        });

        contextMenu.getItems().addAll(createPlaylist, addArtist, divider1, editListView, divider2, removeListView,
                divider3, openInExplorer);

        artistPlaylistListView.setContextMenu(contextMenu);

    }

    private void removeArtist(String removeArtistStr) {
        listViewLibrary.removeArtist(removeArtistStr);

        int tableSize = trackIndex.getTableSize();

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
                tableViewLibrary.outputTrackObservableList();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        artistPlaylistListView.getItems().clear();
        artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());
    }

    private void removePlaylist(String removePlaylistStr) {
        listViewLibrary.removePlaylist(removePlaylistStr);

        int tableSize = trackIndex.getTableSize();

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
                tableViewLibrary.outputTrackObservableList();
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
            int tableSize = trackIndex.getTableSize();

            if (tableSize > 0) {
                System.out.printf("Add %s to %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(), ((MenuItem) event.getTarget()).getText());
                trackTableView.getSelectionModel().getSelectedItem().setPlaylistStr(((MenuItem) event.getTarget()).getText());
                System.out.printf("track playlist set to: %s%n", trackTableView.getSelectionModel().getSelectedItem().getPlaylistStr());
                trackTableView.refresh();
                try {
                    tableViewLibrary.outputTrackObservableList();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Remove track from Playlist
        removeTrackFromPlaylist.setOnAction(event -> {
            int tableSize = trackIndex.getTableSize();

            if (tableSize > 0 && !Objects.equals(trackTableView.getSelectionModel().getSelectedItem().getPlaylistStr(), "*")) {
                System.out.printf("Removing %s from %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(),
                        trackTableView.getSelectionModel().getSelectedItem().getPlaylistStr());
                trackTableView.getSelectionModel().getSelectedItem().setPlaylistStr("*");
                trackTableView.refresh();
                try {
                    tableViewLibrary.outputTrackObservableList();
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
                editTrackController.showEditWindow(columnName, currentTrackTitle, tableViewLibrary.getTrackObservableList(), trackTableView,
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
                editTrackController.showEditWindow(columnName, currentTrackTitle, tableViewLibrary.getTrackObservableList(), trackTableView,
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
                editTrackController.showEditWindow(columnName, currentTrackAlbum, tableViewLibrary.getTrackObservableList(), trackTableView,
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
                editTrackController.showEditWindow(columnName, currentGenre, tableViewLibrary.getTrackObservableList(), trackTableView,
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
            try {
                tableViewLibrary.outputTrackObservableList();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
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


    private Predicate<TrackMetadata> createListPredicate() {
        return this::listViewTrackSearch;
    }

    private boolean listViewTrackSearch(TrackMetadata trackMetadata) {
        if (trackMetadata.getPlaylistStr().contains("*") || trackMetadata.getArtistNameStr().contains(artistNameString)) {
            return trackMetadata.getArtistNameStr().contains(artistPlaylistListView.getSelectionModel().getSelectedItem());

        } else {
            return trackMetadata.getPlaylistStr().contains(artistPlaylistListView.getSelectionModel().getSelectedItem());
        }
    }

    private Predicate<TrackMetadata> createSearchPredicate(String searchText) {
        return track -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            return searchMetadata(track, searchText);
        };
    }

    private boolean searchMetadata(TrackMetadata trackMetadata, String searchText) {
        return (trackMetadata.getTrackTitleStr().toLowerCase().contains(searchText.toLowerCase()) ||
                trackMetadata.getAlbumTitleStr().toLowerCase().contains(searchText.toLowerCase()) ||
                trackMetadata.getArtistNameStr().toLowerCase().contains(searchText.toLowerCase()) ||
                trackMetadata.getTrackGenreStr().toLowerCase().contains(searchText.toLowerCase()) ||
                trackMetadata.getPlaylistStr().toLowerCase().contains(searchText.toLowerCase())
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
            trackIndex.setPushCurrentTrackToStack(true);

            try {
                if (playing) {
                    mediaPlayer.pause();
                    playPauseButton.setGraphic(playIcon);
                    playing = false;

                } else if (this.mediaPlayer == null) {
                    playMedia();

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
            try {
                if (!playing) {
                    mediaPlayer.play();
                    playPauseButton.setGraphic(pauseIcon);

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
                imageView.setImage(musicNotes);
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
        tableViewLibrary.getTrackObservableList().addAll(musicLibrary.getTrackObservableList());
        tableViewLibrary.outputTrackObservableList();
        addArtistFromImport();
    }

    @FXML
    private void importAlbumClicked() throws IOException {
        musicLibrary.importAlbum();
        tableViewLibrary.getTrackObservableList().addAll(musicLibrary.getTrackObservableList());
        tableViewLibrary.outputTrackObservableList();
        addArtistFromImport();
    }

    @FXML
    private void importTrackClicked() throws IOException {
        musicLibrary.importTrack();

        if (!musicLibrary.getTrackObservableList().isEmpty()) {
            tableViewLibrary.addTrack(musicLibrary.getImportedTrack());
            tableViewLibrary.outputTrackObservableList();
            addArtistFromImport();
        }

    }

    // Add artist name to list view and save to file if not available
    private void addArtistFromImport() {
        if (!listViewLibrary.getArtistList().contains(musicLibrary.getArtistNameStr())) {
            listViewLibrary.addArtist(musicLibrary.getArtistNameStr());
            artistPlaylistListView.getItems().clear();
            artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());
        }
    }

    @FXML
    private void settingsClicked() throws IOException {
        String directoryLabel = userSettings.getRootMusicDirectoryString();
        SettingsController settingsController = new SettingsController();
        settingsController.showSettingsWindow(artistPlaylistListView, listViewLibrary, trackTableView,
               tableViewLibrary, tableViewLibrary.getTrackObservableList(), musicLibrary, userSettings, directoryLabel);
    }

    @FXML
    private void closeClicked() {
        SettingsFileIO.jsonFileOutput(userSettings);
        stage.close();
    }


    // SETTERS
    public void setUserSettings(UserSettings userSettings) { this.userSettings = userSettings;}
    public void setStage(Stage stage) { this.stage = stage; }
}

