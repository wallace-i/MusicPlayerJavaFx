/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import java.util.Objects;

public class ResourceURLs {
    private static final String settingsURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "settings.json")).toString().substring(6));

    private static final String artistlistURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "artistlist.ser")).toString().substring(6));

    private static final String tracklistURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "tracklist.ser")).toString().substring(6));

    private static final String playlistsURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "playlists.ser")).toString().substring(6));

    private static final String autoplayiconURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "autoplay2.png")).toString().substring(6));

    private static final String musicnotesURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "musicnotes.png")).toString().substring(6));

    private static final String styledefaultURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "style-default.css")).toString().substring(6));


    // Getters
    public static String getSettingsURL() { return settingsURL; }
    public static String getArtistListURL() { return artistlistURL; }
    public static String getTrackListURL() { return tracklistURL; }
    public static String getPlaylistsURL() { return playlistsURL; }
    public static String getAutoplayiconURL() { return autoplayiconURL; }
    public static String getStyledefaultURL() { return styledefaultURL; }
    public static String getMusicnotesURL() { return musicnotesURL; }
}
