package com.iandw.musicplayerjavafx;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;

public class BugReportController {
    @FXML private TextField userEmailField;
    @FXML private TextField subjectField;
    @FXML private TextArea textArea;
    @FXML private Button sendButton;
    @FXML private Button consoleLogButton;
    @FXML private Label statusLabel;
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
        textArea.setText(consoleOutput.toString());
    }

    @FXML
    private void sendClicked() {
        final String devEmail = "iandw@spectrallines.dev";
        Dotenv dotenv = Dotenv.configure().load();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true"); //TLS
        props.put("mail.smtp.auth", "true");

        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(devEmail, dotenv.get("TOKEN"));
            }
        };

        Session session = Session.getInstance(props, authenticator);


        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userEmailField.getText()));
            message.setRecipients(Message.RecipientType.TO, devEmail);
            message.setSubject(subjectField.getText());
            message.setSentDate(new Date());
            message.setText(textArea.getText());
            Transport.send(message);

            statusLabel.setText("Message sent!");

        } catch (MessagingException mex) {
            statusLabel.setText("Send failed.");
            System.out.println("send failed, exception: " + mex);
        }

    }

}
