package edu.gwu.buzzify.models;

import android.view.View;

/**
 * Created by Nick on 11/26/2015.
 */
public interface SpotifyItemViewHolderClickListener {
    public void onClick(View view, String title, int position);
}
