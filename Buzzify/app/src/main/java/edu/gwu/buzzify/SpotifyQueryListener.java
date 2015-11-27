package edu.gwu.buzzify;

import java.util.List;

import edu.gwu.buzzify.queues.SpotifyItem;

/**
 * Created by Nick on 11/26/2015.
 */
public interface SpotifyQueryListener {
    public void onTracksParsed(List<SpotifyItem> songs);
    public void onQueryFailed(byte code);
}
