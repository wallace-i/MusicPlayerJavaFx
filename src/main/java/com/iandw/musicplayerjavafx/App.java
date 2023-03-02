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
import java.util.Objects;

public class App extends Application {
    private UserSettings userSettings;
    @Override
    public void start(Stage stage) {
        try {
            // Create UserSettings object to hold settings from JSON file for
            // file I/O on start up and exit.
            userSettings = new UserSettings();

            // Pass userSettings to MusicPlayerController object via fxmlLoader
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("musicplayer.fxml")));
            fxmlLoader.setControllerFactory(musicPlayerController -> new MusicPlayerController(userSettings));
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
                saveAndExit(stage);
            });

        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    public void saveAndExit(Stage stage) {
        SettingsFileIO.jsonFileOutput(userSettings);
        stage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}