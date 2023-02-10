package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class TableViewLibrary implements Serializable {

    private ObservableList<Track> trackObservableList;

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
        System.out.println("Reading from tracklist.ser");

        try {
            // Read from file
            InputStream in = Files.newInputStream(Path.of(ResourceURLs.getTrackListURL()));
            ObjectInputStream ois = new ObjectInputStream(in);
            trackArrayList = (ArrayList<TrackSerializable>) ois.readObject();
            ois.close();

            clearObservableList();
            deepCopyArrayToObservable();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void outputTrackObservableList() throws FileNotFoundException {
        System.out.println("Writing to tracklist.ser");

        clearArrayList();
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
     *                          GETTERS / SETTERS / CLEAR ARRAYS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<Track> getTrackObservableList() { return trackObservableList; }
    public void setTrackObservableList(ObservableList<Track> trackObservableList) {
        this.trackObservableList = trackObservableList;
    }
    public void clearObservableList() { trackObservableList.clear(); }
    public void clearArrayList() { trackArrayList.clear(); }

}
