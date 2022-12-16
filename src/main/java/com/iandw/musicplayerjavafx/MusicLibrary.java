package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.nio.file.*;

public class MusicLibrary {

    private static String musicRootDirectory;
    private static ObservableList<String> artistNameCollection;


    public static ObservableList<String> loadArtistNameCollection() throws IOException {
        //tracks = FXCollections.observableArrayList();
        artistNameCollection = FXCollections.observableArrayList();

        Path path = Paths.get("C:\\dev\\DemoMusic");
        musicRootDirectory = path.toString();

        if (Files.exists(path)) {
            //System.out.printf("%n%s exists%n", path);
            if (Files.isDirectory(path)) {
                //System.out.printf("%nDirectory contents:%n");

                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);

                for (Path p : directoryStream) {

                    //String trackName = p.toString();
                    //trackName = trackName.substring(trackName.lastIndexOf('\\') + 1, trackName.indexOf('.'));
                    String artistNameStr = p.toString();
                    artistNameStr = artistNameStr.substring(artistNameStr.lastIndexOf('\\') + 1);
                    artistNameCollection.add(artistNameStr);
                    //System.out.println(p);


                }
            }
        } else {
            System.out.printf("%s does not exist%n", path);
        }

        return artistNameCollection;
    }

    public static String getMusicRootDirectory() {
        return musicRootDirectory;
    }

    public static String getInitialFileName() { return artistNameCollection.get(0); }
}
