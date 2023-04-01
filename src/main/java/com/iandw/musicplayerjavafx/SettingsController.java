/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import com.iandw.musicplayerjavafx.Libraries.ListViewLibrary;
import com.iandw.musicplayerjavafx.Libraries.MusicLibrary;
import com.iandw.musicplayerjavafx.Libraries.TableViewLibrary;
import com.iandw.musicplayerjavafx.Utilities.ProgressBarData;
import com.iandw.musicplayerjavafx.Utilities.UserSettings;
import com.iandw.musicplayerjavafx.Utilities.Utils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InterruptedIOException;

import javafx.scene.layout.AnchorPane;


public class SettingsController extends AnchorPane {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button musicFolder;
    @FXML
    private Button resetLibrary;
    @FXML
    private Button clearLibrary;
    @FXML
    private ComboBox<String> themesComboBox;
    @FXML
    private Label rootDirectoryLabel;
    @FXML
    private Label themesLabel;

    private TableView<TrackMetadata> trackTableView;
    private ListView<String> artistListView;
    private ListView<String> playlistListView;
    private MusicLibrary musicLibrary;
    private TableViewLibrary tableViewLibrary;
    private ListViewLibrary listViewLibrary;
    private UserSettings userSettings;

    // ComboBox variables
    final String light = "Light";
    final String dark = "Dark";
    final String console = "Console";
    final String blue = "Blue";
    final String green = "Green";
    final String red = "Red";
    final String pink = "Pink";

    // CSS File Names
    final String styleLightFileName = "style-light.css";
    final String styleDarkFileName = "style-dark.css";
    final String styleBlueFileName = "style-blue.css";
    final String styleGreenFileName = "style-green.css";
    final String styleRedFileName = "style-red.css";
    final String stylePinkFileName = "style-pink.css";
    final String styleConsoleFileName = "style-console.css";

    public void initialize() {
        // Initialize ComboBox for css themes
        ObservableList<String> themesList = FXCollections.observableArrayList(light, dark, console, blue, green, red, pink );
        themesComboBox.getItems().addAll(themesList);

        themeSelection();
    }

