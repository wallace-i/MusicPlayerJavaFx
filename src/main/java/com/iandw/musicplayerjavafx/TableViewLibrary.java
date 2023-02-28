package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TableViewLibrary implements Serializable {
    // Main list to hold all Track objects for TableView
    private ObservableList<Track> trackObservableList;

    // Filtered from trackObservableList from Search Bar or ListView
    private FilteredList<Track> filteredList;

    // Shuffle Function member variables
    private final List<Integer> shuffleArray = new ArrayList<>();
    private int currentTrackIndex;
    private int nextTrackIndex;
    private int previousTrackIndex;
    private int tableSize;

    public TableViewLibrary() {
        trackObservableList = FXCollections.observableArrayList();
    }

    public TableViewLibrary(ObservableList<Track> trackObservableList) {
        this.trackObservableList = trackObservableList;
    }

    public void createFilteredList() {
        //TODO => see if these filtered lists object lifetime is limited or not
        filteredList = new FilteredList<>(FXCollections.observableArrayList(trackObservableList));
    }

    public void addTrack(Track track) throws FileNotFoundException {
        trackObservableList.add(track);
        TracklistFileIO.outputTrackObservableList(trackObservableList);
    }

    public void addToShuffle(int randomIndex) {
        shuffleArray.add(randomIndex);
    }

    public void removeTrack(Track track) {
        trackObservableList.remove(track);
    }

    public void outputTrackObservableList() throws FileNotFoundException {
        TracklistFileIO.outputTrackObservableList(trackObservableList);
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS / SETTERS / CLEAR ARRAYS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void setTrackObservableList(ObservableList<Track> trackObservableList) {
        this.trackObservableList = trackObservableList;
    }
    public void setTableSize(int tableSize) { this.tableSize = tableSize; }
    public void setCurrentTrackIndex(int currentTrackIndex) { this.currentTrackIndex = currentTrackIndex; }
    public void setNextTrackIndex(int nextTrackIndex) { this.nextTrackIndex = nextTrackIndex; }
    public void setPreviousTrackIndex(int previousTrackIndex) { this.previousTrackIndex = previousTrackIndex; }

    public ObservableList<Track> getTrackObservableList() { return trackObservableList; }
    public FilteredList<Track> getFilteredList() { return filteredList; }
    public List<Integer> getShuffleArray() { return shuffleArray; }
    public int getTableSize() { return tableSize; }
    public int getCurrentTrackIndex() { return currentTrackIndex; }
    public int getNextTrackIndex() { return nextTrackIndex; }
    public int getPreviousTrackIndex() { return previousTrackIndex; }

    // Clear arrays
    public void clearObservableList() { trackObservableList.clear(); }
    public void clearShuffleArray() { shuffleArray.clear(); }

}
