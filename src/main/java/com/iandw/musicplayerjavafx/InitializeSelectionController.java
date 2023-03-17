package com.iandw.musicplayerjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InitializeSelectionController {
    @FXML
    private AnchorPane anchorPane;
    @FXML private Button standardButton;
    @FXML private Button recursiveButton;
    @FXML private Button cancelButton;
    @FXML private Label standardLabel;
    @FXML private Label recursiveLabel;

    private MusicLibrary musicLibrary;
    private TableViewLibrary tableViewLibrary;
    private ListViewLibrary listViewLibrary;
    private UserSettings userSettings;
    private ListView<String> artistListView;
    private ListView<String> playlistListView;
    private TableView<TrackMetadata> trackTableView;

    @FXML
    private Label rootDirectoryLabel;
    public void initializeData(MusicLibrary musicLibrary, TableViewLibrary tableViewLibrary,
                               ListViewLibrary listViewLibrary, UserSettings userSettings,
                               ListView<String> artistListView, ListView<String> playlistListView,
                               TableView<TrackMetadata> trackTableView,  Label rootDirectoryLabel)
    {
        this.musicLibrary = musicLibrary;
        this.tableViewLibrary = tableViewLibrary;
        this.listViewLibrary = listViewLibrary;
        this.userSettings = userSettings;
        this.artistListView = artistListView;
        this.playlistListView = playlistListView;
        this.trackTableView = trackTableView;
        this.rootDirectoryLabel = rootDirectoryLabel;

        setLabelText();
    }

    public void showInitializationWindow(MusicLibrary musicLibrary, TableViewLibrary tableViewLibrary,
                                         ListViewLibrary listViewLibrary, UserSettings userSettings,
                                         ListView<String> artistListView, ListView<String> playlistListView,
                                         TableView<TrackMetadata> trackTableView,  Label rootDirectoryLabel) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("initializeselection.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        InitializeSelectionController controller = loader.getController();

        controller.initializeData(musicLibrary, tableViewLibrary, listViewLibrary, userSettings,
                artistListView, playlistListView, trackTableView, rootDirectoryLabel);

        // Set/Show Stage
        stage.setAlwaysOnTop(true);
        stage.setTitle("Initialize Library");
        stage.setResizable(false);
        stage.show();

    }

    private void setLabelText() {
        standardLabel.setText("Best for organized music libraries that follow the file structure: " +
                "Music Folder -> Artist Folder -> Album Folder -> Track.mp3 or " +
                "Music Folder -> Artist Folder -> Track.mp3. " +
                "Gets names and titles from folder and file names.");

        recursiveLabel.setText("Best for unorganized music libraries that can follow any file structure: " +
                "Music Folder -> Folder A -> Folder B -> Folder n -> Track.mp3. " +
                "Gets all track data from the file's metadata. May be inaccurate or missing.");

    }

    @FXML
    private void standardClicked() throws IOException {
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
                userSettings.setInitializationString("standard");
                System.out.println("Initializing metadata");

                // Clear current list file and observable list
                Utils.clearSerializedFiles();
                tableViewLibrary.clearObservableList();
                listViewLibrary.clearObservableLists();

                // Re-initialize with new metadata from new root directory
                musicLibrary.clearMusicLibrary();
                musicLibrary.setRootMusicDirectoryString(rootDirectoryPath.toString());

                // Initialize library by either 'standard' or 'recursive' means.
                musicLibrary.initializeMusicLibrary();

                loadLibraries();

                System.out.println("Finished initializing.");
                System.out.printf("updated root directory: %s%n", rootDirectoryPath);

            }

        } else {
            rootDirectoryLabel.setText("Select file or directory");
        }

        stage.close();
    }

    @FXML
    private void recursiveClicked() throws IOException {
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
                userSettings.setInitializationString("recursive");

                System.out.println("Initializing metadata");

                // Clear current list file and observable list
                Utils.clearSerializedFiles();
                tableViewLibrary.clearObservableList();
                listViewLibrary.clearObservableLists();

                // Re-initialize with new metadata from new root directory
                musicLibrary.clearMusicLibrary();
                musicLibrary.setRootMusicDirectoryString(rootDirectoryPath.toString());

                // Initialize library by either 'standard' or 'recursive' means.
                musicLibrary.recursiveInitialization();

                loadLibraries();

                System.out.println("Finished initializing.");
                System.out.printf("updated root directory: %s%n", rootDirectoryPath);

            }

        } else {
            rootDirectoryLabel.setText("Select file or directory");
        }

        stage.close();
    }

    @FXML
    private void cancelClicked() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
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
    }

}
