/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: ResourceURLs.java
 *      Notes: Holds static String objects for quick file location anywhere in the Application
 */

package com.iandw.musicplayerjavafx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class ResourceURLs {

    /**
     *
     *  In .JAR file compile
     *
     */
//    private static final InputStream inSettings = ResourceURLs.class.getClassLoader().getResourceAsStream("/settings.json");
//    private static final BufferedReader readerSettings = new BufferedReader(new InputStreamReader(inSettings));
//    private static final String settingsURL;
//    static {
//        try {
//            settingsURL = readerSettings.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inArtistList = ResourceURLs.class.getClassLoader().getResourceAsStream("/artistlist.ser");
//    private static final BufferedReader readerArtistList = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inArtistList)));
//    private static final String artistlistURL;
//
//    static {
//        try {
//            artistlistURL = readerArtistList.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inTrackList = ResourceURLs.class.getClassLoader().getResourceAsStream("/tracklist.ser");
//    private static final BufferedReader readerTrackList = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inTrackList)));
//    private static final String tracklistURL;
//
//    static {
//        try {
//            tracklistURL = readerTrackList.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inPlaylist = ResourceURLs.class.getClassLoader().getResourceAsStream("/playlistslist.ser");
//    private static final BufferedReader readerPlaylist = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inPlaylist)));
//    private static final String playlistsURL;
//
//    static {
//        try {
//            playlistsURL = readerPlaylist.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inConsoleLog = ResourceURLs.class.getClassLoader().getResourceAsStream("/consolelog.txt");
//    private static final BufferedReader readerConsoleLog= new BufferedReader(new InputStreamReader(Objects.requireNonNull(inConsoleLog)));
//    private static final String consolelogURL;
//
//    static {
//        try {
//            consolelogURL = readerConsoleLog.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inAutoPlay = ResourceURLs.class.getClassLoader().getResourceAsStream("/autoplay2.png");
//    private static final BufferedReader readerAutoPlay = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inAutoPlay)));
//    private static final String autoplayiconURL;
//
//    static {
//        try {
//            autoplayiconURL = readerAutoPlay.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /*
//     *                      DEFAULT ALBUM ART FILES
//     */
//
//    private static final InputStream inNotesLight = ResourceURLs.class.getClassLoader().getResourceAsStream("/musicnotes-light.png");
//    private static final BufferedReader readerNotesLight = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inNotesLight)));
//    private static final String musicnoteslightURL;
//
//    static {
//        try {
//            musicnoteslightURL = readerNotesLight.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inNotesDark = ResourceURLs.class.getClassLoader().getResourceAsStream("/musicnotes-dark.png");
//    private static final BufferedReader readerNotesDark = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inNotesDark)));
//    private static final String musicnotesdarkURL;
//
//    static {
//        try {
//            musicnotesdarkURL = readerNotesDark.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inNotesGreen = ResourceURLs.class.getClassLoader().getResourceAsStream("/musicnotes-green.png");
//    private static final BufferedReader readerNotesGreen= new BufferedReader(new InputStreamReader(Objects.requireNonNull(inNotesGreen)));
//    private static final String musicnotesgreenURL;
//
//    static {
//        try {
//            musicnotesgreenURL = readerNotesGreen.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inNotesBlue = ResourceURLs.class.getClassLoader().getResourceAsStream("/musicnotes-blue.png");
//    private static final BufferedReader readerNotesBlue = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inNotesBlue)));
//    private static final String musicnotesblueURL;
//
//    static {
//        try {
//            musicnotesblueURL = readerNotesBlue.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inNotesRed = ResourceURLs.class.getClassLoader().getResourceAsStream("/musicnotes-red.png");
//    private static final BufferedReader readerNotesRed = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inNotesRed)));
//    private static final String musicnotesredURL;
//
//    static {
//        try {
//            musicnotesredURL = readerNotesRed.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inNotesPink = ResourceURLs.class.getClassLoader().getResourceAsStream("/musicnotes-pink.png");
//    private static final BufferedReader readerNotesPink = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inNotesPink)));
//    private static final String musicnotespinkURL;
//
//    static {
//        try {
//            musicnotespinkURL = readerNotesPink.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static final InputStream inNotesConsole = ResourceURLs.class.getClassLoader().getResourceAsStream("/musicnotes-console.png");
//    private static final BufferedReader readerNotesConsole = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inNotesConsole)));
//    private static final String musicnotesconsoleURL;
//
//    static {
//        try {
//            musicnotesconsoleURL = readerNotesConsole.readLine().substring(10);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }


    /**
     *
     *  In IDE compile
     *
     */
    private static final String settingsURL = Objects.requireNonNull(App.class.getResource(
                    "settings.json")).toString().substring(6);

    private static final String artistlistURL = Objects.requireNonNull(App.class.getResource(
                    "artistlist.ser")).toString().substring(6);

    private static final String tracklistURL = Objects.requireNonNull(App.class.getResource(
                    "tracklist.ser")).toString().substring(6);

    private static final String playlistsURL = Objects.requireNonNull(App.class.getResource(
                    "playlists.ser")).toString().substring(6);

    private static final String consolelogURL = Objects.requireNonNull(App.class.getResource(
                    "consolelog.txt")).toString().substring(6);

    private static final String autoplayiconURL = Objects.requireNonNull(App.class.getResource(
                    "autoplay2.png")).toString().substring(6);


    /*
     *                      DEFAULT ALBUM ART FILES
     */

    private static final String musicnoteslightURL = Objects.requireNonNull(App.class.getResource(
                    "musicnotes-light.png")).toString().substring(6);

    private static final String musicnotesdarkURL = Objects.requireNonNull(App.class.getResource(
                    "musicnotes-dark.png")).toString().substring(6);

    private static final String musicnotesgreenURL = Objects.requireNonNull(App.class.getResource(
                    "musicnotes-green.png")).toString().substring(6);

    private static final String musicnotesblueURL = Objects.requireNonNull(App.class.getResource(
                    "musicnotes-blue.png")).toString().substring(6);

    private static final String musicnotesredURL = Objects.requireNonNull(App.class.getResource(
                    "musicnotes-red.png")).toString().substring(6);

    private static final String musicnotespinkURL = Objects.requireNonNull(App.class.getResource(
                    "musicnotes-pink.png")).toString().substring(6);

    private static final String musicnotesconsoleURL = Objects.requireNonNull(App.class.getResource(
                    "musicnotes-console.png")).toString().substring(6);


    /**
     *
     *                      GETTERS
     *
     */

    public synchronized static String getSettingsURL() { return settingsURL; }
    public static String getArtistListURL() { return artistlistURL; }
    public static String getTrackListURL() { return tracklistURL; }
    public static String getPlaylistsURL() { return playlistsURL; }
    public static String getConsolelogURL() { return consolelogURL; }
    // Image urls
    public static String getAutoplayiconURL() { return autoplayiconURL; }
    public static String getMusicnotesLightURL() { return musicnoteslightURL; }
    public static String getMusicnotesDarkURL() { return musicnotesdarkURL; }
    public static String getMusicnotesGreenURL() { return musicnotesgreenURL; }
    public static String getMusicnotesBlueURL() { return musicnotesblueURL; }
    public static String getMusicnotesRedURL() { return musicnotesredURL; }
    public static String getMusicnotesPinkURL() { return musicnotespinkURL; }
    public static String getMusicnotesConsoleURL() { return musicnotesconsoleURL; }

}
