package edu.gwu.buzzify.spotify.fragments;

import edu.gwu.buzzify.spotify.SpotifyItem;

/**
 * Listen to callback events from the Spotify Query fragments.
 */
public interface SpotifyFragmentListener {
    void onArtistSelected(SpotifyItem artist);
    void onAlbumSelected(SpotifyItem album);
    void onSongSelected(SpotifyItem song);
}
