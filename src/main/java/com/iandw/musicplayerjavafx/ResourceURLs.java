/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: ResourceURLs.java
 *      Notes: Holds static String objects for quick file location anywhere in the Application
 */

package com.iandw.musicplayerjavafx;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

public class ResourceURLs {

    /**
     *
     *  In IDE compile
     *
     */


    private static final String settingsURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "settings.json")).toString().substring(10);

    private static final String artistlistURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "artistlist.ser")).toString().substring(6);

    private static final String tracklistURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "tracklist.ser")).toString().substring(6);

    private static final String playlistsURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "playlists.ser")).toString().substring(6);

    private static final String consolelogURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "consolelog.txt")).toString().substring(6);
//
    private static final String autoplayiconURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "autoplay2.png")).toString().substring(6);
//
//
//    /*
//     *                      DEFAULT ALBUM ART FILES
//     */
//
    private static final String musicnoteslightURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "musicnotes-light.png")).toString().substring(6);

    private static final String musicnotesdarkURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "musicnotes-dark.png")).toString().substring(6);

    private static final String musicnotesgreenURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "musicnotes-green.png")).toString().substring(6);

    private static final String musicnotesblueURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "musicnotes-blue.png")).toString().substring(6);

    private static final String musicnotesredURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "musicnotes-red.png")).toString().substring(6);

    private static final String musicnotespinkURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "musicnotes-pink.png")).toString().substring(6);

    private static final String musicnotesconsoleURL = Objects.requireNonNull(ResourceURLs.class.getResource(
            "musicnotes-console.png")).toString().substring(6);


    /**
     *
     *                      GETTERS
     *
     */

//    public static String getSettingsURL() throws IOException {
//        String settingsStr = File.separator + "settings.json";
//        InputStream is = ResourceURLs.class.getResourceAsStream(settingsStr);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//        return reader.readLine().substring(6);
//    }
//
//    public static String getArtistListURL() throws IOException {
//        String artistListStr = File.separator + "artistlist.ser";
//
//        return artistListStr.readLine().substring(6);
//    }
//
//    public static String getTrackListURL() throws IOException {
//        String trackListStr = File.separator + "tracklist.ser";
//        InputStream is = ResourceURLs.class.getResourceAsStream(trackListStr);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//        return reader.readLine().substring(6);
//    }
//
//    public static String getPlaylistURL() throws IOException {
//        String playlistsStr = File.separator + "playlists.ser";
//        InputStream is = ResourceURLs.class.getResourceAsStream(playlistsStr);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//        return reader.readLine().substring(6);
//    }
//
//    public static String getConsolelogURL() throws IOException {
//        String consoleLogStr = File.separator + "consolelog.txt";
//        InputStream is = ResourceURLs.class.getResourceAsStream(consoleLogStr);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//
//        return reader.readLine().substring(6);
//    }

    /**
     *
     *  In IDE compile getters
     *
     */
    public static String getSettingsURL() { return settingsURL; }
    public static String getArtistListURL() { return artistlistURL; }
    public static String getTrackListURL() { return tracklistURL; }
    public static String getPlaylistURL() { return playlistsURL; }
    public static String getConsolelogURL() { return consolelogURL; }
//    // Image urls
    public static String getAutoplayiconURL() { return autoplayiconURL; }
    public static String getMusicnotesLightURL() { return musicnoteslightURL; }
    public static String getMusicnotesDarkURL() { return musicnotesdarkURL; }
    public static String getMusicnotesGreenURL() { return musicnotesgreenURL; }
    public static String getMusicnotesBlueURL() { return musicnotesblueURL; }
    public static String getMusicnotesRedURL() { return musicnotesredURL; }
    public static String getMusicnotesPinkURL() { return musicnotespinkURL; }
    public static String getMusicnotesConsoleURL() { return musicnotesconsoleURL; }


}
