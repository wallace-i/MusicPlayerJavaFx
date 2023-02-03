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
    public static ArrayList<String> inputPlaylists() {

      //  ObservableList<String> playlistObservable = FXCollections.observableArrayList();
        ArrayList<String> playlistArray;

        try {
            // Read from file
            System.out.println("Reading playlists from file");
            InputStream in = Files.newInputStream(Path.of(ResourceURLs.getPlaylistsURL()));
            ObjectInputStream ois = new ObjectInputStream(in);
            playlistArray = (ArrayList<String>) ois.readObject();
            ois.close();

            // copy Array to Observable
//            playlistObservable.addAll(playlistArray);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return playlistArray;

    }

    public static void outputPlaylists(ArrayList<String> playlistArray) {

        //ArrayList<String> playlistArray = new ArrayList<>(playlistObservableList);

        try {
            // Write track objects to file
            System.out.println("Writing playlist to file");
            OutputStream out = Files.newOutputStream(Path.of(ResourceURLs.getPlaylistsURL()));
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(playlistArray);
            oos.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
