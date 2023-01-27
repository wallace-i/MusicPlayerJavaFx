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

    private static final String tracklistURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "tracklist.ser")).toString().substring(6));

    private static final String playlistsURL = Objects.requireNonNull(
            Objects.requireNonNull(SettingsController.class.getResource(
                    "playlists.ser")).toString().substring(6));

    // Getters
    public static String getSettingsURL() { return settingsURL; }
    public static String getTrackListURL() { return tracklistURL; }
    public static String getPlaylistsURL() { return playlistsURL; }
}
