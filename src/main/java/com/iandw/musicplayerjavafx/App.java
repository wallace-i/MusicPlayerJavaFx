/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

            // Input music library files and settings via ExecutorService
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.execute(userSettings);
            executorService.execute(listViewLibrary);
            executorService.execute(tableViewLibrary);
            executorService.shutdown();

            // Set console to output text for user to view via Help menu
            ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
            System.setOut(new PrintStream(consoleOutput));

            // HostServices for accessing program GitHub page.
            HostServices hostServices = getHostServices();
            stage.getProperties().put("hostServices", this.getHostServices());

            // Pass top level objects to MusicPlayerController object via fxmlLoader
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("musicplayer.fxml")));
            fxmlLoader.setControllerFactory(musicPlayerController -> new MusicPlayerController(
                    stage, executorService, consoleOutput, userSettings, listViewLibrary, tableViewLibrary));

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
        if (userSettings.getWriteOnClose()) {
            SettingsFileIO.jsonFileOutput(userSettings);
        }

        listViewLibrary.onClose();
        tableViewLibrary.onClose();

        stage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}