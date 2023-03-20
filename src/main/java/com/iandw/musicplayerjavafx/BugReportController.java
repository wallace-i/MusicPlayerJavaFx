package com.iandw.musicplayerjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BugReportController {
    @FXML private TextField devEmailField;

    @FXML private TextField userEmailField;
    @FXML private TextField subjectField;
    @FXML private TextArea textArea;
    @FXML private Button sendButton;
    @FXML private Button consoleLogButton;
    private ByteArrayOutputStream consoleOutput;


    public void initializeData(ByteArrayOutputStream consoleOutput) {
        this.consoleOutput = consoleOutput;

    }

    public void showBugReportWindow(ByteArrayOutputStream consoleOutput) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bugreport.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        BugReportController controller = loader.getController();

        controller.initializeData(consoleOutput);

        stage.setAlwaysOnTop(true);
        stage.setTitle("Bug Report");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void insertConsoleLogClicked() {

    }

    @FXML
    private void sendClicked() {


    }

}
