/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: Track
 *      Notes: Holds audio File Meta Data for TableView
 */

package com.iandw.musicplayerjavafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;

import java.io.Serial;
import java.io.Serializable;


public class Track {
    private final SimpleStringProperty artistNameStr;
    private final SimpleStringProperty trackFileNameStr;
    private final SimpleStringProperty trackContainerTypeStr;
    private final SimpleStringProperty trackTitleStr;
    private final SimpleStringProperty albumDirectoryStr;
    private final SimpleStringProperty albumTitleStr;
    private final Duration trackDuration;
    private final SimpleStringProperty trackDurationStr;
    private final SimpleStringProperty trackGenreStr;
    private final SimpleStringProperty trackPathStr;
    private SimpleStringProperty playlistStr;

    public Track(String artistNameStr, String trackFileNameStr, String trackContainerTypeStr, String trackTitleStr,
                 String albumDirectoryStr, String albumTitleStr, String trackGenreStr, Duration trackDuration,
                 String trackPathStr, String playlistStr)
    {
        this.artistNameStr = new SimpleStringProperty(artistNameStr);
        this.trackFileNameStr = new SimpleStringProperty(trackFileNameStr);
        this.trackContainerTypeStr = new SimpleStringProperty(trackContainerTypeStr);
        this.trackTitleStr = new SimpleStringProperty(trackTitleStr);
        this.albumDirectoryStr = new SimpleStringProperty(albumDirectoryStr);
        this.albumTitleStr = new SimpleStringProperty(albumTitleStr);
        this.trackGenreStr = new SimpleStringProperty(trackGenreStr);
        this.trackDuration = trackDuration;
        int trackDurationSeconds = (int) trackDuration.toSeconds();
        this.trackDurationStr =  new SimpleStringProperty(formatSeconds(trackDurationSeconds));
        this.trackPathStr = new SimpleStringProperty(trackPathStr);
        this.playlistStr = new SimpleStringProperty(playlistStr);
    }

    public static String formatSeconds(int seconds) {
        if (seconds >= 3600) {
            return String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60);
        }

        return String.format("%02d:%02d", (seconds / 60) % 60, seconds % 60);

    }

    public String getArtistNameStr() { return artistNameStr.get(); }
    public String getTrackFileNameStr() { return trackFileNameStr.get(); }
    public String getTrackContainerTypeStr() { return trackContainerTypeStr.get(); }
    public String getTrackTitleStr() { return trackTitleStr.get(); }
    public String getAlbumDirectoryStr() { return albumDirectoryStr.get(); }
    public String getAlbumTitleStr() { return albumTitleStr.get(); }
    public String getTrackDurationStr() {return trackDurationStr.get(); }
    public Duration getTrackDuration() { return trackDuration; }
    public String getTrackGenreStr() { return trackGenreStr.get(); }
    public String getTrackPathStr() { return trackPathStr.get(); }

    public String getPlaylistStr() { return playlistStr.get(); }
    public void setPlaylistStr(String playlist) { this.playlistStr = new SimpleStringProperty(playlist); }

}
