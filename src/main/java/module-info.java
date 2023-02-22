module com.iandw.musicplayerjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;
    requires java.desktop;
    requires json.simple;
    requires jaudiotagger;
    requires javafx.web;

    opens com.iandw.musicplayerjavafx to javafx.fxml;
    exports com.iandw.musicplayerjavafx;
}