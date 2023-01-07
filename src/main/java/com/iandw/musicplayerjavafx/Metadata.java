package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.HashMap;

public class Metadata extends HashMap {
    //      HashMap<ArtistName, HashMap<TrackTitle, Track object>>
    private HashMap<String, HashMap<String, Track>> metadataHashMap;

    public Metadata() {
        metadataHashMap = null;

    }

    //TODO => Initiailze function
    // have metadata re-initialize when directory changes in settings so only .ser file
    // is enough for app.

    // Setter
    public void setTrackMetadata(Track track, String artistName) {

        metadataHashMap.get(artistName).put(track.getTrackTitleStr(), track);
    }

    public void setMetadata(ObservableList<Track> trackList, String artistName) {
        metadataHashMap = new HashMap<>();
        HashMap<String, Track> trackTempMap = new HashMap<>();

        for (Track track : trackList) {
            trackTempMap.put(track.getTrackTitleStr(), track);
        }

        metadataHashMap.put(artistName, trackTempMap);
    }

    // Getter
    public HashMap<String, HashMap<String, Track>> getMetadata() {
        return metadataHashMap;
    }
    public ObservableList<Track> getTrackList(String artistNameString) {
        ObservableList<Track> trackList = FXCollections.observableArrayList();


        return trackList;
    };
}
