/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: Track
 *      Notes: Holds audio File Meta Data for TableView
 */


package com.iandw.musicplayerjavafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.floor;

public class Track {
    private final SimpleStringProperty trackFileNameStr;
    private final SimpleStringProperty trackContainerTypeStr;
    private final SimpleStringProperty trackTitleStr;
    private final SimpleStringProperty albumDirectoryStr;
    private final SimpleStringProperty albumTitleStr;
    private final SimpleStringProperty trackNumberStr;
    private final SimpleStringProperty trackGenreStr;

    private final Duration trackDuration;
    private final SimpleStringProperty trackDurationStr;

    public Track(String trackFileName, String trackContainerType, String trackTitleStr, String albumDirectoryStr,
                 String albumTitleStr, String trackNumberStr, String trackGenreStr, Duration trackDuration)
    {
        this.trackFileNameStr = new SimpleStringProperty(trackFileName);
        this.trackContainerTypeStr = new SimpleStringProperty(trackContainerType);
        this.trackTitleStr = new SimpleStringProperty(trackTitleStr);
        this.albumDirectoryStr = new SimpleStringProperty(albumDirectoryStr);
        this.albumTitleStr = new SimpleStringProperty(albumTitleStr);
        this.trackNumberStr = new SimpleStringProperty(trackNumberStr);
        this.trackGenreStr = new SimpleStringProperty(trackGenreStr);
        this.trackDuration = trackDuration;
        int trackDurationSeconds = (int) trackDuration.toSeconds();
        this.trackDurationStr =  new SimpleStringProperty(formatSeconds(trackDurationSeconds));
    }

    private static String formatSeconds(int seconds) {

        if (seconds >= 3600) {
            return String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60);
        }

        return String.format("%02d:%02d", (seconds / 60) % 60, seconds % 60);

    }


    public String getTrackFileNameStr() { return trackFileNameStr.get(); }
    public String getTrackContainerTypeStr() { return trackContainerTypeStr.get(); }
    public String getTrackTitleStr() { return trackTitleStr.get(); }
    public String getAlbumDirectoryStr() { return albumDirectoryStr.get(); }
    public String getAlbumTitleStr() { return albumTitleStr.get(); }
    public String getTrackNumberStr() { return trackNumberStr.get(); }
    public String getTrackGenreStr() { return trackGenreStr.get(); }
    public Duration getTrackDuration() { return trackDuration; }

    public String getTrackDurationStr() { return trackDurationStr.get(); }



}
