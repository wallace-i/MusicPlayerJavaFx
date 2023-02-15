package com.iandw.musicplayerjavafx;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Utils {

    public static String formatSeconds(int seconds) {
        if (seconds >= 3600) {
            return String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60);
        }

        return String.format("%02d:%02d", (seconds / 60) % 60, seconds % 60);
    }

    public static void clearSerializedFiles() throws IOException {
        PrintWriter clearTrackList = new PrintWriter(ResourceURLs.getTrackListURL());
        clearTrackList.close();
        PrintWriter clearArtistList = new PrintWriter(ResourceURLs.getArtistListURL());
        clearArtistList.close();
        PrintWriter clearPlaylists = new PrintWriter(ResourceURLs.getPlaylistsURL());
        clearPlaylists.close();

    }

}
