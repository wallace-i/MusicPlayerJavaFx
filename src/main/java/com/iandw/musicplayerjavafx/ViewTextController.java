package com.iandw.musicplayerjavafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ViewTextController {
    @FXML private TextArea textArea;
    @FXML private Button copyToClipboard;
    ByteArrayOutputStream newConsole;
    private String menuChoice;

    public void initialize() {}

    public void initializeData(String menuChoice, ByteArrayOutputStream newConsole) {
        this.menuChoice = menuChoice;
        this.newConsole = newConsole;

        fillTextArea();
    }

    public void showViewTextWindow(String menuChoice, ByteArrayOutputStream newConsole) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("viewtext.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        ViewTextController controller = loader.getController();

        controller.initializeData(menuChoice, newConsole);

        stage.setAlwaysOnTop(true);
        stage.setTitle(menuChoice);
        stage.setResizable(false);
        stage.show();

    }

    private void fillTextArea() {
        final String consoleLog = "Console Log";
        final String about = "About";

        switch (menuChoice) {
            case consoleLog -> viewConsoleLog();
            case about -> viewAbout();
        }


    }

    private void viewConsoleLog() {
        textArea.setText(newConsole.toString());
        textArea.setFocusTraversable(false);
    }

    private void viewAbout() {



    }

    @FXML
    private void copyClicked() {
        String textAreaString = textArea.getText();
        StringSelection stringSelection = new StringSelection(textAreaString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

}
