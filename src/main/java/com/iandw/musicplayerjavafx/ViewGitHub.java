package com.iandw.musicplayerjavafx;

import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ViewGitHub {
    public static void viewGitHub(String URL) {
        WebView webView = new WebView();
        webView.getEngine().load(URL);

        Stage primaryStage = new Stage();
        Scene scene = new Scene(webView, 1000, 780);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
