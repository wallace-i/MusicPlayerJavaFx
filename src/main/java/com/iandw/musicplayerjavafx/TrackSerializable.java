package com.iandw.musicplayerjavafx;

import java.io.Serializable;

public class TrackSerializable implements Serializable {
    private final String artistNameStr;
    private final String trackFileNameStr;
    private final String trackContainerTypeStr;
    private final String trackTitleStr;
    private final String albumTitleStr;
    private final String trackDurationStr;
    private final String trackGenreStr;
    private final String trackPathStr;
    private final String playlistStr;

    public TrackSerializable(String artistNameStr,      String trackFileNameStr,    String trackContainerTypeStr,
                             String trackTitleStr,      String albumTitleStr,       String trackGenreStr,
                             String trackDurationStr,   String trackPathStr,        String playlistStr)
    {
        this.artistNameStr = artistNameStr;
        this.trackFileNameStr = trackFileNameStr;
        this.trackContainerTypeStr = trackContainerTypeStr;
        this.trackTitleStr = trackTitleStr;
        this.albumTitleStr = albumTitleStr;
        this.trackDurationStr = trackDurationStr;
        this.trackGenreStr = trackGenreStr;
        this.trackPathStr = trackPathStr;
        this.playlistStr = playlistStr;

    }

    // Getters
    public String getArtistNameStr() { return artistNameStr; }
    public String getTrackFileNameStr() { return trackFileNameStr; }
    public String getTrackContainerTypeStr() { return trackContainerTypeStr; }
    public String getTrackTitleStr() { return trackTitleStr; }
    public String getAlbumTitleStr() { return albumTitleStr; }
    public String getTrackDurationStr() { return trackDurationStr; }
    public String getTrackGenreStr() { return trackGenreStr; }
    public String getTrackPathStr() { return trackPathStr; }
    public String getPlaylistStr() { return playlistStr; }

}
