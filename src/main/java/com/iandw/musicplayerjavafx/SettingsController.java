package com.iandw.musicplayerjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


public class SettingsController extends AnchorPane {
    @FXML private AnchorPane anchorid;
    @FXML private BorderPane rootMusicDirectoryChooserBorderPane;
    @FXML private Button musicFolder;
    @FXML private Label rootDirectoryLabel;
    private Path rootDirectoryPath;

    public SettingsController() {


    }

    public static void initialize() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(SettingsController.class.getResource("Settings.fxml")));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();


    }

    @FXML
    public void rootDirectoryClicked(MouseEvent mouseClick) {
        DirectoryChooser rootMusicDirectoryChooser = new DirectoryChooser();
        rootMusicDirectoryChooser.setTitle("Select Music Folder");
        rootMusicDirectoryChooser.setInitialDirectory((new File(".")));
        Stage stage = (Stage) anchorid.getScene().getWindow();
        File file = rootMusicDirectoryChooser.showDialog(stage);

        if (file != null) {
            rootDirectoryPath = file.toPath();
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
