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
import java.nio.file.Path;
import java.util.*;

public class ListViewLibrary {
    private ObservableList<String> artistsObservableList;
    private ObservableList<String> playlistsObservableList;
    private String noArtists = "* no artists *";
    private String noPlaylists = "* no playlists *";


    public ListViewLibrary() throws IOException {
        // Input artist data if file is not empty
        if (Files.size(Path.of(ResourceURLs.getArtistListURL())) > 0) {
            artistsObservableList = FXCollections.observableArrayList(ArtistlistFileIO.inputArtistNameObservableList());

        } else {
            artistsObservableList = FXCollections.observableArrayList();
//            assert false;
//            playlistsObservableList.add(noArtists);
        }

        // Input playlist data if file is not empty
        if (Files.size(Path.of(ResourceURLs.getPlaylistsURL())) > 0) {
            playlistsObservableList = FXCollections.observableArrayList(PlaylistsFileIO.inputPlaylists());

        } else {
            playlistsObservableList = FXCollections.observableArrayList();
//            playlistsObservableList.add(noPlaylists);
        }
    }

    public void loadObservableListsFromFile() {
        artistsObservableList = FXCollections.observableArrayList(ArtistlistFileIO.inputArtistNameObservableList());
        playlistsObservableList = FXCollections.observableArrayList(PlaylistsFileIO.inputPlaylists());

    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          ADD / REMOVE ELEMENTS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void addArtist(String artistName) {
        // Remove warning if empty
//        artistsObservableList.remove(noArtists);

        // Update observable list, sort, and write to file
        artistsObservableList.add(artistName);
        Collections.sort(artistsObservableList);
        ArtistlistFileIO.outputArtistNameObservableList(artistsObservableList);
    }

    public void removeArtist(String artistName) {
        artistsObservableList.remove(artistName);
        ArtistlistFileIO.outputArtistNameObservableList(artistsObservableList);
    }

    public void addPlaylist(String playlist) {
        // Remove warning if empty
//        artistsObservableList.remove(noPlaylists);

        // Update observable list, sort, and write to file
        playlistsObservableList.add(playlist);
        Collections.sort(playlistsObservableList);
        PlaylistsFileIO.outputPlaylists(playlistsObservableList);
    }
    public void removePlaylist(String playlist) {
        playlistsObservableList.remove(playlist);
        PlaylistsFileIO.outputPlaylists(playlistsObservableList);
    }

    public void clearObservableLists() {
        artistsObservableList.clear();
        playlistsObservableList.clear();
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS / SETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<String> getArtistObservableList() { return artistsObservableList; }
    public ObservableList<String> getPlaylistsObservableList() { return playlistsObservableList; }


}
