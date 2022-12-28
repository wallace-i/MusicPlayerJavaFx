package com.iandw.musicplayerjavafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.util.Objects;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


public class SettingsController extends AnchorPane {
    @FXML private AnchorPane anchorPane;
    @FXML private Button musicFolder;
    @FXML private Label rootDirectoryLabel;

    public void initialize() {
        rootDirectoryLabel.setText(JsonReadWrite.readMusicDirectoryString(
                Objects.requireNonNull(SettingsController.class.getResource("Settings.json"))));

    }

    @FXML
    public void rootDirectoryClicked(MouseEvent mouseClick) {
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

    private void analyzePath(Path path) {
        // if the file or directory exists, display it
        if (path != null && Files.exists(path)) {
            rootDirectoryLabel.setText(path.toString());

            //Settings.setRootDirectoryPath(path);
        }

    }


}
