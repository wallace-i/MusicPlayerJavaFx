/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: Track
 *      Notes: Holds audio File Meta Data for TableView
 */

package com.iandw.musicplayerjavafx;

import javafx.beans.property.SimpleStringProperty;

public class Track {
    private final SimpleStringProperty artistNameStr;
    private final SimpleStringProperty trackFileNameStr;
    private final SimpleStringProperty trackContainerTypeStr;
    private final SimpleStringProperty trackTitleStr;
    private final SimpleStringProperty albumTitleStr;
    private final SimpleStringProperty trackDurationStr;
    private final SimpleStringProperty trackGenreStr;
    private final SimpleStringProperty trackPathStr;
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
    public void setPlaylistStr(String playlist) { this.playlistStr = new SimpleStringProperty(playlist); }

}
