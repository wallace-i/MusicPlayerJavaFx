package com.iandw.musicplayerjavafx;

import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;

public class ArtistListContextMenu {
    public static void getContextMenu(ListView<String> artistListView, ListView<String> playlistListView,
                                      TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                      TableViewLibrary tableViewLibrary, TrackIndex trackIndex, UserSettings userSettings) {

        String selectedItem = artistListView.getSelectionModel().getSelectedItem();
        System.out.println("Artists selected: " + selectedItem);

        ContextMenu contextMenu = new ContextMenu();

        // Add to list
        MenuItem addArtist = new MenuItem("Add Artist");

        // Edit List
        MenuItem editArtist = new MenuItem("Edit");
        SeparatorMenuItem divider1 = new SeparatorMenuItem();

        // Remove from list
        MenuItem removeArtist = new MenuItem("Remove");
        SeparatorMenuItem divider2 = new SeparatorMenuItem();

        // View folder in explorer
        MenuItem openInExplorer = new MenuItem("Open in Explorer");

        // Add Artist
        addArtist.setOnAction(event -> {
            addArtist(artistListView, playlistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex);
        });

        // Edit Artist or Playlist
        editArtist.setOnAction(event -> {
            editArtist(artistListView, playlistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex);
        });

        // Remove Artist
        removeArtist.setOnAction(event -> {
            removeArtist(artistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex);
        });

        // Open in File Explorer
        openInExplorer.setOnAction(event -> {
            String menuSelection = artistListView.getSelectionModel().getSelectedItem();
            File file = new File(userSettings.getRootMusicDirectoryString() + File.separator + menuSelection);

            if (menuSelection != null) {
                Utils.openExplorer(file);
            }
        });

        contextMenu.getItems().addAll(addArtist, editArtist, divider1, removeArtist, divider2, openInExplorer);

        artistListView.setContextMenu(contextMenu);
    }

    public static void addArtist(ListView<String> artistListView, ListView<String> playlistListView,
                                 TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                 TableViewLibrary tableViewLibrary, TrackIndex trackIndex)
    {
        try {
            String windowTitle = "Add Artist";
            String menuSelection = artistListView.getSelectionModel().getSelectedItem();
            ListViewController listViewController = new ListViewController();
            listViewController.showListViewInputWindow(artistListView, playlistListView, trackTableView,
                    listViewLibrary, tableViewLibrary, trackIndex, windowTitle, menuSelection);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void editArtist(ListView<String> artistListView, ListView<String> playlistListView,
                                   TableView<TrackMetadata> trackTableView, ListViewLibrary listViewLibrary,
                                   TableViewLibrary tableViewLibrary, TrackIndex trackIndex)
    {
        try {
            String windowTitle = "Edit Artist";
            String menuSelection = artistListView.getSelectionModel().getSelectedItem();

            if (menuSelection != null) {
                ListViewController listViewController = new ListViewController();
                listViewController.showListViewInputWindow(artistListView, playlistListView, trackTableView,
                        listViewLibrary, tableViewLibrary, trackIndex, windowTitle, menuSelection);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void removeArtist(ListView<String> artistListView, TableView<TrackMetadata> trackTableView,
                                     ListViewLibrary listViewLibrary, TableViewLibrary tableViewLibrary, TrackIndex trackIndex)
    {
        String menuSelection = artistListView.getSelectionModel().getSelectedItem();

        if (menuSelection != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove Artist");
            alert.setHeaderText("Removing Artist deletes their tracks from Library, but does not affect files or folders.");
            alert.setContentText("Would you like to continue?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                Utils.removeArtist(menuSelection, listViewLibrary, tableViewLibrary, trackIndex, trackTableView, artistListView);
            }
        }


    }
}
