package edu.gwu.buzzify.spotify.fragments;

import edu.gwu.buzzify.spotify.SpotifyItem;

/**
 * Created by Nick on 11/27/2015.
 */
public interface SpotifyFragmentListener {
    void onArtistSelected(SpotifyItem artist);
    void onAlbumSelected(SpotifyItem album);
    void onSongSelected(SpotifyItem song);
}
