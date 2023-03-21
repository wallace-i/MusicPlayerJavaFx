package com.iandw.musicplayerjavafx;

import jakarta.mail.internet.MimeMessage;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;

import javax.swing.text.StyledEditorKit;

public class BugReportController {
    @FXML private AnchorPane anchorPane;
    @FXML private TextField userNameField;
    @FXML private TextField userEmailField;
    @FXML private TextField subjectField;
    @FXML private TextArea textArea;
    @FXML private Button sendButton;
    @FXML private Button consoleLogButton;
    @FXML private Label statusLabel;
    private ByteArrayOutputStream consoleOutput;


    public void initializeData(ByteArrayOutputStream consoleOutput) {
        this.consoleOutput = consoleOutput;
        setTextFieldFocus();
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


    private void setTextFieldFocus() {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);

        // Keeps prompt text readable in text fields
        userNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                anchorPane.requestFocus();
                firstTime.setValue(false);
            }
        });
    }

    @FXML
    private void insertConsoleLogClicked() {
        // Add console log to bottom of text area
        textArea.setText(textArea.getText() + '\n' + consoleOutput.toString());
    }

    @FXML
    private void sendClicked() {

        // Update UI on background thread
        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {
                Platform.runLater(() -> statusLabel.setText("Sending message..."));
                Dotenv dotenv = Dotenv.configure().load();
                final String devEmail = dotenv.get("BUG_REPORT_EMAIL");
                final String token = dotenv.get("TOKEN");

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.starttls.enable", "true"); //TLS
                props.put("mail.smtp.auth", "true");

                // Authenticates via dev bugreport gmail account
                Authenticator authenticator = new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(devEmail, token);
                    }
                };

                Session session = Session.getInstance(props, authenticator);

                try {
                    // Create message
                    MimeMessage message = new MimeMessage(session);
                    message.setRecipients(Message.RecipientType.TO, devEmail);
                    message.setSubject(subjectField.getText());
                    message.setSentDate(new Date());

                    // Add user email to top of bug report
                    message.setText(
                            "From: " + userNameField.getText() + '\n' +
                                    "Email: " + userEmailField.getText() + '\n' +
                                    "App: MusicPlayer" + '\n' +
                                    textArea.getText()
                    );

                    // Send it
                    Transport.send(message);

                    // Update UI on a background thread
                    Platform.runLater(() -> {
                        statusLabel.setText("Message sent!");
                        sendButton.disableProperty().set(true);
                        setTextFieldFocus();
                    });

                } catch (MessagingException mex) {
                    // Update UI on a background thread
                    Platform.runLater(() -> {
                        statusLabel.setText("Send failed.");
                        System.out.println("send failed, exception: " + mex);
                        sendButton.disableProperty().set(false);
                    });
                }

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();

    }

}