    private void initializeData(ListView<String> artistListView, ListView<String> playlistListView,
                                TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                TableViewLibrary tableViewLibrary, MusicLibrary musicLibrary,
                                UserSettings userSettings, String directoryLabel, Stage stage)
    {
        rootDirectoryLabel.setText(directoryLabel);
        themesLabel.setText("Music Player Appearance.");

        // Initialize ComboBox
        String currentTheme = userSettings.getThemeFileNameString();
        switch (currentTheme) {
            case styleLightFileName -> themesComboBox.setValue(light);
            case styleDarkFileName -> themesComboBox.setValue(dark);
            case styleBlueFileName -> themesComboBox.setValue(blue);
            case styleGreenFileName -> themesComboBox.setValue(green);
            case styleRedFileName -> themesComboBox.setValue(red);
            case stylePinkFileName -> themesComboBox.setValue(pink);
            case styleConsoleFileName -> themesComboBox.setValue(console);
        }

        this.artistListView = artistListView;
        this.playlistListView = playlistListView;
        this.trackTableView = trackTableView;
        this.listViewLibrary = listViewLibrary;
        this.tableViewLibrary = tableViewLibrary;
        this.musicLibrary = musicLibrary;
        this.userSettings = userSettings;

        // Close key binding
        stage.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });
    }

    public void showSettingsWindow(ListView<String> artistListView, ListView<String> playlistListView,
                                   TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                   TableViewLibrary tableViewLibrary, MusicLibrary musicLibrary,
                                   UserSettings userSettings, String directoryLabel) throws IOException
    {
        // Load Stage and Settings  Controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        SettingsController controller = loader.getController();

        // Initialize SettingsController object member variables
        controller.initializeData(artistListView, playlistListView, trackTableView, listViewLibrary, tableViewLibrary,
                musicLibrary, userSettings, directoryLabel, stage);

        // Set/Show Stage
        stage.setAlwaysOnTop(true);
        stage.setTitle("Settings");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          MUSIC LIBRARY INITIALIZATION =>
     *                              'Music Folder' BUTTON
     *                              'Reset Library' BUTTON
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public void rootDirectoryClicked(MouseEvent mouseClick) throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();

        // Standard or Recursive initialization chooser
        InitializeSelectionController initializeSelectionController = new InitializeSelectionController();
        initializeSelectionController.showInitializationWindow(musicLibrary, tableViewLibrary, listViewLibrary, userSettings,
                artistListView, playlistListView, trackTableView,  rootDirectoryLabel);

        // write files on close
        listViewLibrary.setOutputListsOnClose();
        tableViewLibrary.setOutputTrackListOnClose();

        stage.setAlwaysOnTop(true);
    }

    @FXML
    public void resetLibraryClicked(MouseEvent mouseClick) throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setAlwaysOnTop(false);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Reset Library");
        alert.setHeaderText("This will only reset the application data\n and will not affect your files or folders.");
        alert.setContentText("Would you like to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Resetting Music Library.");

            // Clear current list file and observable list
            Utils.clearSerializedFiles();
            tableViewLibrary.clearObservableList();
            listViewLibrary.clearObservableLists();

            // Re-initialize with new metadata from new root directory
            musicLibrary.clearMusicLibrary();

            // Holds data for progressbar to update to
            ProgressBarData progressBarData = new ProgressBarData(userSettings.getRootMusicDirectoryString());

            // Run initializeMusicLibrary on separate thread to free up Application Thread
            // for ProgressBarController
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        if (userSettings.getInitalizationString().equals("recursive")) {
                            musicLibrary.recursiveInitialization(progressBarData);
                        } else {
                            musicLibrary.initializeMusicLibrary(progressBarData);
                        }

                        if (Thread.currentThread().isInterrupted()) {
                            throw new InterruptedIOException();
                        }

                        Platform.runLater(() -> loadLibraries());
                        System.out.println("Finished initializing.");

                    } catch (InterruptedIOException consumed) {
                        System.out.println("Cancelled Library Initialization.");
                    }

                    return null;
                }
            };

            // Open progress bar window
            ProgressBarController progressBarController = new ProgressBarController(progressBarData);
            progressBarController.showProgressBarWindow();

            // Re-initialize with new metadata from new root directory
            musicLibrary.clearMusicLibrary();

            // Cancel task thread on Cancel Button clicked
            progressBarData.addPropertyChangeListener(evt -> {
                if (evt.getPropertyName().equals("continueInitialization")) {
                    boolean continueInitialization = (boolean) evt.getNewValue();
                    Platform.runLater(() -> {
                        if (!continueInitialization) {
                            task.cancel();
                            task.setOnCancelled(null);
                            stage.setAlwaysOnTop(true);
                        }
                    });
                }
            });

            task.setOnSucceeded(evt -> {
                progressBarController.close();
                stage.setAlwaysOnTop(true);
            });

            task.setOnFailed(evt -> {
                System.out.println("Initialization Failed.");
                progressBarController.close();
                stage.setAlwaysOnTop(true);
            });

            // Start initializeMusicLibrary() thread
            Thread thread = new Thread(task);
            thread.start();
        }
    }

    private void loadLibraries() {
        listViewLibrary.setArtistObservableList(musicLibrary.getArtistNameObservableList());
        tableViewLibrary.setTrackObservableList(musicLibrary.getTrackObservableList());

        // Set Listview and Tableview
        artistListView.setItems(listViewLibrary.getArtistObservableList());
        playlistListView.setItems(listViewLibrary.getPlaylistObservableList());
        trackTableView.setItems(musicLibrary.getTrackObservableList());

        trackTableView.refresh();
        artistListView.refresh();
        playlistListView.refresh();

        // Write files on close
        listViewLibrary.setOutputListsOnClose();
        tableViewLibrary.setOutputTrackListOnClose();
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          CLEAR LIBRARY BUTTONS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @FXML
    private void clearLibraryClicked() throws IOException {

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setAlwaysOnTop(false);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Library");
        alert.setHeaderText("This will only clear the application data\n and will not affect your files or folders.");
        alert.setContentText("Would you like to continue?");


        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Clearing Music Library data.");

            // Clear files
            Utils.clearSerializedFiles();

            // Clear listview and tableview
            artistListView.getItems().clear();
            playlistListView.getItems().clear();
            tableViewLibrary.clearObservableList();
            tableViewLibrary.createFilteredList();
            trackTableView.setItems(tableViewLibrary.getTrackObservableList());
            trackTableView.refresh();
        }

        stage.setAlwaysOnTop(true);
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          THEMES COMBOBOX
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private void themeSelection() {
        themesComboBox.setOnAction( (event) -> {
            String selectedTheme = themesComboBox.getSelectionModel().getSelectedItem();

            System.out.printf("System theme changed from %s to %s.%n", userSettings.getThemeFileNameString(), selectedTheme);

            switch (selectedTheme) {
                case light   -> userSettings.setThemeFileNameString(styleLightFileName);
                case dark    -> userSettings.setThemeFileNameString(styleDarkFileName);
                case blue    -> userSettings.setThemeFileNameString(styleBlueFileName);
                case green   -> userSettings.setThemeFileNameString(styleGreenFileName);
                case red     -> userSettings.setThemeFileNameString(styleRedFileName);
                case pink    -> userSettings.setThemeFileNameString(stylePinkFileName);
                case console -> userSettings.setThemeFileNameString(styleConsoleFileName);
            }

            themesLabel.setText("Restart application to apply new theme");
        });
    }
}
