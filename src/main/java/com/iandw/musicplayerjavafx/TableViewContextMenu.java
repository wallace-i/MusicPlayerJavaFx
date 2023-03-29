package com.iandw.musicplayerjavafx;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TableViewContextMenu {

    public static void getContextMenu(ListView<String> artistListView, TableView<TrackMetadata> trackTableView,
                                      ListViewLibrary listViewLibrary, TableViewLibrary tableViewLibrary, TrackIndex trackIndex)
    {
        ContextMenu contextMenu = new ContextMenu();

        // Playlist Options
        ArrayList<MenuItem> playlistMenuList = new ArrayList<>();
        Menu addTrackToPlaylist = new Menu("Add to Playlist");
        MenuItem removeTrackFromPlaylist = new MenuItem("Remove from Playlist");
        final String emptyPlaylist = "* playlists *";
        SeparatorMenuItem divider1 = new SeparatorMenuItem();

        // Add playlists to menu
        for (String playlist : listViewLibrary.getPlaylistObservableList()) {
            if (!Objects.equals(playlist, emptyPlaylist)) {
                playlistMenuList.add(new MenuItem(playlist));
            }
        }

        addTrackToPlaylist.getItems().addAll(playlistMenuList);

        // Edit track data
        Menu editTrack = new Menu("Edit Track");
        MenuItem editArtistName = new MenuItem("Artist Name");
        MenuItem editTrackTitle = new MenuItem("Track Title");
        MenuItem editAlbumTitle = new MenuItem("Album Title");
        MenuItem editTrackGenre = new MenuItem("Genre");

        MenuItem deleteTrack = new MenuItem("Delete Track");
        SeparatorMenuItem divider2 = new SeparatorMenuItem();

        editTrack.getItems().addAll(editArtistName, editAlbumTitle, editTrackTitle, editTrackGenre);

        // Explorer/Properties items
        MenuItem openInExplorer = new MenuItem("Open in Explorer");

        // Add track to Playlist
        addTrackToPlaylist.setOnAction(event ->  {
            int tableSize = trackIndex.getTableSize();

            if (tableSize > 0) {
                System.out.printf("Add %s to %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(), ((MenuItem) event.getTarget()).getText());
                trackTableView.getSelectionModel().getSelectedItem().setPlaylistStr(((MenuItem) event.getTarget()).getText());
                System.out.printf("track playlist set to: %s%n", trackTableView.getSelectionModel().getSelectedItem().getPlaylistStr());
                tableViewLibrary.setOutputTrackListOnClose();
            }
        });

        // Remove track from Playlist
        removeTrackFromPlaylist.setOnAction(event -> {
            int tableSize = trackIndex.getTableSize();

            if (tableSize > 0 && !Objects.equals(trackTableView.getSelectionModel().getSelectedItem().getPlaylistStr(), "*")) {
                System.out.printf("Removing %s from %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(),
                        trackTableView.getSelectionModel().getSelectedItem().getPlaylistStr());
                trackTableView.getSelectionModel().getSelectedItem().setPlaylistStr("*");
                tableViewLibrary.setOutputTrackListOnClose();
            }
        });

        // Edit Artist Name
        editArtistName.setOnAction(event -> {
            EditTrackController editTrackController = new EditTrackController();
            String columnName = "Artist Name";
            String currentTrackTitle = trackTableView.getSelectionModel().getSelectedItem().getArtistNameStr();
            String selectedArtist = trackTableView.getSelectionModel().getSelectedItem().getArtistNameStr();
            artistListView.getSelectionModel().select(selectedArtist);
            System.out.println(currentTrackTitle);

            try {
                editTrackController.showEditWindow(columnName, currentTrackTitle, tableViewLibrary.getTrackObservableList(), trackTableView,
                        artistListView, listViewLibrary, tableViewLibrary);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

            trackTableView.refresh();
        });

        // Edit Album Title
        editAlbumTitle.setOnAction(event -> {
            EditTrackController editTrackController = new EditTrackController();
            String columnName = "Album Title";
            String currentTrackAlbum = trackTableView.getSelectionModel().getSelectedItem().getAlbumTitleStr();
            System.out.println(currentTrackAlbum);

            try {
                editTrackController.showEditWindow(columnName, currentTrackAlbum, tableViewLibrary.getTrackObservableList(), trackTableView,
                        artistListView, listViewLibrary, tableViewLibrary);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

            trackTableView.refresh();

        });

        // Edit Track Title
        editTrackTitle.setOnAction(event -> {
            EditTrackController editTrackController = new EditTrackController();
            String columnName = "Track Title";
            String currentTrackTitle = trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr();
            System.out.println(currentTrackTitle);

            try {
                editTrackController.showEditWindow(columnName, currentTrackTitle, tableViewLibrary.getTrackObservableList(), trackTableView,
                        artistListView, listViewLibrary, tableViewLibrary);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

            trackTableView.refresh();
        });

        // Edit Genre
        editTrackGenre.setOnAction(event -> {
            EditTrackController editTrackController = new EditTrackController();
            String columnName = "Genre";
            String currentGenre = trackTableView.getSelectionModel().getSelectedItem().getTrackGenreStr();
            System.out.println(currentGenre);

            try {
                editTrackController.showEditWindow(columnName, currentGenre, tableViewLibrary.getTrackObservableList(), trackTableView,
                        artistListView, listViewLibrary, tableViewLibrary);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }

            trackTableView.refresh();
        });

        // Delete Track
        deleteTrack.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Track");
            alert.setHeaderText("You are about to delete " + trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr());
            alert.setContentText("Would you like to continue?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                // Get current artist selected
                String selectedArtist = trackTableView.getSelectionModel().getSelectedItem().getArtistNameStr();

                // Delete Track
                System.out.printf("Removing %s from %s%n", trackTableView.getSelectionModel().getSelectedItem().getTrackTitleStr(),
                        artistListView.getSelectionModel().getSelectedItem());

                tableViewLibrary.removeTrack(trackTableView.getSelectionModel().getSelectedItem());

                // Refocus on current artist for tableview to refresh
                artistListView.getSelectionModel().select(selectedArtist);

                // Simulate mouse click to update tableview
                if (artistListView.getSelectionModel().getSelectedItem() != null) {
                    MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1,
                            false, false, false, false, true, false, false, true, false, false, null);
                    artistListView.fireEvent(mouseEvent);
                }

                trackTableView.refresh();
            }
        });

        // Open in File Explorer
        openInExplorer.setOnAction(event -> {
            File file = new File(trackTableView.getSelectionModel().getSelectedItem().getTrackPathStr());
            if (file.exists()) {
                Utils.openExplorer(file);
            }
        });

        contextMenu.getItems().addAll(addTrackToPlaylist, removeTrackFromPlaylist, divider1, editTrack, deleteTrack, divider2, openInExplorer);

        trackTableView.setContextMenu(contextMenu);

        trackTableView.refresh();
    }
}
