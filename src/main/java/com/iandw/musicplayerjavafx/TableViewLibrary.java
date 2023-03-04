package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class TableViewLibrary {
    // Main list to hold all Track objects for TableView
    private ObservableList<TrackMetadata> trackMetadataObservableList;

    // Filtered from trackObservableList from Search Bar or ListView
    private FilteredList<TrackMetadata> filteredList;

    public TableViewLibrary() throws IOException {

        // Load trackTableView
        if (Files.size(Path.of(ResourceURLs.getTrackListURL())) > 0) {
            trackMetadataObservableList = FXCollections.observableArrayList(TracklistFileIO.inputTrackObservableList());

        } else {
            trackMetadataObservableList = FXCollections.observableArrayList();

        }

    }

    public void loadTrackMetadataObservableListFromFile() {
        trackMetadataObservableList = TracklistFileIO.inputTrackObservableList();
    }

    public void createFilteredList() {
        //TODO => see if these filtered lists object lifetime is limited or not
        filteredList = new FilteredList<>(FXCollections.observableArrayList(trackMetadataObservableList));
    }

    public void addTrack(TrackMetadata trackMetadata) {
        trackMetadataObservableList.add(trackMetadata);
    }

    public void removeTrack(TrackMetadata trackMetadata) {
        trackMetadataObservableList.remove(trackMetadata);
    }

    public void outputTrackObservableList() throws FileNotFoundException {
        TracklistFileIO.outputTrackObservableList(trackMetadataObservableList);
    }

    public void clearObservableList() { trackMetadataObservableList.clear(); }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          SETTERS / GETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void setTrackObservableList(ObservableList<TrackMetadata> trackMetadataObservableList) {
        this.trackMetadataObservableList = trackMetadataObservableList;
    }
    public ObservableList<TrackMetadata> getTrackObservableList() { return trackMetadataObservableList; }
    public FilteredList<TrackMetadata> getFilteredList() { return filteredList; }


}
