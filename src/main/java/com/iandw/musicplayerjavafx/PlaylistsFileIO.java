/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class PlaylistsFileIO implements Serializable {
    public static ObservableList<String> inputPlaylists() {

        ArrayList<String> playlistArray;
        ObservableList<String> playlistObservableList = FXCollections.observableArrayList();

        try {
            // Read from file
            System.out.println("Reading from playlists.ser");
            InputStream in = Files.newInputStream(Path.of(ResourceURLs.getPlaylistsURL()));
            ObjectInputStream ois = new ObjectInputStream(in);
            playlistArray = (ArrayList<String>) ois.readObject();
            ois.close();

            playlistObservableList.addAll(playlistArray);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return playlistObservableList;

    }

    public static void outputPlaylists(ObservableList<String> playlistsObservableList) {

        ArrayList<String> playlistsArrayList = new ArrayList<>(playlistsObservableList);

        try {
            // Write track objects to file
            System.out.println("Writing to playlists.ser");
            OutputStream out = Files.newOutputStream(Path.of(ResourceURLs.getPlaylistsURL()));
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(playlistsArrayList);
            oos.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
