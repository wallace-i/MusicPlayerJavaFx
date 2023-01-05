package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MetadataFileIO {
    private static final char separator = '/';

    // Json file for reading metadata directly from file instead of parsing through
    // Media objects in ArtistLibrary
    // [
    //      {"artistName": {"trackTitle": "trackFileName/trackContainerType/trackTitle/albumDirectory/trackAlbum/trackGenre/totalDuration"}}
    // ]
    public static void metadataHashMapOutput(String artistNameString, ArrayList<Track> trackArray) {
        // read to temp file
        JSONArray artistObjectList = new JSONArray();
        System.out.println(trackArray.isEmpty());
        System.out.println("writing to MetadataHashMap.json");
        for (Track track : trackArray) {
            JSONObject metaDataObject = new JSONObject();
            String metaDataString = track.getTrackFileNameStr() + separator + track.getTrackContainerTypeStr() +
                    separator + track.getTrackTitleStr() + separator + track.getAlbumDirectoryStr() + separator +
                    track.getAlbumTitleStr() + separator + track.getTrackGenreStr() + separator + track.getTrackDurationStr();

            metaDataObject.put(track.getTrackTitleStr(), metaDataString);
            System.out.printf("Metadata String: %s", metaDataObject);

            JSONObject artistObject = new JSONObject();
            artistObject.put(artistNameString, metaDataObject);
            System.out.printf("Artist Object: %s", artistObject);

            artistObjectList.add(artistObject);
            System.out.printf("ArtistObjectList: %s", artistObjectList);

        }

        try (FileWriter file = new FileWriter(ResourceURLs.getMetadataHashMapURL())) {
            file.write(artistObjectList.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static ObservableList<Track> metadataHashMapInput(String artistNameString) {
        ObservableList<Track> trackList = FXCollections.observableArrayList();

        return trackList;
    }


}
