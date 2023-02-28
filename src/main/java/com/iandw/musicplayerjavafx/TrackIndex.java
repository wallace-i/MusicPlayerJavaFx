package com.iandw.musicplayerjavafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TrackIndex {
    private final List<Integer> shuffleArray = new ArrayList<>();
    //TODO => implement previous track index array (Stack machine, pop each top index when going backwards)
    private final Stack<Integer> previousIndexArray = new Stack<>();
    private int currentTrackIndex;
    private int nextTrackIndex;
    private int previousTrackIndex;
    private int tableSize;

    public void addToShuffleArray(int randomIndex) {
        shuffleArray.add(randomIndex);
    }
    public void clearShuffleArray() { shuffleArray.clear(); }

    // Setters
    public void setTableSize(int tableSize) { this.tableSize = tableSize; }
    public void setCurrentTrackIndex(int currentTrackIndex) { this.currentTrackIndex = currentTrackIndex; }
    public void setNextTrackIndex(int nextTrackIndex) { this.nextTrackIndex = nextTrackIndex; }
    public void setPreviousTrackIndex(int previousTrackIndex) { this.previousTrackIndex = previousTrackIndex; }

    // Getters
    public List<Integer> getShuffleArray() { return shuffleArray; }
    public int getTableSize() { return tableSize; }
    public int getCurrentTrackIndex() { return currentTrackIndex; }
    public int getNextTrackIndex() { return nextTrackIndex; }
    public int getPreviousTrackIndex() { return previousTrackIndex; }
}
