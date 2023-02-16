package com.iandw.musicplayerjavafx;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

    public static void moveFile(String source, String destination) throws IOException {
        Files.move(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void createDirectory(String path, String directoryName) throws IOException {
        String newPath = path + File.separator + directoryName;

        File directory = new File(newPath);
        if (!directory.exists()) {
            Files.createDirectory(Paths.get(newPath));
        }

    }

}
