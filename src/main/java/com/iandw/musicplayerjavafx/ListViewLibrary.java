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
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public void addArtist(String artistName) throws IOException {
        artistList.add(artistName);
        Collections.sort(artistList);
        ArtistlistFileIO.outputArtistNameObservableList(artistList);

        // Create new directory if non-existent
        String artistPathStr = SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL())
                + File.separator + artistName;

        File artistDir = new File(artistPathStr);
        if (!artistDir.exists()) {
            Files.createDirectory(Paths.get(artistPathStr));
        }

    }

    public void removeArtist(String artistName) {
        artistList.remove(artistName);
        ArtistlistFileIO.outputArtistNameObservableList(artistList);

    }


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<String> getListViewObservableList() { return listViewObservableList; }
    public ObservableList<String> getArtistList() { return artistList; }
}
