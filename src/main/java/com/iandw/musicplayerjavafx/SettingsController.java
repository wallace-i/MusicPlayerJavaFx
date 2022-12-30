package com.iandw.musicplayerjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SettingsController extends AnchorPane {
    @FXML private AnchorPane anchorPane;
    @FXML private Button musicFolder;
    @FXML private Label rootDirectoryLabel;
    private ListView<String> m_artistNameListView;

    public void initialize() {}
    private void initializeData(ListView<String> artistNameListView) {
        rootDirectoryLabel.setText(JsonReadWrite.readMusicDirectoryString(SettingsURL.getSettingsURL()));
        m_artistNameListView = artistNameListView;
    }

    public Stage showSettingsWindow(ListView<String> artistNameListView) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        SettingsController controller = loader.getController();
        controller.initializeData(artistNameListView);
        stage.setTitle("Settings");
        stage.setResizable(false);
        stage.show();

        return stage;
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
            m_artistNameListView.setItems(MusicLibrary.loadArtistNameCollection(rootDirectoryPath.toString()));

        } else {
            rootDirectoryLabel.setText("Select file or directory");
        }


    }

    private void analyzePath(Path path) throws IOException {
        // if the file or directory exists, display it
        if (path != null && Files.exists(path)) {
            rootDirectoryLabel.setText(path.toString());
            JsonReadWrite readWriteObject = new JsonReadWrite();
            readWriteObject.updateRootDirectoryJson(path.toString());
        }

    }


}
