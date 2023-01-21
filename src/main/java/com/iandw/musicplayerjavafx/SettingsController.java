/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

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
    private ListView<String> artistNameListView;
    private TableView<Track> trackTableView;

    public void initialize() {}
    private void initializeData(ListView<String> artistNameListView, TableView<Track> trackTableView) {
        rootDirectoryLabel.setText(SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL()));
        this.trackTableView = trackTableView;
        this.artistNameListView = artistNameListView;
    }

    public void showSettingsWindow(ListView<String> artistNameListView, TableView<Track> trackTableView) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        SettingsController controller = loader.getController();
        controller.initializeData(artistNameListView, trackTableView);
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
            trackTableView.getItems().clear();

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
            artistNameListView.setItems(MusicLibrary.loadArtistNameCollection(rootMusicDirectoryString));
            System.out.printf("updated root directory: %s%n", rootMusicDirectoryString);
        }

    }


}
