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
    private ComboBox<String> themesComboBox;
    @FXML
    private Label rootDirectoryLabel;

    private TableViewLibrary tableViewLibrary;
    private ListView<String> artistPlaylistListView;
    private ListViewLibrary listViewLibrary;
    private TableView<TrackMetadata> trackTableView;
    private ObservableList<TrackMetadata> trackMetadataList;
    private ObservableList<String> themesList;

    public void initialize() {}

    private void initializeData(ListView<String> artistNameListView, ListViewLibrary listViewLibrary,
                                TableView<TrackMetadata> trackTableView, TableViewLibrary tableViewLibrary,
                                ObservableList<TrackMetadata> trackMetadataList, String musicFolderString)
    {
        rootDirectoryLabel.setText(musicFolderString);
        this.trackTableView = trackTableView;
        this.artistPlaylistListView = artistNameListView;
        this.listViewLibrary = listViewLibrary;
        this.tableViewLibrary = tableViewLibrary;
        this.trackMetadataList = trackMetadataList;
    }

    private void initializeThemes() {
        themesList = FXCollections.observableArrayList("Light", "Dark");

        themesComboBox = new ComboBox<>(themesList);

    }

    public void showSettingsWindow(ListView<String> artistNameListView, ListViewLibrary listViewLibrary,
                                   TableView<TrackMetadata> trackTableView, TableViewLibrary tableViewLibrary,
                                   ObservableList<TrackMetadata> trackMetadataList, String musicFolderString) throws IOException
    {
        // Load Stage and SettignsController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        SettingsController controller = loader.getController();

        // Initialize SettingsController object member variables
        controller.initializeData(artistNameListView, listViewLibrary, trackTableView, tableViewLibrary,
                trackMetadataList, musicFolderString);

        initializeThemes();

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
            analyzePath(rootDirectoryPath);
            System.out.printf("updated root directory: %s%n", rootDirectoryPath);

        } else {
            rootDirectoryLabel.setText("Select file or directory");
        }
    }

    @FXML
    public void resetLibraryClicked(MouseEvent mouseClick) throws IOException {
        System.out.println("Resetting Music Library.");

        // Clear current list file and observable list
        Utils.clearSerializedFiles();
        trackMetadataList.clear();
        tableViewLibrary.clearObservableList();
        listViewLibrary.clearPlaylistArray();

        // Re-initialize with new metadata from new root directory
        MusicLibrary musicLibrary = new MusicLibrary();
        musicLibrary.initializeMusicLibrary();

        listViewLibrary = new ListViewLibrary();
        artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

        trackMetadataList.addAll(musicLibrary.getTrackObservableList());

        trackTableView.refresh();
        artistPlaylistListView.refresh();
        System.out.println("Finished Resetting.");
    }

    private void analyzePath(Path path) throws IOException {
        // if the file or directory exists, display it
        if (path != null && Files.exists(path)) {
            rootDirectoryLabel.setText(path.toString());

            SettingsFileIO readWriteObject = new SettingsFileIO();
            String rootMusicDirectoryString = path.toString();

            readWriteObject.jsonOutputMusicDirectory(rootMusicDirectoryString);

            System.out.println("Initializing metadata");

            // Clear current list file and observable list
            Utils.clearSerializedFiles();
            trackMetadataList.clear();
            tableViewLibrary.clearObservableList();
            listViewLibrary.clearPlaylistArray();

            // Re-initialize with new metadata from new root directory
            MusicLibrary musicLibrary = new MusicLibrary();
            musicLibrary.initializeMusicLibrary();

            listViewLibrary = new ListViewLibrary();
            artistPlaylistListView.setItems(listViewLibrary.loadListViewObservableList());

            trackMetadataList.addAll(musicLibrary.getTrackObservableList());
            trackTableView.refresh();
            artistPlaylistListView.refresh();
            System.out.println("Finished initializing.");

        }
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          CLEAR LIBRARY BUTTONS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @FXML
    private void clearLibraryClicked() throws IOException {
        System.out.println("Clearing Music Library data.");
        System.out.println("Does not remove actual music files or folders from your harddisk.");

        // Clear files
        Utils.clearSerializedFiles();

        // Clear
        artistPlaylistListView.getItems().clear();
        trackTableView.getItems().removeAll();

        trackTableView.refresh();
    }




    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          Themes Combo Box
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


}
