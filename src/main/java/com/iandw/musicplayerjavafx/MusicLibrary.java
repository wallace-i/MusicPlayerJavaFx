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
import java.nio.file.*;
import java.util.*;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.FieldKey;

public class MusicLibrary {
    private final ObservableList<Track> trackObservableList;
    private final ObservableList<String> artistNameObservableList;
    private ArrayList<TrackSerializable> trackArrayList;
    private ArrayList<String> artistNameArrayList;
    private final List<String> supportedFileTypes;
    private String artistNameStr;
    private String albumDirectoryStr;
    private String trackPathStr;
    private String trackFileName;
    private String trackContainerType;

    public MusicLibrary() {
        trackObservableList = FXCollections.observableArrayList();
        artistNameObservableList = FXCollections.observableArrayList();
        trackArrayList = new ArrayList<>();
        artistNameArrayList = new ArrayList<>();
        supportedFileTypes = Arrays.asList(".aif", ".aiff", ".mp3", "mp4", ".m4a", ".wav");
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          METADATA FILE INITIALIZATION => tracklist.ser
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void initializeMusicLibrary() throws IOException {
        System.out.println("Initializing observable list");

        Utils.clearSerializedFiles();

        String rootMusicDirectoryString = SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL());

        Path rootPath = Paths.get(rootMusicDirectoryString);

        //TODO => directory mismatch InvocationTargetExcteption, ie need to ignore extrafolders/
        // incorrect directory structure
        if (Files.exists(rootPath)) {
            if (Files.isDirectory(rootPath)) {
                DirectoryStream<Path> musicDir = Files.newDirectoryStream(rootPath);

                // MUSIC DIRECTORY => LOOP THROUGH ARTIST FOLDERS
                for (Path artistFolder : musicDir) {
                    Path artistDirectoryPath = artistFolder.toAbsolutePath();
                    artistNameStr = artistDirectoryPath.toString().substring(artistDirectoryPath.toString().lastIndexOf(File.separator) + 1);

                    artistNameStr = artistNameStr.substring(artistNameStr.lastIndexOf(File.separator) + 1);
                    artistNameObservableList.add(artistNameStr);

                    if (Files.isDirectory(artistFolder)) {
                        DirectoryStream<Path> artistDir = Files.newDirectoryStream(artistDirectoryPath);

                        // ARTIST DIRECTORY => LOOP THROUGH ALBUM FOLDERS
                        for (Path albumFolder : artistDir) {
                            Path albumDirectoryPath = albumFolder.toAbsolutePath();
                            albumDirectoryStr = albumDirectoryPath.toString().substring(albumDirectoryPath.toString().lastIndexOf(File.separator) + 1);

                            if (Files.isDirectory(albumFolder)) {
                                DirectoryStream<Path> albumDirPath = Files.newDirectoryStream(albumDirectoryPath);

                                // ALBUM DIRECTORY => LOOP THROUGH TRACK FILES
                                for (Path trackPath : albumDirPath) {
                                    if (Files.isRegularFile(trackPath)) {
                                        if (Files.exists(trackPath)) {
                                            trackPathStr = trackPath.toAbsolutePath().toString();
                                            trackFileName = trackPath.getFileName().toString();
                                            trackContainerType = trackPathStr.substring(trackPathStr.lastIndexOf('.'));

                                            // Check for playable file container
                                            if (supportedFileTypes.contains(trackContainerType.toLowerCase())) {

                                                parseMetadata();

                                            } else {
                                                System.out.printf("%s is not a compatible file type.", trackFileName);
                                            }
                                        }
                                    } else {
                                        System.out.printf("%s is not a file%n", trackPath);
                                        break;
                                    }
                                }
                            } else {
                                // ARTIST DIRECTORY => LOOP THROUGH TRACK FILES
                                // Used when no album folder exists
                                // albumDirectoryPath is equal to trackPath when there is no album directory for audio Files
                                if (Files.exists(albumDirectoryPath)) {
                                    trackPathStr = albumDirectoryPath.toAbsolutePath().toString();
                                    trackFileName = albumDirectoryPath.getFileName().toString();
                                    trackContainerType = trackPathStr.substring(trackPathStr.lastIndexOf('.'));

                                    // Check for playable file container
                                    if (supportedFileTypes.contains(trackContainerType.toLowerCase())) {

                                        parseMetadata();

                                    } else {
                                        System.out.printf("%s is not a compatible file type.", trackFileName);
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.printf("%s is not an artist directory%n", artistDirectoryPath);
                        break;
                    }
                }

            } else {
                System.out.printf("%s is not a directory%n", rootPath);
            }

        } else {
            System.out.printf("%s does not exist%n", rootPath);
        }

//        Debugger
//        for (Track i : trackData) {
//            System.out.printf("%s %s %s %s %n",i.getTrackTitleStr(), i.getAlbumTitleStr(), i.getTrackLengthStr(), i.getTrackGenreStr());
//        }

        // Output track and artist name data to files
        ListViewLibrary listViewLibrary = new ListViewLibrary(artistNameObservableList);
        //TableViewLibrary tableViewLibrary = new TableViewLibrary(trackObservableList);

        System.out.println("Writing user music library to files.");
        listViewLibrary.outputArtistNameObservableList();
        TracklistFileIO.outputTrackObservableList(trackObservableList);

    }

    private void parseMetadata() {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(trackPathStr));
            Tag tag = audioFile.getTag();
            //
            String trackTitle = trackFileName;
            String trackAlbum;
            String trackGenre = tag.getFirst(FieldKey.GENRE);
            String duration = Utils.formatSeconds(audioFile.getAudioHeader().getTrackLength());
            String playlist = "*";

            // Check title metadata for null value, if true replace with file name substring
            if (tag.getFirst(FieldKey.TITLE) == null || Objects.equals(tag.getFirst(FieldKey.TITLE), "")) {
                trackTitle = trackFileName.substring(0, trackTitle.indexOf('.'));

                if (Character.isDigit(trackTitle.charAt(0))) {
                    trackTitle = filterDigitsFromTitle(trackTitle);
                }

            } else {
                trackTitle = tag.getFirst(FieldKey.TITLE);
            }

            // If still null replace with trackFileName (Redundancy)
            if (trackTitle == null) {
                trackTitle = trackFileName;
            }

            // Check album metadata for null value, if true replace with directory name
            if (tag.getFirst(FieldKey.ALBUM) == null || Objects.equals(tag.getFirst(FieldKey.ALBUM), "")) {
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

            // Populate Track object
            Track track = new Track(
                    artistNameStr,
                    trackFileName,
                    trackContainerType,
                    trackTitle,
                    trackAlbum,
                    trackGenre,
                    duration,
                    trackPathStr,
                    playlist
            );

            trackObservableList.add(track);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<Track> getTrackObservableList() { return trackObservableList; }
    public ObservableList<String> getArtistNameObservableList() { return artistNameObservableList; }



    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          STRING PROCESSING
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

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