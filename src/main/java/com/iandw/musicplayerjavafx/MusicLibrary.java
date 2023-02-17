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

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.FieldKey;

public class MusicLibrary {
    @FXML
    private AnchorPane anchorPane;
    private final ObservableList<Track> trackObservableList;
    private final ObservableList<String> artistNameObservableList;
    private final List<String> supportedFileTypes;
    private String artistNameStr;
    private String albumDirectoryStr;
    private String trackPathStr;
    private String trackFileName;
    private String trackContainerType;
    private int index;

    public MusicLibrary() {
        trackObservableList = FXCollections.observableArrayList();
        artistNameObservableList = FXCollections.observableArrayList();
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

        System.out.println("Writing user music library to files.");
        ArtistlistFileIO.outputArtistNameObservableList(artistNameObservableList);
        TracklistFileIO.outputTrackObservableList(trackObservableList);

    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          TRACK METADATA MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // For Library Initialization
    private void parseMetadata() {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(trackPathStr));
            Tag tag = audioFile.getTag();
            String trackTitle = trackFileName;
            String trackAlbum;
            String trackGenre = tag.getFirst(FieldKey.GENRE);
            final String duration = Utils.formatSeconds(audioFile.getAudioHeader().getTrackLength());
            final String playlist = "*";

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

    // For Track/Album/Artist Importing
    private void importTrackMetadata() {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(trackPathStr));
            Tag tag = audioFile.getTag();
            String trackTitle = trackFileName;
            String trackArtist;
            String trackAlbum;
            String trackGenre = tag.getFirst(FieldKey.GENRE);
            final String duration = Utils.formatSeconds(audioFile.getAudioHeader().getTrackLength());
            final String playlist = "*";
            final String unknown = "Unknown";

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

            // Check Artist metadata for null value, if true replace with Unknown
            if (tag.getFirst(FieldKey.ARTIST) == null || Objects.equals(tag.getFirst(FieldKey.ARTIST), "")) {
                trackArtist = unknown;
            } else {
                trackArtist = tag.getFirst(FieldKey.ARTIST);
            }

            // Check Album metadata for null value, if true replace with Unknown
            if (tag.getFirst(FieldKey.ALBUM) == null || Objects.equals(tag.getFirst(FieldKey.ALBUM), "")) {
                trackAlbum = unknown;
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
                    trackArtist,
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
     *                          IMPORT MODULES
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    //
    //      IMPORT ARTIST
    //
    public void importArtist() throws IOException {
        DirectoryChooser artistChooser = new DirectoryChooser();

        artistChooser.setTitle("Select Artist Folder");
        artistChooser.setInitialDirectory((new File(".")));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("musiclibrary.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        File file = artistChooser.showDialog(stage);

        if (file != null) {
            Path artistPath = file.toPath();

            if (Files.isDirectory(artistPath)) {
                DirectoryStream<Path> artistDirectory = Files.newDirectoryStream(artistPath);
                artistNameStr = artistPath.toString().substring(artistPath.toString().lastIndexOf(File.separator) + 1);

                // Clear list to write Artist's tracks
                trackObservableList.clear();
                final String rootDirectory = SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL());
                index = 0;

                for (Path albumPath : artistDirectory) {
                    if (Files.isDirectory(albumPath)) {
                        DirectoryStream<Path> albumDirectory = Files.newDirectoryStream(albumPath);

                        for (Path trackPath : albumDirectory) {
                            if (Files.isRegularFile(trackPath)) {
                                if (Files.exists(trackPath)) {
                                    importTrackLogic(trackPath, rootDirectory);
                                }

                            } else {
                                System.out.printf("%s is not a file%n", trackPath);
                            }
                        }

                    } else {
                        System.out.printf("%s is not a directory%n", albumPath);
                    }
                }
            }
        } else {
            System.out.println("Track File empty or does not exist");
        }


    }

    //
    //      IMPORT ALBUM
    //
    public void importAlbum() throws IOException {
        DirectoryChooser albumChooser = new DirectoryChooser();

        albumChooser.setTitle("Select Album Folder");
        albumChooser.setInitialDirectory((new File(".")));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("musiclibrary.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        File file = albumChooser.showDialog(stage);

        if (file != null) {
            Path albumPath = file.toPath();

            if (Files.isDirectory(albumPath)) {
                DirectoryStream<Path> albumDirectory = Files.newDirectoryStream(albumPath);

                // Clear list to write album
                trackObservableList.clear();
                final String rootDirectory = SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL());
                index = 0;
                //TODO => get artist name from user

                for (Path trackPath : albumDirectory) {
                    if (Files.isRegularFile(trackPath)) {
                        if (Files.exists(trackPath)) {
                            importTrackLogic(trackPath, rootDirectory);

                        }

                    } else {
                        System.out.printf("%s is not a file%n", trackPath);
                    }
                }
            } else {
                System.out.printf("%s is not a directory%n", albumPath);
            }
        } else {
            System.out.println("Track File empty or does not exist");
        }

    }

    //
    //      IMPORT TRACK
    //
    public void importTrack() throws IOException {
        FileChooser trackChooser = new FileChooser();

        trackChooser.setTitle("Select Track File");
        trackChooser.setInitialDirectory((new File(".")));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("musiclibrary.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        File file = trackChooser.showOpenDialog(stage);

        if (file != null) {
            Path trackPath = file.toPath();

            if (Files.isRegularFile(trackPath)) {
                if (Files.exists(trackPath)) {
                    index = 0;
                    //TODO => Get artist name from user
                    final String rootDirectory = SettingsFileIO.getMusicDirectoryString(ResourceURLs.getSettingsURL());

                    importTrackLogic(trackPath, rootDirectory);

                }
            } else {
                System.out.printf("%s is not a file%n", trackPath);
            }

        } else {
            System.out.println("Track File empty or does not exist");
        }

    }

    private void importTrackLogic(Path trackPath, String rootDirectory) throws IOException {
        trackPathStr = trackPath.toAbsolutePath().toString();
        trackFileName = trackPath.getFileName().toString();
        trackContainerType = trackPathStr.substring(trackPathStr.lastIndexOf('.'));

        // Check for playable file container
        if (supportedFileTypes.contains(trackContainerType.toLowerCase())) {

            importTrackMetadata();

            String trackAlbum = trackObservableList.get(index).getAlbumTitleStr();
            String trackFileName = trackObservableList.get(index).getTrackFileNameStr();
            String source = trackPath.toString();
            String destination = rootDirectory + File.separator + artistNameStr + File.separator +
                    trackAlbum + File.separator + trackFileName;

            // Create new Artist/Album Directories, filepath and move track
            // If Artist Directory Exists move to Artist Directory,
            if (Files.exists(Path.of(rootDirectory + File.separator + artistNameStr))) {

                // If Album Directory Exists move to Album Directory,
                if (Files.exists(Path.of(rootDirectory + File.separator + artistNameStr + File.separator +
                        trackAlbum))) {
                    Utils.moveFile(source, destination);

                } else {
                    // Else create Album Directory hen move file
                    Utils.createDirectory(rootDirectory + File.separator + artistNameStr, trackAlbum);
                    Utils.moveFile(source, destination);
                }

            } else {
                // Else Create new Artist and Album Directory
                Utils.createDirectory(rootDirectory, artistNameStr);
                Utils.createDirectory(rootDirectory + File.separator + artistNameStr, trackAlbum);
                Utils.moveFile(source, destination);

            }

            trackObservableList.get(index).setTrackPathStr(destination);
            index++;

        } else {
            System.out.printf("%s is not a compatible file type.", trackFileName);
        }
    }

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

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          GETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public ObservableList<Track> getTrackObservableList() { return trackObservableList; }
    public ObservableList<String> getArtistNameObservableList() { return artistNameObservableList; }
    public Track getImportedTrack() {
        // Get Track, clear list
        Track track = trackObservableList.get(0);
        trackObservableList.clear();

        return track;
    }

    public String getArtistNameStr() { return artistNameStr; }

}