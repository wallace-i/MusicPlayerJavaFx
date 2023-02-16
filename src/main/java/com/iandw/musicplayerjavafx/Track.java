/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: Track
 *      Notes: Holds audio File Meta Data for TableView
 */

package com.iandw.musicplayerjavafx;

import javafx.beans.property.SimpleStringProperty;

public class Track {
    private SimpleStringProperty artistNameStr;
    private final SimpleStringProperty trackFileNameStr;
    private final SimpleStringProperty trackContainerTypeStr;
    private SimpleStringProperty trackTitleStr;
    private SimpleStringProperty albumTitleStr;
    private final SimpleStringProperty trackDurationStr;
    private SimpleStringProperty trackGenreStr;
    private SimpleStringProperty trackPathStr;
    private SimpleStringProperty playlistStr;

    public Track(String artistNameStr, String trackFileNameStr, String trackContainerTypeStr, String trackTitleStr,
                 String albumTitleStr, String trackGenreStr, String trackDurationStr,
                 String trackPathStr, String playlistStr)
    {
        this.artistNameStr = new SimpleStringProperty(artistNameStr);
        this.trackFileNameStr = new SimpleStringProperty(trackFileNameStr);
        this.trackContainerTypeStr = new SimpleStringProperty(trackContainerTypeStr);
        this.trackTitleStr = new SimpleStringProperty(trackTitleStr);
        this.albumTitleStr = new SimpleStringProperty(albumTitleStr);
        this.trackGenreStr = new SimpleStringProperty(trackGenreStr);
        this.trackDurationStr = new SimpleStringProperty(trackDurationStr);
        this.trackPathStr = new SimpleStringProperty(trackPathStr);
        this.playlistStr = new SimpleStringProperty(playlistStr);
    }

    public String getArtistNameStr() { return artistNameStr.get(); }
    public String getTrackFileNameStr() { return trackFileNameStr.get(); }
    public String getTrackContainerTypeStr() { return trackContainerTypeStr.get(); }
    public String getTrackTitleStr() { return trackTitleStr.get(); }
    public String getAlbumTitleStr() { return albumTitleStr.get(); }
    public String getTrackDurationStr() {return trackDurationStr.get(); }
    public String getTrackGenreStr() { return trackGenreStr.get(); }
    public String getTrackPathStr() { return trackPathStr.get(); }
    public String getPlaylistStr() { return playlistStr.get(); }

    public void setArtistNameStr(String artistNameStr) { this.artistNameStr = new SimpleStringProperty(artistNameStr); }
    public void setTrackTitleStr(String trackTitleStr) { this.trackTitleStr = new SimpleStringProperty(trackTitleStr); }
    public void setAlbumTitleStr(String albumTitleStr) { this.albumTitleStr = new SimpleStringProperty(albumTitleStr); }
    public void setTrackGenreStr(String trackGenreStr) { this.trackGenreStr = new SimpleStringProperty(trackGenreStr); }
    public void setPlaylistStr(String playlistStr) { this.playlistStr = new SimpleStringProperty(playlistStr); }
    public void setTrackPathStr(String trackPathStr) { this.trackPathStr = new SimpleStringProperty(trackPathStr); }
}
