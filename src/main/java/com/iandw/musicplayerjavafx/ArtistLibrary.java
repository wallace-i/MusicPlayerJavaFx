/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: ArtistLibrary
 *      Notes: Populates TableView with music file data
 */

package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.beans.*;
import javafx.util.Duration;

public class ArtistLibrary {

    private static ObservableList<Track> trackData;
    private static int tableSize;


    /**
     * function: loadArtistTableView => ObservableList<Track>
     * @param artistDirectoryPathStr => String from MusicPlayerController
     * @return trackData => Track Object container with track metadata for TableView in MusicPlayerController
     * @throws IOException
     */
    public static ObservableList<Track> loadArtistTableView(String artistDirectoryPathStr) throws IOException {
        trackData = FXCollections.observableArrayList();

        Path currentPath = Paths.get(artistDirectoryPathStr);

        if (Files.exists(currentPath)) {
            //System.out.printf("%n%s exists%n", path);
            if (Files.isDirectory(currentPath)) {
                //System.out.printf("%nDirectory contents:%n");

                DirectoryStream<Path> artistDir = Files.newDirectoryStream(currentPath);

                tableSize = 0;

                for (Path albumFolder : artistDir) {
                    String albumDirectoryPathStr = albumFolder.toString();
                    String albumDirectory = albumDirectoryPathStr.substring(albumDirectoryPathStr.lastIndexOf('\\') + 1);
                    currentPath = Paths.get(albumDirectoryPathStr);
                    DirectoryStream<Path> albumDir = Files.newDirectoryStream(currentPath);
                    //System.out.printf("currentPath: %s%nalbumDirectoryPathStr:%s%n", currentPath, albumDirectoryPathStr);

                    for (Path trackPath : albumDir) {
                        String audioTrackPathStr = trackPath.toString();
                        String trackFileName = audioTrackPathStr.substring(audioTrackPathStr.lastIndexOf('\\') + 1);
                        //String trackContainerType = audioTrackPathStr.substring('.' + 1);
                        String trackContainerType = "mp3";
                        //System.out.printf("audioTrackPathStr: %s%n", audioTrackPathStr);
                        Media audioTrack = new Media(new File(audioTrackPathStr).toURI().toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(audioTrack);
                        String trackTitle;
                        String trackAlbum;

                        mediaPlayer.setOnReady(() -> {
                            Track currentTrack = new Track(
                                trackFileName,
                                trackContainerType,
                                (String) mediaPlayer.getMedia().getMetadata().get("title"),
                                albumDirectory,
                                (String) mediaPlayer.getMedia().getMetadata().get("album"),
                                (Integer) mediaPlayer.getMedia().getMetadata().get("track number"),
                                (String) mediaPlayer.getMedia().getMetadata().get("genre"),
                                mediaPlayer.getTotalDuration()
                            );
                            trackData.add(currentTrack);
                        });

                        tableSize++;

                    }


                }
            }
        } else {
            System.out.printf("%s does not exist%n", currentPath);
        }

//        for (Track i : trackData) {
//            System.out.printf("%s %s %s %s %n",i.getTrackTitleStr(), i.getAlbumTitleStr(), i.getTrackLengthStr(), i.getTrackGenreStr());
//        }

        return trackData;
    }

    public static int getTableSize() { return  tableSize; }

}
