/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.FieldKey;

public class TableViewLibrary implements Serializable {
    private final ObservableList<Track> trackObservableList;
    private ArrayList<TrackSerializable> trackArrayList;
    private final List<String> supportedFileTypes;

    public TableViewLibrary() {
        trackObservableList = FXCollections.observableArrayList();
        trackArrayList = new ArrayList<>();
        supportedFileTypes = Arrays.asList(".aif", ".aiff", ".mp3", "mp4", ".m4a", ".wav");
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
                                    String trackPathStr = trackPath.toAbsolutePath().toString();
                                    String trackFileName = trackPath.getFileName().toString();
                                    String trackContainerType = trackPathStr.substring(trackPathStr.lastIndexOf('.'));

                                    // Debugger:
                                    // System.out.printf("TrackPath: %s%n", trackPath);
                                    // System.out.printf("audioTrackPathStr: %s%n", audioTrackPathStr);
                                    // System.out.printf("trackFileName: %s%n", trackFileName);
                                    // System.out.printf("trackContainerType: %s%n", trackContainerType);

                                    try {
                                        File file = new File(trackPathStr);
                                        AudioFile audioFile = AudioFileIO.read(file);
                                        Tag tag = audioFile.getTag();
//
                                        String trackTitle = trackFileName;
                                        String trackAlbum;
                                        String trackGenre = tag.getFirst(FieldKey.GENRE);
                                        String duration = Utils.formatSeconds(audioFile.getAudioHeader().getTrackLength());

                                        // Check title metadata for null value, if true replace with file name substring
                                        if (tag.getFirst(FieldKey.TITLE) == null) {
                                            trackTitle = trackFileName.substring(0, trackTitle.indexOf('.'));

                                        } else {
                                            trackTitle = tag.getFirst(FieldKey.TITLE);
                                        }

                                        // If still null replace with trackFileName
                                        if (trackTitle == null) {
                                            trackTitle = trackFileName;
                                        }

                                        if (Character.isDigit(trackTitle.charAt(0))) {
                                            trackTitle = filterDigitsFromTitle(trackTitle);
                                        }

                                        // Check album metadata for null value, if true replace with directory name
                                        if (tag.getFirst(FieldKey.ALBUM) == null) {
                                            trackAlbum = albumDirectoryStr;
                                        } else {
                                            trackAlbum = tag.getFirst(FieldKey.ALBUM);
                                        }

                                        // Check genre metadata for null value, if true leave blank
                                        if (trackGenre == null) {
                                            trackGenre = "";
                                        } else if (trackGenre.startsWith("(")) {
                                            String trackGenreID = trackGenre.substring(trackGenre.indexOf('(') + 1, trackGenre.indexOf(')'));
                                            trackGenre = ID3v1Genres.getGenre(Integer.parseInt(trackGenreID));
                                        }

                                        // Check for playable file container
                                        if (supportedFileTypes.contains(trackContainerType.toLowerCase())) {

                                            // Populate Track object
                                            Track track = new Track(
                                                    artistNameStr,
                                                    trackFileName,
                                                    trackContainerType,
                                                    trackTitle,
                                                    albumDirectoryStr,
                                                    trackAlbum,
                                                    trackGenre,
                                                    duration,
                                                    trackPathStr,
                                                    null
                                            );

                                            trackObservableList.add(track);

                                        } else {
                                            System.out.printf("%s is not a compatible file type.", trackFileName);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Directory cannot be loaded.");
                    e.printStackTrace();
                }

                // Copy ObservableList to Array and serialize output to trackfile.ser
                outputTrackObservableList();
            }

        } else {
            System.out.printf("%s does not exist%n", rootPath);
        }

//        Debugger
//        for (Track i : trackData) {
//            System.out.printf("%s %s %s %s %n",i.getTrackTitleStr(), i.getAlbumTitleStr(), i.getTrackLengthStr(), i.getTrackGenreStr());
//        }

    }

    private void deepCopyObservableToArray() {
        for (Track track : trackObservableList) {
            trackArrayList.add(new TrackSerializable(
                    track.getArtistNameStr(),
                    track.getTrackFileNameStr(),
                    track.getTrackContainerTypeStr(),
                    track.getTrackTitleStr(),
                    track.getAlbumDirectoryStr(),
                    track.getAlbumTitleStr(),
                    track.getTrackGenreStr(),
                    track.getTrackDurationStr(),
                    track.getTrackPathStr(),
                    track.getPlaylistStr()
            ));
        }
    }

    private void deepCopyArrayToObservable() {
        for (TrackSerializable track : trackArrayList) {
            trackObservableList.add(new Track(
                track.getArtistNameStr(),
                track.getTrackFileNameStr(),
                track.getTrackContainerTypeStr(),
                track.getTrackTitleStr(),
                track.getAlbumDirectoryStr(),
                track.getAlbumTitleStr(),
                track.getTrackGenreStr(),
                track.getTrackDurationStr(),
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

    public void outputTrackObservableList() throws FileNotFoundException {
        System.out.println("Writing to file");

        deepCopyObservableToArray();

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
    public void clearObservableList() { trackObservableList.clear(); }
    public void clearArrayList() { trackArrayList.clear(); }

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