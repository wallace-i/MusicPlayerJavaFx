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
import java.util.*;

public class ListViewLibrary implements Serializable {
    ObservableList<String> listViewObservableList;
    ObservableList<String> artistList;

    public ListViewLibrary() {
        listViewObservableList = FXCollections.observableArrayList();
        artistList = FXCollections.observableArrayList(ArtistlistFileIO.inputArtistNameObservableList());
    };

    public ListViewLibrary(ObservableList<String> artistNameObservableList) {
        this.listViewObservableList = artistNameObservableList;
    }

    public ObservableList<String> loadListViewObservableList(ArrayList<String> playlistArray) {

        // Load user playlists into listview
        listViewObservableList.add("------- Playlists -------");

        if (playlistArray.isEmpty()) {
            listViewObservableList.add("* no playlists *");
        } else {
            listViewObservableList.addAll(playlistArray);
        }
        listViewObservableList.add("------- Artists ---------");

        // Get artist names from artist folders
       // inputArtistNameObservableList();
        listViewObservableList.addAll(artistList);

        return listViewObservableList;
    }

    public void addArtist(String artistName) {
        artistList.add(artistName);
        ArtistlistFileIO.outputArtistNameObservableList(artistList);

    }


//    public void inputArtistNameObservableList() {
//        System.out.println("Reading from artistlist.ser");
//
//        try {
//            // Read from file
//            InputStream in = Files.newInputStream(Path.of(ResourceURLs.getArtistListURL()));
//            ObjectInputStream ois = new ObjectInputStream(in);
//            artistNameArrayList = (ArrayList<String>) ois.readObject();
//            ois.close();
//
//            artistNameObservableList.addAll(artistNameArrayList);
//
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    public void outputArtistNameObservableList() {
//        System.out.println("Writing to artistlist.ser");
//
//        artistNameArrayList.addAll(artistNameObservableList);
//
//        try {
//            // Write track objects to file
//            OutputStream out = Files.newOutputStream(Path.of(ResourceURLs.getArtistListURL()));
//            ObjectOutputStream oos = new ObjectOutputStream(out);
//            oos.writeObject(artistNameArrayList);
//            oos.close();
//
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<String> getListViewObservableList() { return listViewObservableList; }
    public ObservableList<String> getArtistList() { return artistList; }
}
