/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: MusicLibrary
 *      Notes: Initializes Root Directory for music files to be located
 */
package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class ListViewLibrary implements Serializable {
    ObservableList<String> artistNameObservableList;
    ArrayList<String> artistNameArrayList;

    public ListViewLibrary() {
        artistNameObservableList = FXCollections.observableArrayList();
        artistNameArrayList = new ArrayList<>();
    };

    public ListViewLibrary(ObservableList<String> artistNameObservableList) {
        this.artistNameObservableList = artistNameObservableList;
        artistNameArrayList = new ArrayList<>();
    }

    public ObservableList<String> loadArtistNameObservableList(ArrayList<String> playlistArray) {

        // Load user playlists into listview
        artistNameObservableList.add("------- Playlists -------");

        if (playlistArray.isEmpty()) {
            artistNameObservableList.add("* no playlists *");
        } else {
            artistNameObservableList.addAll(playlistArray);
        }
        artistNameObservableList.add("------- Artists ---------");

        // Get artist names from artist folders
        inputArtistNameObservableList();

        return artistNameObservableList;
    }


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          READ/WRITE MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void inputArtistNameObservableList() {
        System.out.println("Reading Artist Names from file");

        try {
            // Read from file
            InputStream in = Files.newInputStream(Path.of(ResourceURLs.getArtistListURL()));
            ObjectInputStream ois = new ObjectInputStream(in);
            artistNameArrayList = (ArrayList<String>) ois.readObject();
            ois.close();

            artistNameObservableList.addAll(artistNameArrayList);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void outputArtistNameObservableList() {
        System.out.println("Writing to file");

        artistNameArrayList.addAll(artistNameObservableList);

        try {
            // Write track objects to file
            OutputStream out = Files.newOutputStream(Path.of(ResourceURLs.getArtistListURL()));
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(artistNameArrayList);
            oos.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<String> getArtistNameObservableList() { return artistNameObservableList; }

}
