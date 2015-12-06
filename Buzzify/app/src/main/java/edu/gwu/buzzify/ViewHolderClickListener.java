package edu.gwu.buzzify;

import android.view.View;

/**
 * Listen to onClick events from RecyclerView items
 */
public interface ViewHolderClickListener {
    public void onClick(View view, String title, int position);
}
