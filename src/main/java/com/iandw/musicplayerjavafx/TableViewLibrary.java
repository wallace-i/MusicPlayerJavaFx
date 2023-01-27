/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Objects;

public class TableViewLibrary implements Serializable {

    private ObservableList<Track> trackObservableList;

    private ArrayList<TrackSerializable> trackArrayList;

    public TableViewLibrary() {
        trackObservableList = FXCollections.observableArrayList();
        trackArrayList = new ArrayList<>();

    }

    public void initializeTrackObservableList() throws IOException {
        System.out.println("Initializing observable list");

        String rootMusicDirectoryString = SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL());

        Path rootPath = Paths.get(rootMusicDirectoryString);

        if (Files.exists(rootPath)) {
            if (Files.isDirectory(rootPath)) {

                DirectoryStream<Path> musicDir = Files.newDirectoryStream(rootPath);

                try {
                    for (Path artistFolder : musicDir) {
                        Path artistDirectoryPath = artistFolder.toAbsolutePath();
                        String artistNameStr = artistDirectoryPath.toString().substring(artistDirectoryPath.toString().lastIndexOf(File.separator) + 1);
                        DirectoryStream<Path> artistDir = Files.newDirectoryStream(artistDirectoryPath);


                        for (Path albumFolder : artistDir) {
                            Path albumDirectoryPath = albumFolder.toAbsolutePath();
                            String albumDirectoryStr = albumDirectoryPath.toString().substring(albumDirectoryPath.toString().lastIndexOf(File.separator) + 1);
                            DirectoryStream<Path> albumDirPath = Files.newDirectoryStream(albumDirectoryPath);

                            for (Path trackPath : albumDirPath) {
                                if (Files.exists(trackPath)) {
                                    String audioTrackPathStr = trackPath.toAbsolutePath().toString();
                                    String trackFileName = trackPath.getFileName().toString();
                                    String trackContainerType = audioTrackPathStr.substring(audioTrackPathStr.lastIndexOf('.'));

                                    // Debugger:
                                    // System.out.printf("TrackPath: %s%n", trackPath);
                                    // System.out.printf("audioTrackPathStr: %s%n", audioTrackPathStr);
                                    // System.out.printf("trackFileName: %s%n", trackFileName);
                                    // System.out.printf("trackContainerType: %s%n", trackContainerType);

                                    Media audioTrack = new Media(new File(audioTrackPathStr).toURI().toString());
                                    MediaPlayer mediaPlayer = new MediaPlayer(audioTrack);

                                    String trackTitle = trackFileName;
                                    String trackAlbum;
                                    String trackGenre = (String) mediaPlayer.getMedia().getMetadata().get("genre");

                                    // Check title metadata for null value, if true replace with file name substring
                                    if (mediaPlayer.getMedia().getMetadata().get("title") == null) {
                                        trackTitle = trackFileName.substring(0, trackTitle.indexOf('.'));

                                    } else {
                                        trackTitle = (String) mediaPlayer.getMedia().getMetadata().get("title");
                                    }

                                    // If still null replace with trackFileName
                                    if (trackTitle == null) {
                                        trackTitle = trackFileName;
                                    }

                                    if (Character.isDigit(trackTitle.charAt(0))) {
                                        trackTitle = filterDigitsFromTitle(trackTitle);
                                    }

                                    // Check album metadata for null value, if true replace with directory name
                                    if (mediaPlayer.getMedia().getMetadata().get("album") == null) {
                                        trackAlbum = albumDirectoryStr;
                                    } else {
                                        trackAlbum = (String) mediaPlayer.getMedia().getMetadata().get("album");
                                    }

                                    // Check genre metadata for null value, if true leave blank
                                    if (trackGenre == null) {
                                        trackGenre = "";
                                    } else if (trackGenre.startsWith("(")) {
                                        String trackGenreID = trackGenre.substring(trackGenre.indexOf('(') + 1, trackGenre.indexOf(')'));
                                        trackGenre = ID3v1Genres.getGenre(Integer.parseInt(trackGenreID));
                                    }

                                    // Check for playable file container
                                    if (Objects.equals(trackContainerType.toLowerCase(), ".aif") ||
                                            Objects.equals(trackContainerType.toLowerCase(), ".aiff") ||
                                            Objects.equals(trackContainerType.toLowerCase(), ".mp3") ||
                                            Objects.equals(trackContainerType.toLowerCase(), ".mp4") ||
                                            Objects.equals(trackContainerType.toLowerCase(), ".m4a") ||
                                            Objects.equals(trackContainerType.toLowerCase(), ".wav")) {

                                        // Populate Track object
                                        TrackSerializable serializableTrack = new TrackSerializable(
                                                artistNameStr,
                                                trackFileName,
                                                trackContainerType,
                                                trackTitle,
                                                albumDirectoryStr,
                                                trackAlbum,
                                                trackGenre,
                                                mediaPlayer.getTotalDuration(),
                                                trackPath.toString(),
                                                null
                                        );

                                        // Add track data to ObservableList
                                        trackArrayList.add(serializableTrack);

                                    } else {
                                        System.out.printf("%s is not a compatible file type.", trackFileName);
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Directory cannot be loaded.");
                    e.printStackTrace();
                }

                deepCopyArrayToObservable();

            }

        } else {
            System.out.printf("%s does not exist%n", rootPath);
        }

//        Debugger
//        for (Track i : trackData) {
//            System.out.printf("%s %s %s %s %n",i.getTrackTitleStr(), i.getAlbumTitleStr(), i.getTrackLengthStr(), i.getTrackGenreStr());
//        }

    }

    private void deepCopyArrayToObservable() {
        for (TrackSerializable track : trackArrayList) {
           // System.out.printf("Copying %s%n", track.getTrackFileNameStr());
            trackObservableList.add(new Track(
                    track.getArtistNameStr(),
                    track.getTrackFileNameStr(),
                    track.getTrackContainerTypeStr(),
                    track.getTrackTitleStr(),
                    track.getAlbumDirectoryStr(),
                    track.getAlbumTitleStr(),
                    track.getTrackGenreStr(),
                    track.getTrackDuration(),
                    track.getTrackPathStr(),
                    track.getPlaylistStr()
            ));
        }

    }

    public void inputTrackObservableList() {
        System.out.println("Reading from file");

        try {
            // Read from file
            InputStream in = Files.newInputStream(Path.of(ResourceURLs.getTrackListURL()));
            ObjectInputStream ois = new ObjectInputStream(in);
            trackArrayList = (ArrayList<TrackSerializable>) ois.readObject();
            ois.close();

            deepCopyArrayToObservable();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void outputTrackObservableList() {
        System.out.println("Writing to file");

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

    public ObservableList<Track> getTrackObservableList() { return trackObservableList; }

    public void clearList() {
        System.out.println("Clearing observablelist");
        trackObservableList.clear();
    }


    private String filterDigitsFromTitle(String trackTitle) {
        if (trackTitle.contains(".")) {
            if (trackTitle.contains(" - ")) {
                trackTitle = trackTitle.substring(trackTitle.indexOf('-') + 2,  trackTitle.lastIndexOf('.'));
            } else {
                trackTitle = trackTitle.substring(trackTitle.indexOf(' ') + 1, trackTitle.lastIndexOf('.'));
            }

        } else {
            if (trackTitle.contains(" - ")) {
                trackTitle = trackTitle.substring(trackTitle.indexOf('-') + 2);
            } else {
                trackTitle = trackTitle.substring(trackTitle.indexOf(' ') + 1);
            }

        }


        return trackTitle;
    }


}