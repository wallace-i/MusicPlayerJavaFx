package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.io.*;

public class TableViewLibrary implements Serializable {
    // Main list to hold all Track objects for TableView
    private ObservableList<TrackMetadata> trackMetadataObservableList;

    // Filtered from trackObservableList from Search Bar or ListView
    private FilteredList<TrackMetadata> filteredList;

    public TableViewLibrary() {
        trackMetadataObservableList = FXCollections.observableArrayList();
    }

//    public TableViewLibrary(ObservableList<TrackMetadata> trackMetadataObservableList) {
//        this.trackMetadataObservableList = trackMetadataObservableList;
//    }

    public ObservableList<TrackMetadata> loadTrackObservableList() {
        trackMetadataObservableList = TracklistFileIO.inputTrackObservableList();

        return trackMetadataObservableList;
    }

    public void createFilteredList() {
        //TODO => see if these filtered lists object lifetime is limited or not
        filteredList = new FilteredList<>(FXCollections.observableArrayList(trackMetadataObservableList));
    }

    public void addTrack(TrackMetadata trackMetadata) throws FileNotFoundException {
        trackMetadataObservableList.add(trackMetadata);
    }

    public void removeTrack(TrackMetadata trackMetadata) {
        trackMetadataObservableList.remove(trackMetadata);
    }

    public void outputTrackObservableList() throws FileNotFoundException {
        TracklistFileIO.outputTrackObservableList(trackMetadataObservableList);
    }

    public void clearObservableList() { trackMetadataObservableList.clear(); }

    // Setter
    public void setTrackObservableList(ObservableList<TrackMetadata> trackMetadataObservableList) {
        this.trackMetadataObservableList = trackMetadataObservableList;
    }


    // Getters
    public ObservableList<TrackMetadata> getTrackObservableList() { return trackMetadataObservableList; }
    public FilteredList<TrackMetadata> getFilteredList() { return filteredList; }

}
