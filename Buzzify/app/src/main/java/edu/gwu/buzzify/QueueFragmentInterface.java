package edu.gwu.buzzify;

import edu.gwu.buzzify.drinks.DrinkInfo;
import edu.gwu.buzzify.spotify.SpotifyItem;

/**
 * Receive callbacks from song/drink queue fragments
 */
public interface QueueFragmentInterface {
    void onItemPressed(Object item);
    void onNewSongPlaying(SpotifyItem item);
    void onDrinkFinished(DrinkInfo drink);
}
