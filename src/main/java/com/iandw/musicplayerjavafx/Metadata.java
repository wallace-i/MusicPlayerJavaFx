package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Metadata extends HashMap {
    //      HashMap<ArtistName, HashMap<TrackTitle, Track Object>>
    private final HashMap<String, HashMap<String, Track>> metadataHashMap;
    private int numberOfTracks;

    public Metadata() {
        metadataHashMap = new HashMap<>();

    }

    //TODO => Initiailze function
    // have metadata re-initialize when directory changes in settings so only .ser file
    // is enough for app.

    // Setter
    public void setTrackMetadata(Track track, String artistName) {

        //metadataHashMap.get(artistName).put(track.getTrackTitleStr(), track);
    }

    public void setMetadata(String rootMusicDirectoryString, ListView<String> artistNameListView) throws IOException {

        HashMap<String, Track> artistTracksMap = new HashMap<>();

        // Loop through artists
//        for (String artistName : artistNameListView.getItems()) {
//            String currentPath = rootMusicDirectoryString + File.separator + artistName;
//            ArtistLibrary artistLibrary = new ArtistLibrary(currentPath);
//
//            //TODO => empty tracklist bug, hashmap not populating
////            System.out.printf("trackListisEmpty():%s%n", trackList.;
//
//            // Loop through tracks per artist
//            for (Track track : ) {
//                // Populate artistTracksMap with file name and track object for each
//                // track in artist directory
//                artistTracksMap.put(track.getTrackFileNameStr(), track);
//                System.out.printf("metadataputtracktitle:%s%n:", track.getTrackTitleStr());
//            }
//
//            // key => artistName, value => artistTracksMap
//            metadataHashMap.put(artistName, artistTracksMap);
//
//        }

    }

    // Getter
    public HashMap<String, HashMap<String, Track>> getHashMap() {
        return metadataHashMap;
    }

    public ObservableList<Track> getTrackList(String artistNameString) {
        ObservableList<Track> trackList = FXCollections.observableArrayList();

        trackList.addAll(metadataHashMap.get(artistNameString).values());
        numberOfTracks = trackList.size();
        System.out.printf("currentartisttracksamount: %s%n", numberOfTracks);

        return trackList;
    };

    public int getNumberOfTracks() {
        return numberOfTracks;
    }
}
