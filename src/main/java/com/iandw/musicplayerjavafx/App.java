/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.io.FileNotFoundException;
import java.util.Objects;

public class App extends Application {
    private UserSettings userSettings;
    private ListViewLibrary listViewLibrary;
    private TableViewLibrary tableViewLibrary;

    @Override
    public void start(Stage stage) {
        try {
            // Create UserSettings object to hold settings from JSON file for
            // file I/O on start up and exit.
            userSettings = new UserSettings();
            listViewLibrary = new ListViewLibrary();
            tableViewLibrary = new TableViewLibrary();

            // ExecutorService to manage threads
            ExecutorService executorService = Executors.newCachedThreadPool();

            executorService.execute(listViewLibrary);
            executorService.execute(tableViewLibrary);

            // Pass userSettings to MusicPlayerController object via fxmlLoader
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("musicplayer.fxml")));
            fxmlLoader.setControllerFactory(musicPlayerController -> new MusicPlayerController(
                    stage, userSettings, listViewLibrary, tableViewLibrary));

            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(
                    userSettings.getThemeFileNameString())).toExternalForm());

            stage.setTitle("Music Player");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            // Save user settings in on close
            stage.setOnCloseRequest(event -> {
                event.consume();
                try {
                    saveAndExit(stage);

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    public void saveAndExit(Stage stage) throws FileNotFoundException {
        SettingsFileIO.jsonFileOutput(userSettings);
        listViewLibrary.onClose();
        tableViewLibrary.onClose();

        stage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}