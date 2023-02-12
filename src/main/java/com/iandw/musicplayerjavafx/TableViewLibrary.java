package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;

public class TableViewLibrary implements Serializable {
    private ObservableList<Track> trackObservableList;

    public TableViewLibrary() {
        trackObservableList = FXCollections.observableArrayList();
    }

    public TableViewLibrary(ObservableList<Track> trackObservableList) {
        this.trackObservableList = trackObservableList;
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS / SETTERS / CLEAR ARRAYS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<Track> getTrackObservableList() { return trackObservableList; }
    public void setTrackObservableList(ObservableList<Track> trackObservableList) throws FileNotFoundException {
        this.trackObservableList = trackObservableList;
        TracklistFileIO.outputTrackObservableList(trackObservableList);
    }
    public void clearObservableList() { trackObservableList.clear(); }

}
