package com.iandw.musicplayerjavafx;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;


public class SearchTreeMap {
    // searchMap => Treemap (K: String, V: String)
    // Key: Artist Name || Album Title || Track Title
    // Value: Path  =>  artist name path => "artistName"
    //                  album title path => "artistName"
    //                  track title path => "artistName" + File.separator + trackFilename
    private final TreeMap<String, String> searchMap;

    public SearchTreeMap(String musicRootDirectory) throws IOException {
        searchMap = new TreeMap<>();
        fileInput();

        if (searchMap.isEmpty()) {
            System.out.println("Initializing search tree");
            initializeSearchTree(musicRootDirectory);
        } else {
            System.out.println("SearchTreeMap input from file");
        }

        fileOutput();

    }

    public String searchForKey(String key) {
        String searchResult = null;

        if (searchMap.containsKey(key)) {
            searchResult = searchMap.get(key);
        }

        return searchResult;
    }

    public TreeMap<String, String> getSearchMap() { return searchMap; }

    public void setSearchMap(String key, String value) { searchMap.put(key, value); }

    private void fileInput() {



    }

    private void fileOutput() {
        for (String key : searchMap.keySet()) {
            //System.out.printf("K:%s V:%s%n", key, searchMap.get(key));
        }

    }

    private void initializeSearchTree(String musicRootDirectory) throws IOException {
        Path rootPath = Paths.get(musicRootDirectory);

        if (Files.exists(rootPath)) {
            if (Files.isDirectory(rootPath)) {

                DirectoryStream<Path> musicLibraryDirectory = Files.newDirectoryStream(rootPath);

                for (Path artistDir : musicLibraryDirectory) {
                    if (Files.isDirectory(artistDir)) {
                        String artistNameStr = artistDir.toString();
                        artistNameStr = artistNameStr.substring(artistNameStr.lastIndexOf(File.separator) + 1);

                        // Load artistName into search tree
                        searchMap.put(artistNameStr.toLowerCase(), artistNameStr + File.separator + "null");

                        DirectoryStream<Path> artistDirectory = Files.newDirectoryStream(artistDir);
                        int trackIndex = 0;

                        for (Path albumDir : artistDirectory) {
                            if (Files.isDirectory(albumDir)) {

                                String albumTitleStr = albumDir.toString();
                                albumTitleStr = albumTitleStr.substring(albumTitleStr.lastIndexOf(File.separator) + 1);;

                                // Load Album Title into search tree
                                searchMap.put(albumTitleStr.toLowerCase(), artistNameStr + File.separator + trackIndex);

                                DirectoryStream<Path> albumDirectory = Files.newDirectoryStream(albumDir);
                                for (Path track : albumDirectory) {
                                    String trackTitleStr = track.toString();
                                    trackTitleStr = trackTitleStr.substring(trackTitleStr.lastIndexOf(File.separator) + 1, trackTitleStr.lastIndexOf('.'));

                                    // Load TrackTitle into search tree
                                    searchMap.put(trackTitleStr.toLowerCase(), artistNameStr + File.separator + trackIndex);
                                    trackIndex++;
                                }
                            }
                        }
                    }
                }
            }

        } else {
            System.out.printf("%s does not exist%n", rootPath);
        }

    }

}
