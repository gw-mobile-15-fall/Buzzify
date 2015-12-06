package edu.gwu.buzzify;

import edu.gwu.buzzify.drinks.DrinkInfo;
import edu.gwu.buzzify.spotify.SpotifyItem;

/**
 * Created by cheng on 12/4/15.
 */
public interface QueueFragmentInterface {
    void onItemPressed(Object item);
    void onNewSongPlaying(SpotifyItem item);
    void onDrinkFinished(DrinkInfo drink);
}
