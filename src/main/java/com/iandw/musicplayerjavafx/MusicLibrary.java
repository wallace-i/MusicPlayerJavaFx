/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: MusicLibrary
 *      Notes: Initializes Root Directory for music files to be located
 */
package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.nio.file.*;

public class MusicLibrary {

    private static String musicRootDirectory;
    private static ObservableList<String> artistNameCollection;

    /**
     * Function: loadArtistNameCollection => ObservableList<String>
     * @return artistNameCollection => List of strings of artist names in root directory,
     *                                 taken from directory names.
     * @throws IOException
     */
    public static ObservableList<String> loadArtistNameCollection(Path path) throws IOException {
        artistNameCollection = FXCollections.observableArrayList();

        //Path path = Paths.get("C:\\dev\\DemoMusic");
        //Path path = Paths.get("E:\\Music");
        musicRootDirectory = path.toString();

        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {

                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);

                for (Path artistDir : directoryStream) {
                    String artistNameStr = artistDir.toString();
                    artistNameStr = artistNameStr.substring(artistNameStr.lastIndexOf('\\') + 1);
                    artistNameCollection.add(artistNameStr);

                }
            }

        } else {
            System.out.printf("%s does not exist%n", path);
        }

        return artistNameCollection;
    }

    /**
     * Function: getMusicRootDirectory => String
     * @return musicRootDirectory => file path to root
     */
    public static String getMusicRootDirectory() {
        return musicRootDirectory;
    }
}
