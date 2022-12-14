package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.nio.file.*;

public class MusicLibrary {

    private static String directory;
    private static ObservableList<String> tracks;

    public static void loadData(ListView audioTracksListView) throws IOException {
        tracks = FXCollections.observableArrayList();
        Path path = Paths.get("C:\\Users\\ianda\\IdeaProjects\\MusicPlayerJavaFx\\src\\main\\resources\\com\\iandw\\musicplayerjavafx\\DemoMusic");
        directory = path.toString();
        if (Files.exists(path)) {
            //System.out.printf("%n%s exists%n", path);
            if (Files.isDirectory(path)) {
                //System.out.printf("%nDirectory contents:%n");

                DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);

                for (Path p : directoryStream) {
                    String trackName = p.toString();
                    trackName = trackName.substring(trackName.lastIndexOf('\\') + 1, trackName.indexOf('.'));
                    tracks.add(trackName);
                    System.out.println(p);

                }
            }
        } else {
            System.out.printf("%s does not exist%n", path);
        }
        audioTracksListView.setItems(tracks);

    }

    public static String getLibraryPath() {
        return directory;
    }

    public static String getInitialFileName() { return tracks.get(0); }
}
