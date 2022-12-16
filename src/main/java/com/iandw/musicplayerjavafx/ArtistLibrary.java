package com.iandw.musicplayerjavafx;

import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.media.AudioTrack;
import javafx.scene.media.Media;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class ArtistLibrary {
    private static String audioTrackPathStr;
    private static String albumDirectoryPathStr;
    private static Path currentPath;

    private static ObservableList<Track> trackData;


    public static ObservableList<Track> loadArtistTableView(String artistDirectoryPathStr) throws IOException {
        trackData = FXCollections.observableArrayList();

        currentPath = Paths.get(artistDirectoryPathStr);

        if (Files.exists(currentPath)) {
            //System.out.printf("%n%s exists%n", path);
            if (Files.isDirectory(currentPath)) {
                //System.out.printf("%nDirectory contents:%n");

                DirectoryStream<Path> artistDir = Files.newDirectoryStream(currentPath);

                for (Path albumFolder : artistDir) {
                    String param1, param2, param3, param4, param5;
                    albumDirectoryPathStr = albumFolder.toString();
                    currentPath = Paths.get(albumDirectoryPathStr);
                    DirectoryStream<Path> albumDir = Files.newDirectoryStream(currentPath);
                    System.out.printf("currentPath: %s%nalbumDirectoryPathStr:%s%n", currentPath, albumDirectoryPathStr);

                    for (Path trackPath : albumDir) {
                        audioTrackPathStr = trackPath.toString();
                        System.out.printf("audioTrackPathStr: %s%n", audioTrackPathStr);
                        Media audioTrack = new Media(new File(audioTrackPathStr).toURI().toString());
                        if (audioTrack.getMetadata().isEmpty()) {
                            param1 = "null";
                        } else {
                            param1 = audioTrack.getMetadata().get("title").toString();
                        }
                        Track currentTrack = new Track(param1, "2", "3", "4", "5");



                        trackData.add(currentTrack);



                    }


                }
            }
        } else {
            System.out.printf("%s does not exist%n", currentPath);
        }

        for (Track i : trackData) {
            System.out.printf("%s %s %s %s %n",i.getTrackTitleStr(), i.getAlbumTitleStr(), i.getTrackLengthStr(), i.getTrackGenreStr());
        }

        return trackData;
    }

    public static String getAudioTrackPath() { return audioTrackPathStr; }

    public static String getInitialFileName() { return trackData.get(0).getTrackTitleStr(); }

}
