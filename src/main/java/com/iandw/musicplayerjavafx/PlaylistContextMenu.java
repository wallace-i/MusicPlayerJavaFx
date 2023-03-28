package com.iandw.musicplayerjavafx;

import javafx.scene.control.*;

import java.io.IOException;
import java.util.Objects;

public class PlaylistContextMenu {

    private static final String emptyPlaylist = "* playlists *";

    public static void getContextMenu(ListView<String> artistListView, ListView<String> playlistListView,
                                      TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                      TableViewLibrary tableViewLibrary, TrackIndex trackIndex)
    {
        String selectedItem = playlistListView.getSelectionModel().getSelectedItem();
        System.out.println("Playlist selected: " + selectedItem);

        ContextMenu contextMenu = new ContextMenu();

        // Add to list
        MenuItem createPlaylist = new MenuItem("Create Playlist");

        // Edit List
        MenuItem editPlaylist = new MenuItem("Edit");
        SeparatorMenuItem divider1 = new SeparatorMenuItem();

        // Remove from list
        MenuItem removePlaylist = new MenuItem("Remove");
        SeparatorMenuItem divider2 = new SeparatorMenuItem();

        // View folder in explorer
        MenuItem openInExplorer = new MenuItem("Open in Explorer");

        // Create Playlist
        createPlaylist.setOnAction(event -> {
            createPlaylist(artistListView, playlistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex);
        });

        editPlaylist.setOnAction(event -> {
            editPlaylist(artistListView, playlistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex);
        });

        removePlaylist.setOnAction(event -> {
            removePlaylist(artistListView, playlistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex);
        });

        contextMenu.getItems().addAll(createPlaylist, editPlaylist, divider1, removePlaylist, divider2, openInExplorer);

        playlistListView.setContextMenu(contextMenu);

    }


    public static void createPlaylist(ListView<String> artistListView, ListView<String> playlistListView,
                               TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                               TableViewLibrary tableViewLibrary, TrackIndex trackIndex)
    {
        try {
            String windowTitle = "Create Playlist";
            String menuSelection = playlistListView.getSelectionModel().getSelectedItem();
            ListViewController listViewController = new ListViewController();
            listViewController.showListViewInputWindow(artistListView, playlistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex, windowTitle, menuSelection);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private static void editPlaylist(ListView<String> artistListView, ListView<String> playlistListView,
                                     TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                     TableViewLibrary tableViewLibrary, TrackIndex trackIndex)
    {
        try {
            String windowTitle = "Edit Playlist";
            String menuSelection = playlistListView.getSelectionModel().getSelectedItem();

            if (menuSelection != null && !Objects.equals(menuSelection, emptyPlaylist)) {
                ListViewController listViewController = new ListViewController();
                listViewController.showListViewInputWindow(artistListView, playlistListView, trackTableView,
                        listViewLibrary, tableViewLibrary, trackIndex, windowTitle, menuSelection);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private static void removePlaylist(ListView<String> artistListView, ListView<String> playlistListView,
                                       TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                       TableViewLibrary tableViewLibrary, TrackIndex trackIndex)
    {
        String menuSelection = playlistListView.getSelectionModel().getSelectedItem();

        if (menuSelection != null && !Objects.equals(menuSelection, emptyPlaylist)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Playlist");
            alert.setHeaderText("Removing playlist does not affect files or folders.");
            alert.setContentText("Would you like to continue?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                Utils.removePlaylist(menuSelection, listViewLibrary, tableViewLibrary, trackIndex,
                        trackTableView, artistListView, playlistListView);
            }
        }

    }
}
