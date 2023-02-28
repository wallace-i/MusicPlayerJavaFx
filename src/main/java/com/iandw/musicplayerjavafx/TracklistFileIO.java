package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class TracklistFileIO {

    public static ObservableList<TrackMetadata> inputTrackObservableList() {
        System.out.println("Reading from tracklist.ser");

        ObservableList<TrackMetadata> trackMetadataObservableList = FXCollections.observableArrayList();

        try {
            // Read from file
            InputStream in = Files.newInputStream(Path.of(ResourceURLs.getTrackListURL()));
            ObjectInputStream ois = new ObjectInputStream(in);
            ArrayList<TrackSerializable> trackArrayList = (ArrayList<TrackSerializable>) ois.readObject();
            ois.close();

            // Deep copy Array to Observable
            for (TrackSerializable trackSerializable : trackArrayList) {
                trackMetadataObservableList.add(new TrackMetadata(
                        trackSerializable.getArtistNameStr(),
                        trackSerializable.getTrackFileNameStr(),
                        trackSerializable.getTrackContainerTypeStr(),
                        trackSerializable.getTrackTitleStr(),
                        trackSerializable.getAlbumTitleStr(),
                        trackSerializable.getTrackGenreStr(),
                        trackSerializable.getTrackDurationStr(),
                        trackSerializable.getTrackPathStr(),
                        trackSerializable.getPlaylistStr()
                ));
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return trackMetadataObservableList;
    }

    public static void outputTrackObservableList(ObservableList<TrackMetadata> trackMetadataObservableList) throws FileNotFoundException {
        System.out.println("Writing to tracklist.ser");

        ArrayList<TrackSerializable> trackArrayList = new ArrayList<>();

        // Deep copy Observable to Array
        for (TrackMetadata trackMetadata : trackMetadataObservableList) {
            trackArrayList.add(new TrackSerializable(
                    trackMetadata.getArtistNameStr(),
                    trackMetadata.getTrackFileNameStr(),
                    trackMetadata.getTrackContainerTypeStr(),
                    trackMetadata.getTrackTitleStr(),
                    trackMetadata.getAlbumTitleStr(),
                    trackMetadata.getTrackGenreStr(),
                    trackMetadata.getTrackDurationStr(),
                    trackMetadata.getTrackPathStr(),
                    trackMetadata.getPlaylistStr()
            ));
        }

        try {
            // Write track objects to file
            OutputStream out = Files.newOutputStream(Path.of(ResourceURLs.getTrackListURL()));
            ObjectOutputStream oos = new ObjectOutputStream(out);

            oos.writeObject(trackArrayList);
            oos.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
