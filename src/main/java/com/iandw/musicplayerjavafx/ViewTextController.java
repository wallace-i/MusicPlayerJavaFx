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
import org.jsoup.*;
import org.jsoup.nodes.Document;


public class ViewTextController {
    @FXML private TextArea textArea;
    @FXML private Button copyToClipboard;
    private ByteArrayOutputStream consoleOutput;
    private String menuChoice;

    public void initialize() {}

    public void initializeData(String menuChoice, ByteArrayOutputStream consoleOutput) throws IOException {
        this.menuChoice = menuChoice;
        this.consoleOutput = consoleOutput;

        fillTextArea();
    }

    public void showViewTextWindow(String menuChoice, ByteArrayOutputStream consoleOutput) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("viewtext.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        ViewTextController controller = loader.getController();

        controller.initializeData(menuChoice, consoleOutput);

        stage.setAlwaysOnTop(true);
        stage.setTitle(menuChoice);
        stage.setResizable(false);
        stage.show();

    }

    private void fillTextArea() throws IOException {
        final String consoleLog = "Console Log";
        final String about = "About";

        switch (menuChoice) {
            case consoleLog -> viewConsoleLog();
            case about -> viewAbout();
        }
    }

    private void viewConsoleLog() {
        textArea.setText(consoleOutput.toString());
        textArea.setFocusTraversable(false);
    }

    private void viewAbout() throws IOException {
        // Parse README.md from github readme raw file
        Document doc = Jsoup.connect("https://raw.githubusercontent.com/wallace-i/MusicPlayerJavaFx/master/README.md").get();
        String htmlString = doc.toString();
        String cleanString = Jsoup.parse(htmlString).wholeText();

        textArea.setText(cleanString);
        textArea.setFocusTraversable(false);

    }

    @FXML
    private void copyClicked() {
        String textAreaString = textArea.getText();
        StringSelection stringSelection = new StringSelection(textAreaString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

}
