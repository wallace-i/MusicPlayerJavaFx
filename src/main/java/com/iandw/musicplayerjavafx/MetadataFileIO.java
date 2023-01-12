package com.iandw.musicplayerjavafx;

import javafx.scene.control.ListView;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;


public class MetadataFileIO {

    private static void metadataHashMapOutput(Metadata artistMetadata) throws FileNotFoundException {

        try {
            FileOutputStream fileOut = new FileOutputStream(ResourceURLs.getMetadataHashMapURL());
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);

            for (HashMap<String, Track> trackTitles : artistMetadata.getHashMap().values()) {

                for (Track track : trackTitles.values()) {
                    objOut.writeObject(track);
                }
            }

            objOut.close();
            fileOut.close();
            System.out.printf("Serialized data saved at %s", ResourceURLs.getMetadataHashMapURL());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private static Metadata metadataHashMapInput(Metadata metadataHashMap) {
        HashMap<String, Track> trackHashMap = new HashMap<>();
        Track tempTrack;
        String currentArtist;
        String previousTrackArtist;
        String trackTitle;

        try {
            FileInputStream fileIn = new FileInputStream(ResourceURLs.getMetadataHashMapURL());
            ObjectInputStream inObj = new ObjectInputStream(fileIn);

            // Initialize map, load first track
            tempTrack = (Track) inObj.readObject();
            currentArtist = tempTrack.getArtistNameStr();
            trackTitle = tempTrack.getTrackTitleStr();
            trackHashMap.put(trackTitle, tempTrack);
            metadataHashMap.put(currentArtist, trackHashMap);


            // Load rest of file
            while (fileIn.available() > 0) {

                // Load all tracks of the same artist into trackHashMap
                do {
                    previousTrackArtist = currentArtist;
                    tempTrack = (Track) inObj.readObject();
                    currentArtist = tempTrack.getArtistNameStr();
                    trackTitle = tempTrack.getTrackTitleStr();
                    trackHashMap.put(trackTitle, tempTrack);

                } while (Objects.equals(previousTrackArtist, currentArtist));

                // Load trackHashMap into Artist metadataHashMap
                metadataHashMap.put(currentArtist, trackHashMap);

                // reset map
                trackHashMap.clear();
            }

            inObj.close();
            fileIn.close();

        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return metadataHashMap;
    }

    public static Metadata initializeMetadataFile(String rootMusicDirectoryString, ListView<String> artistNameListView) throws IOException {
        System.out.println("Initializing metadata file");
        Metadata metadataHashMap = new Metadata();
        // gather each track file metadata and copy to Metadata object
        metadataHashMap.setMetadata(rootMusicDirectoryString, artistNameListView);

        // write metadataHashMap to file
        metadataHashMapOutput(metadataHashMap);

        return metadataHashMap;
    }

    public static Metadata inputMetadataFromFile() {
        Metadata metadataHashMap = new Metadata();

        return metadataHashMapInput(metadataHashMap);
    }




}
