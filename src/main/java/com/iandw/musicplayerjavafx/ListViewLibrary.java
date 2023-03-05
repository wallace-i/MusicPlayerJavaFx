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
    private ObservableList<String> artistObservableList;
    private ObservableList<String> playlistObservableList;
    private final String noArtists = "* artists *";
    private final String noPlaylists = "* playlists *";
    private boolean outputArtistOnClose;
    private boolean outputPlaylistOnClose;


    public ListViewLibrary() throws IOException {
        // Input artist data if file is not empty
        if (Files.size(Path.of(ResourceURLs.getArtistListURL())) > 0) {
            artistObservableList = FXCollections.observableArrayList(ArtistlistFileIO.inputArtistNameObservableList());

        } else {
            artistObservableList = FXCollections.observableArrayList();
            assert false;
            playlistObservableList.add(noArtists);
        }

        // Input playlist data if file is not empty
        if (Files.size(Path.of(ResourceURLs.getPlaylistsURL())) > 0) {
            playlistObservableList = FXCollections.observableArrayList(PlaylistsFileIO.inputPlaylists());

        } else {
            playlistObservableList = FXCollections.observableArrayList();
            playlistObservableList.add(noPlaylists);
        }
    }

    public void loadObservableListsFromFile() throws IOException {
        // Input artist data if file is not empty
        if (Files.size(Path.of(ResourceURLs.getArtistListURL())) > 0) {
            artistObservableList = FXCollections.observableArrayList(ArtistlistFileIO.inputArtistNameObservableList());

        } else {
            artistObservableList = FXCollections.observableArrayList();
            assert false;
            artistObservableList.add(noArtists);
        }

        // Input playlist data if file is not empty
        if (Files.size(Path.of(ResourceURLs.getPlaylistsURL())) > 0) {
            playlistObservableList = FXCollections.observableArrayList(PlaylistsFileIO.inputPlaylists());

        } else {
            playlistObservableList = FXCollections.observableArrayList();
            playlistObservableList.add(noPlaylists);
        }

    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          ADD / REMOVE ELEMENTS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void addArtist(String artistName) {
        outputArtistOnClose = true;
        // If empty remove empty message
        if (playlistObservableList.contains(noPlaylists)) {
            playlistObservableList.remove(0);
        }

        // Update observable list, sort, and write to file
        artistObservableList.add(artistName);

        Collections.sort(artistObservableList);
    }

    public void removeArtist(String artistName) {
        outputArtistOnClose = true;
        artistObservableList.remove(artistName);

        if (artistObservableList.isEmpty()) {
            artistObservableList.add(noArtists);
        }
    }

    public void addPlaylist(String playlist) {
        outputPlaylistOnClose = true;
        // If empty remove empty message
        if (playlistObservableList.contains(noPlaylists)) {
            playlistObservableList.remove(0);
        }

        // Update observable list, sort, and write to file
        playlistObservableList.add(playlist);
        Collections.sort(playlistObservableList);
    }
    public void removePlaylist(String playlist) {
        outputPlaylistOnClose = true;
        playlistObservableList.remove(playlist);

        if (playlistObservableList.isEmpty()) {
            playlistObservableList.add(noPlaylists);
        }

    }

    public void clearObservableLists() {
        outputArtistOnClose = true;
        outputPlaylistOnClose = true;

        artistObservableList.clear();
        playlistObservableList.clear();
    }

    public void onClose() {
        if (outputArtistOnClose) {
            ArtistlistFileIO.outputArtistNameObservableList(artistObservableList);
        }

        if (outputPlaylistOnClose) {
            PlaylistsFileIO.outputPlaylists(playlistObservableList);
        }

    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS / SETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<String> getArtistObservableList() { return artistObservableList; }
    public ObservableList<String> getPlaylistObservableList() { return playlistObservableList; }



}
