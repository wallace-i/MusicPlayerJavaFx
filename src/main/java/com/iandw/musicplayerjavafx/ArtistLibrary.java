/**
 *      Author: Ian Wallace copyright 2022 all rights reserved.
 *      Application: MusicPlayer
 *      Class: ArtistLibrary
 *      Notes: Populates TableView with music file data
 */

package com.iandw.musicplayerjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class ArtistLibrary {

    private final ObservableList<Track> artistLibraryList;
//    private final ArrayList<Track> trackArrayList;
    private int tableSize;


    /**
     * function: loadArtistTableView => ObservableList<Track>
     * @param artistDirectoryPathStr => String from MusicPlayerController
     * @return trackData => Track Object container with track metadata for TableView in MusicPlayerController
     */
    public ArtistLibrary(String artistDirectoryPathStr) throws IOException {
        artistLibraryList = FXCollections.observableArrayList();
//        trackArrayList = new ArrayList<>();

        System.out.println(artistDirectoryPathStr);
        Path currentPath = Paths.get(artistDirectoryPathStr);

        if (Files.exists(currentPath)) {
            if (Files.isDirectory(currentPath)) {
                DirectoryStream<Path> artistDir = Files.newDirectoryStream(currentPath);

                tableSize = 0;

                try {
                    for (Path albumFolder : artistDir) {
                        String albumDirectoryPathStr = albumFolder.toAbsolutePath().toString();
                        String albumDirectory = albumDirectoryPathStr.substring(albumDirectoryPathStr.lastIndexOf(File.separator) + 1);
                        DirectoryStream<Path> albumDirPath = Files.newDirectoryStream(Paths.get(albumDirectoryPathStr));

                        for (Path trackPath : albumDirPath) {
                            if (Files.exists(trackPath)) {
                                String audioTrackPathStr = trackPath.toAbsolutePath().toString();
                                String trackFileName = trackPath.getFileName().toString();
                                String trackContainerType = audioTrackPathStr.substring(audioTrackPathStr.lastIndexOf('.'));
                                //                          Debugger:
                                //                          System.out.printf("audioTrackPathStr: %s%n", audioTrackPathStr);
                                //                          System.out.printf("trackFileName: %s%n", trackFileName);
                                //                          System.out.printf("trackContainerType: %s%n", trackContainerType);

                                Media audioTrack = new Media(new File(audioTrackPathStr).toURI().toString());
                                MediaPlayer mediaPlayer = new MediaPlayer(audioTrack);

                                mediaPlayer.setOnReady(() -> {
                                    String artistName = artistDirectoryPathStr.substring(artistDirectoryPathStr.lastIndexOf(File.separator) + 1);
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
                                        trackAlbum = albumDirectory;
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
                                            Objects.equals(trackContainerType.toLowerCase(), ".aiff")||
                                            Objects.equals(trackContainerType.toLowerCase(), ".mp3") ||
                                            Objects.equals(trackContainerType.toLowerCase(), ".mp4") ||
                                            Objects.equals(trackContainerType.toLowerCase(), ".m4a") ||
                                            Objects.equals(trackContainerType.toLowerCase(), ".wav")) {

                                        // Populate Track object
                                        Track currentTrack = new Track(
                                                artistName,
                                                trackFileName,
                                                trackContainerType,
                                                trackTitle,
                                                albumDirectory,
                                                trackAlbum,
                                                trackGenre,
                                                mediaPlayer.getTotalDuration(),
                                                trackPath.toString()
                                        );

                                        // Add track data to ObservableList
                                        artistLibraryList.add(currentTrack);

                                    } else {
                                        System.out.printf("%s is not a compatible file type.", trackFileName);
                                    }
                                });

                                // Increment size to calculate table size for shuffle play
                                tableSize++;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Directory cannot be loaded.");
                    e.printStackTrace();
                }

            }

        } else {
            System.out.printf("%s does not exist%n", currentPath);
        }
//        Debugger
//        for (Track i : trackData) {
//            System.out.printf("%s %s %s %s %n",i.getTrackTitleStr(), i.getAlbumTitleStr(), i.getTrackLengthStr(), i.getTrackGenreStr());
//        }


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

    public ObservableList<Track> getArtistLibraryList() { return artistLibraryList; }

//    public ArrayList<Track> getTrackArrayList() { return trackArrayList; }

    public int getTableSize() { return tableSize; }

}