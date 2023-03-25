package com.iandw.musicplayerjavafx;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProgressBarData implements java.io.Serializable {
    private double progressDouble;
    private String trackPathStr;
    private final PropertyChangeSupport propertySupport;
    private final double fileAmount;
    private double fileIndex;

    public ProgressBarData(String rootDirectory) throws IOException {
        propertySupport = new PropertyChangeSupport(this);

        // Get size of directory
        fileAmount = (double) Files.find(Paths.get(rootDirectory),
                3, (path, attributes) -> attributes.isRegularFile()).count() - 1;

        System.out.println(fileAmount);
        fileIndex = 0.0;
        progressDouble = 0.0;

    }

    public void increaseProgress(String trackPathStr) {
        fileIndex++;
        setProgressDouble(fileIndex / fileAmount);
        setTrackPathStr(trackPathStr);
//        System.out.println(fileIndex);
//        System.out.println(progressDouble);
    }

    public void setTrackPathStr(String newValue) {
        String oldValue = trackPathStr;
        trackPathStr = newValue;
        propertySupport.firePropertyChange("trackPathStr", oldValue, newValue);
    }

    public double getProgressDouble() {
        return progressDouble;
    }

    public void setProgressDouble(double newValue) {
        double oldValue = progressDouble;
        progressDouble = newValue;
//        System.out.println("new value" + newValue);
        propertySupport.firePropertyChange("progressDouble", oldValue, newValue);

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }


}
