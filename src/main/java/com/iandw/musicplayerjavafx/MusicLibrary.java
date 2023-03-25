/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class:
 *      Notes:
 */

package com.iandw.musicplayerjavafx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
    private final ObservableList<TrackMetadata> trackMetadataObservableList;
    private final ObservableList<String> artistNameObservableList;
    private final List<String> supportedFileTypes;
    private String artistNameStr;
    private String albumDirectoryStr;
    private String trackPathStr;
    private String trackFileName;
    private String trackContainerType;
    private int index;
    private ImportCatagory importCatagory;
    private String rootMusicDirectoryString;
    private Boolean continueInitializing;

    public MusicLibrary(UserSettings userSettings) {
        rootMusicDirectoryString = userSettings.getRootMusicDirectoryString();
        trackMetadataObservableList = FXCollections.observableArrayList();
        artistNameObservableList = FXCollections.observableArrayList();
        supportedFileTypes = Arrays.asList(".aif", ".aiff", ".mp3", "mp4", ".m4a", ".wav");
        continueInitializing = true;
    }

    public void clearMusicLibrary() {
        trackMetadataObservableList.clear();
        artistNameObservableList.clear();
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          METADATA FILE INITIALIZATION => tracklist.ser
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void initializeMusicLibrary(ProgressBarData progressBarData) throws IOException {
        System.out.println("Initializing observable list");

        Utils.clearSerializedFiles();

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

                            if (Files.isDirectory(albumFolder )) {
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
                                                progressBarData.increaseProgress(trackPathStr);

                                            } else {
                                                System.out.printf("%s is not a compatible file type.", trackFileName);

                                            }
                                        }
                                    } else {
                                        System.out.printf("%s is not a file%n", trackPath);
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
                                } else {
                                    System.out.printf("%s does not exist%n", albumDirectoryPath);
                                }
                            }
                        }
                    } else {
                        System.out.printf("%s is not an artist directory%n", artistDirectoryPath);
                    }
                }

            } else {
                System.out.printf("%s is not a directory%n", rootPath);
            }

        } else {
            System.out.printf("%s does not exist%n", rootPath);
        }


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
            TrackMetadata trackMetadata = new TrackMetadata(
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

            trackMetadataObservableList.add(trackMetadata);

            System.out.printf("Importing: %s%n", trackFileName);
            System.out.printf(tag.toString());

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
            String trackGenre = tag.getFirst(FieldKey.GENRE);
            final String duration = Utils.formatSeconds(audioFile.getAudioHeader().getTrackLength());
            final String playlist = "*";
            final String unknown = "Unknown";
            String trackArtist = unknown;
            String trackAlbum = unknown;

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

            switch (importCatagory) {
                case ARTIST -> {
                    trackArtist = artistNameStr;
                    trackAlbum = albumDirectoryStr;
                }

                case ALBUM -> {

                    if (tag.getFirst(FieldKey.ALBUM_ARTIST) == null || Objects.equals(tag.getFirst(FieldKey.ALBUM_ARTIST), "")){
                        if (tag.getFirst(FieldKey.ARTIST) == null || Objects.equals(tag.getFirst(FieldKey.ARTIST), "")) {
                            trackArtist = unknown;
                        } else {
                            trackArtist = tag.getFirst(FieldKey.ARTIST);
                        }
                    } else {
                        trackArtist = tag.getFirst(FieldKey.ALBUM_ARTIST);
                    }

                    trackAlbum = albumDirectoryStr;

                }

                case TRACK -> {

                    if (tag.getFirst(FieldKey.ARTIST) == null || Objects.equals(tag.getFirst(FieldKey.ARTIST), "")){
                        if (tag.getFirst(FieldKey.ALBUM_ARTIST) == null || Objects.equals(tag.getFirst(FieldKey.ALBUM_ARTIST), "")) {
                            trackArtist = unknown;
                        } else {
                            trackArtist = tag.getFirst(FieldKey.ALBUM_ARTIST);
                        }
                    } else {
                        trackArtist = tag.getFirst(FieldKey.ARTIST);
                    }

                    // Check Album metadata for null value, if true replace with Unknown
                    if (tag.getFirst(FieldKey.ALBUM) != null || !Objects.equals(tag.getFirst(FieldKey.ALBUM), "")) {
                        trackAlbum = tag.getFirst(FieldKey.ALBUM);
                    }
                }
            }

            // Check genre metadata for null value, if true leave blank
            if (trackGenre == null) {
                trackGenre = "";
            } else if (trackGenre.startsWith("(")) {
                String trackGenreID = trackGenre.substring(trackGenre.indexOf('(') + 1, trackGenre.indexOf(')'));
                trackGenre = ID3v1Genres.getGenre(Integer.parseInt(trackGenreID));
            }

            // Populate Track object
            TrackMetadata trackMetadata = new TrackMetadata(
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

            trackMetadataObservableList.add(trackMetadata);

            System.out.printf("Importing: %s%n", trackFileName);
            System.out.printf(tag.toString());

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
        importCatagory = ImportCatagory.ARTIST;

        // Clear list to write Artist's tracks
        trackMetadataObservableList.clear();

        // Select Artist
        DirectoryChooser artistChooser = new DirectoryChooser();
        artistChooser.setTitle("Select Artist Folder");
        artistChooser.setInitialDirectory((new File(".")));

        // Set Stage, show artistChooser Dialog
        Stage stage = new Stage();
        File file = artistChooser.showDialog(stage);

        // Import Artist metadata into Music Library
        if (file != null) {
            Path artistPath = file.toPath();
            artistNameStr = artistPath.toString().substring(artistPath.toString().lastIndexOf(File.separator) + 1);

            if (Files.isDirectory(artistPath)) {
                DirectoryStream<Path> artistDirectory = Files.newDirectoryStream(artistPath);

                index = 0;

                for (Path albumPath : artistDirectory) {
                    if (Files.isDirectory(albumPath)) {
                        DirectoryStream<Path> albumDirectory = Files.newDirectoryStream(albumPath);
                        albumDirectoryStr = albumPath.toString().substring(albumPath.toString().lastIndexOf(File.separator) + 1);

                        for (Path trackPath : albumDirectory) {
                            if (Files.isRegularFile(trackPath)) {
                                if (Files.exists(trackPath)) {
                                    importTrackLogic(trackPath, rootMusicDirectoryString);
                                }

                            } else {
                                System.out.printf("%s is not a file%n", trackPath);
                            }
                        }

                    } else {
                        if (Files.exists(albumPath)) {
                            trackPathStr = albumPath.toAbsolutePath().toString();
                            trackFileName = albumPath.getFileName().toString();
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

                artistNameStr = trackMetadataObservableList.get(0).getArtistNameStr();
            }

        } else {
            System.out.println("Track File empty or does not exist");
        }
    }

    //
    //      IMPORT ALBUM
    //
    public void importAlbum() throws IOException {
        importCatagory = ImportCatagory.ALBUM;

        // Clear list to write album
        trackMetadataObservableList.clear();

        // Select album folder
        DirectoryChooser albumChooser = new DirectoryChooser();
        albumChooser.setTitle("Select Album Folder");
        albumChooser.setInitialDirectory((new File(".")));

        // Set Stage, show albumChooser Dialog
        Stage stage = new Stage();
        File file = albumChooser.showDialog(stage);

        // Import Album metadata into Music Library
        if (file != null) {
            Path albumPath = file.toPath();
            albumDirectoryStr = albumPath.toString().substring(albumPath.toString().lastIndexOf(File.separator) + 1);

            if (Files.isDirectory(albumPath)) {
                DirectoryStream<Path> albumDirectory = Files.newDirectoryStream(albumPath);

                index = 0;

                for (Path trackPath : albumDirectory) {
                    if (Files.isRegularFile(trackPath)) {
                        if (Files.exists(trackPath)) {
                            importTrackLogic(trackPath, rootMusicDirectoryString);
                        }

                    } else {
                        System.out.printf("%s is not a file%n", trackPath);
                    }
                }
                artistNameStr = trackMetadataObservableList.get(0).getArtistNameStr();

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
        importCatagory = ImportCatagory.TRACK;

        // Clear list to write track
        trackMetadataObservableList.clear();

        // Select track file
        FileChooser trackChooser = new FileChooser();
        trackChooser.setTitle("Select Track File");
        trackChooser.setInitialDirectory((new File(".")));

        // Set Stage, show trackChooser Dialog
        Stage stage = new Stage();
        File file = trackChooser.showOpenDialog(stage);

        // Import Track metadata into Music Library
        if (file != null) {
            Path trackPath = file.toPath();

            if (Files.isRegularFile(trackPath)) {
                if (Files.exists(trackPath)) {

                    index = 0;

                    importTrackLogic(trackPath, rootMusicDirectoryString);

                    artistNameStr = trackMetadataObservableList.get(0).getArtistNameStr();
                    System.out.println("ArtistName: " + artistNameStr);
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

            try {
                // Check for slashes which could interfere with file creation
                final String trackArtist = removeSlashs(trackMetadataObservableList.get(index).getArtistNameStr());
                final String trackAlbum = removeSlashs(trackMetadataObservableList.get(index).getAlbumTitleStr());
                final String trackFileName = removeSlashs(trackMetadataObservableList.get(index).getTrackFileNameStr());
                final String source = trackPath.toString();
                final String destination = rootDirectory + File.separator + trackArtist + File.separator +
                        trackAlbum + File.separator + trackFileName;

                // Create new Artist/Album Directories, filepath and move track
                // If Artist Directory Exists move to Artist Directory,
                if (Files.exists(Path.of(rootDirectory + File.separator + trackArtist))) {

                    // If Album Directory Exists move to Album Directory,
                    if (Files.exists(Path.of(rootDirectory + File.separator + trackArtist + File.separator + trackAlbum))) {
                        Utils.copyFile(source, destination);

                    } else {
                        // Else create Album Directory hen move file
                        Utils.createDirectory(rootDirectory + File.separator + trackArtist, trackAlbum);
                        Utils.copyFile(source, destination);
                    }

                } else {
                    System.out.println(trackArtist);
                    // Else Create new Artist and Album Directory
                    Utils.createDirectory(rootDirectory, trackArtist);
                    Utils.createDirectory(rootDirectory + File.separator + trackArtist, trackAlbum);
                    Utils.copyFile(source, destination);

                }

                trackMetadataObservableList.get(index).setTrackPathStr(destination);
                index++;

            }catch(IndexOutOfBoundsException e) {
                System.out.println("Import failed");
                e.printStackTrace();
            }
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

    private String removeSlashs(String string) {
        String updatedString = string;

        if (string.contains("/") ) {
            updatedString = string.replace('/', '_');
        }

        if (string.contentEquals("\\")) {
            updatedString = string.replace('\\', '_');
        }

        return updatedString;
    }


    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          RECURSIVE INITIALIZATION
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void recursiveInitialization() throws IOException {
        System.out.println("Initializing observable list");

        Utils.clearSerializedFiles();

        Path rootPath = Paths.get(rootMusicDirectoryString);

        if (Files.exists(rootPath)) {
            if (Files.isDirectory(rootPath)) {

                File rootDirectory = new File(rootMusicDirectoryString);

                trackMetadataObservableList.addAll(listFileTree(rootDirectory));



            } else {
                System.out.printf("%s is not a directory%n", rootPath);
            }

        } else {
            System.out.printf("%s does not exist%n", rootPath);
        }

    }



    private Collection<TrackMetadata> listFileTree(File dir) {
        Set<TrackMetadata> fileTree = new HashSet<>();

        if (dir == null || dir.listFiles() == null) {
            return fileTree;
        }

        for (File entry : Objects.requireNonNull(dir.listFiles())) {
            if (entry.isFile()) {
                trackPathStr = entry.getAbsolutePath();
                trackFileName = entry.getName();
                trackContainerType = trackPathStr.substring(trackPathStr.lastIndexOf('.'));

                if (supportedFileTypes.contains(trackContainerType.toLowerCase())) {

                    recursiveParse();

                } else {
                    System.out.printf("%s is not a compatible file type.", trackFileName);
                }

            } else {
                fileTree.addAll(listFileTree(entry));
            }
        }

        return fileTree;

    }



    private void recursiveParse() {
        try {
            AudioFile audioFile = AudioFileIO.read(new File(trackPathStr));
            Tag tag = audioFile.getTag();
            String trackArtist;
            String trackAlbum;
            String trackTitle = trackFileName;
            String trackGenre = tag.getFirst(FieldKey.GENRE);
            final String duration = Utils.formatSeconds(audioFile.getAudioHeader().getTrackLength());
            final String playlist = "*";

            // Get Track Artist Name
            if (tag.getFirst(FieldKey.ALBUM_ARTIST) == null || Objects.equals(tag.getFirst(FieldKey.ALBUM_ARTIST), "")){
                if (tag.getFirst(FieldKey.ARTIST) == null || Objects.equals(tag.getFirst(FieldKey.ARTIST), "")) {
                    trackArtist = "Unknown";
                } else {
                    trackArtist = tag.getFirst(FieldKey.ARTIST);
                }
            } else {
                trackArtist = tag.getFirst(FieldKey.ALBUM_ARTIST);
            }

            // Check album metadata for null value, if true replace with directory name
            if (tag.getFirst(FieldKey.ALBUM) == null || Objects.equals(tag.getFirst(FieldKey.ALBUM), "")) {
                trackAlbum = "Unknown";
            } else {
                trackAlbum = tag.getFirst(FieldKey.ALBUM);
            }

            // Get Track Title
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

            // Check genre metadata for null value, if true leave blank
            if (trackGenre == null) {
                trackGenre = "";
            } else if (trackGenre.startsWith("(")) {
                String trackGenreID = trackGenre.substring(trackGenre.indexOf('(') + 1, trackGenre.indexOf(')'));
                trackGenre = ID3v1Genres.getGenre(Integer.parseInt(trackGenreID));
            }

            // Populate Track object
            TrackMetadata trackMetadata = new TrackMetadata(
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

            trackMetadataObservableList.add(trackMetadata);

            if (!artistNameObservableList.contains(trackArtist)) {
                artistNameObservableList.add(trackArtist);
            }

            System.out.printf("Importing: %s%n", trackFileName);
            System.out.printf(tag.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *
     *                          SETTERS / GETTERS
     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void setRootMusicDirectoryString(String rootMusicDirectoryString) {
        this.rootMusicDirectoryString = rootMusicDirectoryString;
    }

    public ObservableList<TrackMetadata> getTrackObservableList() {
        return trackMetadataObservableList;
    }
    public ObservableList<String> getArtistNameObservableList() {
        return artistNameObservableList;
    }

    public TrackMetadata getImportedTrack() {
        // Get Track, clear list
        TrackMetadata trackMetadata = trackMetadataObservableList.get(0);
        trackMetadataObservableList.clear();

        return trackMetadata;
    }

    public String getArtistNameStr() { return artistNameStr; }

}