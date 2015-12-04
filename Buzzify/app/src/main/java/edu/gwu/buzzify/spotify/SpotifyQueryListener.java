package edu.gwu.buzzify.spotify;

import java.util.List;

import edu.gwu.buzzify.models.SpotifyItem;

/**
 * Created by Nick on 11/26/2015.
 */
public interface SpotifyQueryListener {
    public void onArtistsParsed(List<SpotifyItem> artists);
    public void onAlbumsParsed(List<SpotifyItem> albums);
    public void onSongsParsed(List<SpotifyItem> songs);
    public void onSingleSongParsed(SpotifyItem song);
    public void onQueryFailed(byte code);
}
