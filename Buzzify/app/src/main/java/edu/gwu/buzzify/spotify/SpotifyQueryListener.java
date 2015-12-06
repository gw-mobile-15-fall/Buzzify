package edu.gwu.buzzify.spotify;

import java.util.List;

/**
 * Callbacks from the QueryManager after queries are executed.
 */
public interface SpotifyQueryListener {
    public void onArtistsParsed(List<SpotifyItem> artists);
    public void onAlbumsParsed(List<SpotifyItem> albums);
    public void onSongsParsed(List<SpotifyItem> songs);
    public void onSingleSongParsed(SpotifyItem song);
    public void onQueryFailed(byte code);
}
