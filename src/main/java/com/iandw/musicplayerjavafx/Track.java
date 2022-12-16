package com.iandw.musicplayerjavafx;

import javafx.beans.property.SimpleStringProperty;

import java.time.Duration;

public class Track {
    private final SimpleStringProperty trackTitleStr;
    private final SimpleStringProperty albumTitleStr;
    private final SimpleStringProperty trackNumberStr;
    private final SimpleStringProperty trackGenreStr;
    private final SimpleStringProperty trackLengthStr;

    public Track(String trackTitleStr, String albumTitleStr, String trackNumberStr,
                    String trackGenreStr, String trackLengthStr)
    {
        this.trackTitleStr = new SimpleStringProperty(trackTitleStr);
        this.albumTitleStr = new SimpleStringProperty(albumTitleStr);
        this.trackNumberStr = new SimpleStringProperty(trackNumberStr);
        this.trackGenreStr = new SimpleStringProperty(trackGenreStr);
        this.trackLengthStr = new SimpleStringProperty(trackLengthStr);
    }

    public String getTrackTitleStr() { return trackTitleStr.get(); }
    public String getAlbumTitleStr() { return albumTitleStr.get(); }
    public String getTrackNumberStr() { return trackNumberStr.get(); }
    public String getTrackGenreStr() { return trackGenreStr.get(); }
    public String getTrackLengthStr() { return trackLengthStr.get(); }



}
