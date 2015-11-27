package edu.gwu.buzzify.spotify;

import edu.gwu.buzzify.models.SpotifyItem;

/**
 * Created by Nick on 11/27/2015.
 */
public interface SpotifyFragmentListener {
    void onArtistSelected(SpotifyItem artist);
    void onAlbumSelected(SpotifyItem album);
    void onSongSelected(SpotifyItem song);
}
