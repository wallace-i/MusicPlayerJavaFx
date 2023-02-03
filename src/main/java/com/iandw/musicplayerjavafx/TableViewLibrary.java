package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class TableViewLibrary implements Serializable {

    private final ObservableList<Track> trackObservableList;

    private ArrayList<TrackSerializable> trackArrayList;


    public TableViewLibrary() {
        trackObservableList = FXCollections.observableArrayList();
        trackArrayList = new ArrayList<>();
    }

    public TableViewLibrary(ObservableList<Track> trackObservableList) {
        this.trackObservableList = trackObservableList;
        trackArrayList = new ArrayList<>();
    }



    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          DEEP COPY MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private void deepCopyObservableToArray() {
        for (Track track : trackObservableList) {
            trackArrayList.add(new TrackSerializable(
                    track.getArtistNameStr(),
                    track.getTrackFileNameStr(),
                    track.getTrackContainerTypeStr(),
                    track.getTrackTitleStr(),
                    // track.getAlbumDirectoryStr(),
                    track.getAlbumTitleStr(),
                    track.getTrackGenreStr(),
                    track.getTrackDurationStr(),
                    track.getTrackPathStr(),
                    track.getPlaylistStr()
            ));
        }
    }

    private void deepCopyArrayToObservable() {
        for (TrackSerializable track : trackArrayList) {
            trackObservableList.add(new Track(
                    track.getArtistNameStr(),
                    track.getTrackFileNameStr(),
                    track.getTrackContainerTypeStr(),
                    track.getTrackTitleStr(),
                    //  track.getAlbumDirectoryStr(),
                    track.getAlbumTitleStr(),
                    track.getTrackGenreStr(),
                    track.getTrackDurationStr(),
                    track.getTrackPathStr(),
                    track.getPlaylistStr()
            ));
        }
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          TRACK METADATA READ/WRITE MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */


    public void inputTrackObservableList() {
        System.out.println("Reading Track data from file");

        try {
            // Read from file
            InputStream in = Files.newInputStream(Path.of(ResourceURLs.getTrackListURL()));
            ObjectInputStream ois = new ObjectInputStream(in);
            trackArrayList = (ArrayList<TrackSerializable>) ois.readObject();
            ois.close();

            deepCopyArrayToObservable();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void outputTrackObservableList() {
        System.out.println("Writing to file");

        deepCopyObservableToArray();

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

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS / CLEAR ARRAYS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<Track> getTrackObservableList() { return trackObservableList; }
    public void clearObservableList() { trackObservableList.clear(); }
    public void clearArrayList() { trackArrayList.clear(); }

}
