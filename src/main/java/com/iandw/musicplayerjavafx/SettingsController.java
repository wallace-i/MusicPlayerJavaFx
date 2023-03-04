/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


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

    private MusicLibrary musicLibrary;
    private TableViewLibrary tableViewLibrary;
    private ListView<String> artistPlaylistListView;
    private ListViewLibrary listViewLibrary;
    private TableView<TrackMetadata> trackTableView;
    private ObservableList<TrackMetadata> trackMetadataList;
    private UserSettings userSettings;

    // ComboBox variables
    final String light = "Light";
    final String dark = "Dark";
    final String blue = "Blue";
    final String green = "Green";
    final String pink = "Pink";

    // CSS File Names
    final String styleLightFileName = "style-light.css";
    final String styleDarkFileName = "style-dark.css";
    final String styleBlueFileName = "style-blue.css";
    final String styleGreenFileName = "style-green.css";
    final String stylePinkFileName = "style-pink.css";

    public void initialize() {
        // Initialize ComboBox for css themes
        ObservableList<String> themesList = FXCollections.observableArrayList(light, dark, blue, green, pink);
        themesComboBox.getItems().addAll(themesList);

        themeSelection();
    }

    private void initializeData(ListView<String> artistNameListView, ListViewLibrary listViewLibrary,
                                TableView<TrackMetadata> trackTableView, TableViewLibrary tableViewLibrary,
                                ObservableList<TrackMetadata> trackMetadataList, MusicLibrary musicLibrary,
                                UserSettings userSettings, String directoryLabel)
    {
        rootDirectoryLabel.setText(directoryLabel);
        themesLabel.setText("Music Player Appearance.");

        // Initialize ComboBox
        String currentTheme = userSettings.getThemeFileNameString();
        switch (currentTheme) {
            case styleLightFileName -> themesComboBox.setValue(light);
            case styleDarkFileName -> themesComboBox.setValue(dark);
        }

        this.trackTableView = trackTableView;
        this.artistPlaylistListView = artistNameListView;
        this.listViewLibrary = listViewLibrary;
        this.tableViewLibrary = tableViewLibrary;
        this.musicLibrary = musicLibrary;
        this.userSettings = userSettings;
        this.trackMetadataList = trackMetadataList;
    }

    public void showSettingsWindow(ListView<String> artistNameListView, ListViewLibrary listViewLibrary,
                                   TableView<TrackMetadata> trackTableView, TableViewLibrary tableViewLibrary,
                                   ObservableList<TrackMetadata> trackMetadataList, MusicLibrary musicLibrary,
                                   UserSettings userSettings, String directoryLabel) throws IOException
    {
        // Load Stage and SettignsController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        SettingsController controller = loader.getController();

        // Initialize SettingsController object member variables
        controller.initializeData(artistNameListView, listViewLibrary, trackTableView, tableViewLibrary,
                trackMetadataList, musicLibrary, userSettings, directoryLabel);

        // Set/Show Stage
        stage.setAlwaysOnTop(true);
        stage.setTitle("Settings");
        stage.setResizable(false);
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
        // Create DirectoryChooser for root Music Directory
        DirectoryChooser rootMusicDirectoryChooser = new DirectoryChooser();
        rootMusicDirectoryChooser.setTitle("Select Music Folder");
        rootMusicDirectoryChooser.setInitialDirectory((new File(".")));

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = rootMusicDirectoryChooser.showDialog(stage);

        if (file != null) {
            Path rootDirectoryPath = file.toPath();

            if (Files.exists(rootDirectoryPath)) {
                rootDirectoryLabel.setText(rootDirectoryPath.toString());

                userSettings.setRootMusicDirectoryString(rootDirectoryPath.toString());

                System.out.println("Initializing metadata");

                // Clear current list file and observable list
                Utils.clearSerializedFiles();
                trackMetadataList.clear();
                tableViewLibrary.clearObservableList();
                listViewLibrary.clearPlaylistArray();

                // Re-initialize with new metadata from new root directory
                musicLibrary.clearMusicLibrary();
                musicLibrary.setRootMusicDirectoryString(rootDirectoryPath.toString());
                musicLibrary.initializeMusicLibrary();

                listViewLibrary = new ListViewLibrary();
                artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

                trackMetadataList.addAll(musicLibrary.getTrackObservableList());
                trackTableView.refresh();
                artistPlaylistListView.refresh();
                System.out.println("Finished initializing.");
                System.out.printf("updated root directory: %s%n", rootDirectoryPath);

            }

        } else {
            rootDirectoryLabel.setText("Select file or directory");
        }
    }

    @FXML
    public void resetLibraryClicked(MouseEvent mouseClick) throws IOException {

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.setAlwaysOnTop(false);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Library");
        alert.setHeaderText("You are about to reset your track metadata and clear playlists.");
        alert.setContentText("Would you like to continue?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Resetting Music Library.");

            // Clear current list file and observable list
            Utils.clearSerializedFiles();
            trackMetadataList.clear();
            tableViewLibrary.clearObservableList();
            listViewLibrary.clearPlaylistArray();

            // Re-initialize with new metadata from new root directory
            musicLibrary.clearMusicLibrary();
            musicLibrary.initializeMusicLibrary();

            listViewLibrary = new ListViewLibrary();
            artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

            trackMetadataList.addAll(musicLibrary.getTrackObservableList());

            trackTableView.refresh();
            artistPlaylistListView.refresh();
            System.out.println("Finished Resetting.");

        }

        stage.setAlwaysOnTop(true);
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
        alert.setHeaderText("You are about to clear all track metadata and playlists.");
        alert.setContentText("Would you like to continue?");


        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Clearing Music Library data.");
            System.out.println("Does not remove actual music files or folders from your harddisk.");

            // Clear files
            Utils.clearSerializedFiles();

            // Clear listview and tableview
            artistPlaylistListView.getItems().clear();
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
                case light -> userSettings.setThemeFileNameString(styleLightFileName);
                case dark -> userSettings.setThemeFileNameString(styleDarkFileName);
                case blue -> userSettings.setThemeFileNameString(styleBlueFileName);
                case green -> userSettings.setThemeFileNameString(styleGreenFileName);
                case pink -> userSettings.setThemeFileNameString(stylePinkFileName);
            }

            themesLabel.setText("Restart application to apply new theme");
        });
    }
}
