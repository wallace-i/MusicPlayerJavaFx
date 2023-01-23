/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: MusicLibrary
 *      Notes: Initializes Root Directory for music files to be located
 */
package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class ListViewLibrary {

    /**
     * Function: loadArtistNameCollection => ObservableList<String>
     * @return artistNameCollection => List of strings of artist names in root directory,
     *                                 taken from directory names.
     * @throws IOException
     */
    public static ObservableList<String> loadArtistNameCollection(String musicRootDirectoryString) throws IOException {
        ObservableList<String> artistNameCollection = FXCollections.observableArrayList();

        Path path = Paths.get(musicRootDirectoryString);

        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {

                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);

                for (Path artistDir : directoryStream) {
                    String artistNameStr = artistDir.toString();
                    artistNameStr = artistNameStr.substring(artistNameStr.lastIndexOf(File.separator) + 1);
                    artistNameCollection.add(artistNameStr);

                }
            }

        } else {
            System.out.printf("%s does not exist%n", path);
        }

        return artistNameCollection;
    }

}
