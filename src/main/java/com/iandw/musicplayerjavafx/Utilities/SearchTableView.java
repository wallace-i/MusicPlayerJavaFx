package com.iandw.musicplayerjavafx.Utilities;

import com.iandw.musicplayerjavafx.TrackMetadata;
import javafx.scene.control.ListView;
import java.util.function.Predicate;

public class SearchTableView {
    private String artistNameString;
    private String playlistTitleString;
    private ListView<String> artistListView;
    private ListView<String> playlistListView;

    public SearchTableView() {}

    public Predicate<TrackMetadata> createArtistsListPredicate(String artistNameString, ListView<String> artistListView) {
        this.artistNameString = artistNameString;
        this.artistListView = artistListView;

        return this::artistsListTrackSearch;
    }

    public Predicate<TrackMetadata> createPlaylistsListPredicate(String playlistTitleString, ListView<String> playlistListView) {
        this.playlistTitleString = playlistTitleString;
        this.playlistListView = playlistListView;

        return this::playlistsListTrackSearch;
    }

    public Predicate<TrackMetadata> createSearchPredicate(String searchText) {
        return track -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            return searchMetadata(track, searchText);
        };
    }

    private boolean artistsListTrackSearch(TrackMetadata trackMetadata) {
        if (trackMetadata.getArtistNameStr().contains(artistNameString) && artistListView != null) {
            return trackMetadata.getArtistNameStr().contains(artistListView.getSelectionModel().getSelectedItem());
        }

        return false;
    }

    private boolean playlistsListTrackSearch(TrackMetadata trackMetadata) {
        if (trackMetadata.getPlaylistStr().contains(playlistTitleString)) {
            return trackMetadata.getPlaylistStr().contains(playlistListView.getSelectionModel().getSelectedItem());
        }

        return false;
    }

    private boolean searchMetadata(TrackMetadata trackMetadata, String searchText) {
        return (trackMetadata.getTrackTitleStr().toLowerCase().contains(searchText.toLowerCase()) ||
                trackMetadata.getAlbumTitleStr().toLowerCase().contains(searchText.toLowerCase()) ||
                trackMetadata.getArtistNameStr().toLowerCase().contains(searchText.toLowerCase()) ||
                trackMetadata.getTrackGenreStr().toLowerCase().contains(searchText.toLowerCase()) ||
                trackMetadata.getPlaylistStr().toLowerCase().contains(searchText.toLowerCase())
        );
    }
}
