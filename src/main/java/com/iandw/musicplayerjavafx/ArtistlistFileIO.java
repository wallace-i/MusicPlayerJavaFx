package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class ArtistlistFileIO {

    public static ObservableList<String> inputArtistNameObservableList() {
        System.out.println("Reading from artistlist.ser");

        ArrayList<String> artistNameArrayList;
        ObservableList<String> artistNameObservableList = FXCollections.observableArrayList();

        try {
            // Read from file
            InputStream in = Files.newInputStream(Path.of(ResourceURLs.getArtistListURL()));
            ObjectInputStream ois = new ObjectInputStream(in);
            artistNameArrayList = (ArrayList<String>) ois.readObject();
            ois.close();

            artistNameObservableList.addAll(artistNameArrayList);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return artistNameObservableList;
    }

    public static void outputArtistNameObservableList(ObservableList<String> artistNameObservableList) {
        System.out.println("Writing to artistlist.ser");

        ArrayList<String> artistNameArrayList = new ArrayList<>(artistNameObservableList);

        //artistNameArrayList.addAll(artistNameObservableList);

        try {
            // Write track objects to file
            OutputStream out = Files.newOutputStream(Path.of(ResourceURLs.getArtistListURL()));
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(artistNameArrayList);
            oos.close();

        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
