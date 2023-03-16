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

    public MusicLibrary(UserSettings userSettings) {
        rootMusicDirectoryString = userSettings.getRootMusicDirectoryString();
        trackMetadataObservableList = FXCollections.observableArrayList();
        artistNameObservableList = FXCollections.observableArrayList();
        supportedFileTypes = Arrays.asList(".aif", ".aiff", ".mp3", "mp4", ".m4a", ".wav");
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

    public void initializeMusicLibrary() throws IOException {
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

//        Debugger
//        for (Track i : trackData) {
//            System.out.printf("%s %s %s %s %n",i.getTrackTitleStr(), i.getAlbumTitleStr(), i.getTrackLengthStr(), i.getTrackGenreStr());
//        }

    }

    private Collection<TrackMetadata> listFileTree(File dir) {
        Set<TrackMetadata> fileTree = new HashSet<>();

        if (dir==null || dir.listFiles() == null) {
            return fileTree;
        }

        for (File entry : Objects.requireNonNull(dir.listFiles())) {
            if (entry.isFile()) {
                trackPathStr = entry.getAbsolutePath();
                trackFileName = entry.getName();
                parseMetadata();

            } else {
                fileTree.addAll(listFileTree(entry));
            }
        }

        return fileTree;

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
            String trackArtist;
            String trackAlbum;
            String trackTitle = trackFileName;
            String trackGenre = tag.getFirst(FieldKey.GENRE);
            String trackContainerType = trackPathStr.substring(trackPathStr.lastIndexOf('.'));
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



    private void importTrackLogic(Path trackPath, String rootDirectory) throws IOException {
        trackPathStr = trackPath.toAbsolutePath().toString();
        trackFileName = trackPath.getFileName().toString();
        trackContainerType = trackPathStr.substring(trackPathStr.lastIndexOf('.'));

        // Check for playable file container
        if (supportedFileTypes.contains(trackContainerType.toLowerCase())) {

            parseMetadata();

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