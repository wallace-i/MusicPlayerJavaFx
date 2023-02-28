/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class SettingsController extends AnchorPane {
    @FXML private AnchorPane anchorPane;
    @FXML private Button musicFolder;
    @FXML private Label rootDirectoryLabel;
    private TableViewLibrary tableViewLibrary;
    private ListView<String> artistNameListView;
    private ListViewLibrary listViewLibrary;
    private TableView<TrackMetadata> trackTableView;
    private ObservableList<TrackMetadata> trackMetadataList;

    public void initialize() {}
    private void initializeData(ListView<String> artistNameListView, ListViewLibrary listViewLibrary,
                                TableView<TrackMetadata> trackTableView, TableViewLibrary tableViewLibrary,
                                ObservableList<TrackMetadata> trackMetadataList)
    {
        rootDirectoryLabel.setText(SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL()));
        this.trackTableView = trackTableView;
        this.artistNameListView = artistNameListView;
        this.listViewLibrary = listViewLibrary;
        this.tableViewLibrary = tableViewLibrary;
        this.trackMetadataList = trackMetadataList;
    }

    public void showSettingsWindow(ListView<String> artistNameListView, ListViewLibrary listViewLibrary,
                                   TableView<TrackMetadata> trackTableView, TableViewLibrary tableViewLibrary, ObservableList<TrackMetadata> trackMetadataList) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        SettingsController controller = loader.getController();
        controller.initializeData(artistNameListView, listViewLibrary, trackTableView, tableViewLibrary, trackMetadataList);

        stage.setTitle("Settings");
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    public void rootDirectoryClicked(MouseEvent mouseClick) throws IOException {
        DirectoryChooser rootMusicDirectoryChooser = new DirectoryChooser();

        rootMusicDirectoryChooser.setTitle("Select Music Folder");
        rootMusicDirectoryChooser.setInitialDirectory((new File(".")));

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        File file = rootMusicDirectoryChooser.showDialog(stage);

        if (file != null) {
            Path rootDirectoryPath = file.toPath();
            analyzePath(rootDirectoryPath);

        } else {
            rootDirectoryLabel.setText("Select file or directory");
        }


    }

    private void analyzePath(Path path) throws IOException {
        // if the file or directory exists, display it
        if (path != null && Files.exists(path)) {
            rootDirectoryLabel.setText(path.toString());

            SettingsFileIO readWriteObject = new SettingsFileIO();
            String rootMusicDirectoryString = path.toString();

            readWriteObject.jsonOutputMusicDirectory(rootMusicDirectoryString);
          //  artistNameListView.setItems(ListViewLibrary.loadArtistNameCollection(rootMusicDirectoryString, playlistArray));

            System.out.printf("updated root directory: %s%n", rootMusicDirectoryString);
            System.out.println("Initializing metadata");

            // Clear current list file and observable list
            Utils.clearSerializedFiles();
            trackMetadataList.clear();
            tableViewLibrary.clearObservableList();
            listViewLibrary.clearPlaylistArray();

            // Re-initialize with new metadata from new root directory
            MusicLibrary musicLibrary = new MusicLibrary();
            musicLibrary.initializeMusicLibrary();
            artistNameListView.setItems(musicLibrary.getArtistNameObservableList());
            trackMetadataList.addAll(musicLibrary.getTrackObservableList());
            trackTableView.refresh();
            artistNameListView.refresh();
            System.out.println("Finished initializing.");

        }
    }
}
