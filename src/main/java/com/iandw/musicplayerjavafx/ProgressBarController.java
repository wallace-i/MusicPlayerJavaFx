package com.iandw.musicplayerjavafx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ProgressBarController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label percentLabel;
    @FXML
    private Label systemTextLabel;
    @FXML
    private Button cancelButton;

    private Stage stage;
    private final ProgressBarData progressBarData;

    public ProgressBarController(ProgressBarData progressBarData) {
        this.progressBarData = progressBarData;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressBar.setProgress(0);
        percentLabel.setText("0%");
        systemTextLabel.setText("Analyzing Directory...");

        // Listener for updated double value from ProgressBarData
        // Where each file read increments the progress bar.
        progressBarData.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("progressDouble")) {
                double progressDouble = (double) evt.getNewValue();
                Platform.runLater(() -> {
                    progressBar.setProgress(progressDouble);
                    percentLabel.setText((int) Math.round(progressDouble * 100) + "%");
                });
            }
        });

        progressBarData.addPropertyChangeListener(evt -> {
            if (evt.getPropertyName().equals("trackPathStr")) {
                String trackPathStr = (String) evt.getNewValue();
                Platform.runLater(() -> {
                    systemTextLabel.setText(trackPathStr);
                });
            }
        });

    }

    public void showProgressBarWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("progressbar.fxml"));
        loader.setControllerFactory(progressBarController -> new ProgressBarController(progressBarData));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));

        setStage(stage);

        // Set/Show Stage
        stage.setAlwaysOnTop(true);
        stage.setTitle("Initialize Library");
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            progressBarData.setContinueInitialization(false);
            stage.close();
        });
    }

    @FXML
    private void cancelButtonClicked() {
        progressBarData.setContinueInitialization(false);
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void setStage(Stage stage) { this.stage = stage; }

    public void close() { stage.close(); }

}
