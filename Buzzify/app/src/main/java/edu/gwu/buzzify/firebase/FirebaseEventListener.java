package edu.gwu.buzzify.firebase;

import com.firebase.client.DataSnapshot;

/**
 * Created by cheng on 12/4/15.
 */
public interface FirebaseEventListener {
    void onItemRemoved(Object toRemove);
    void onItemMoved(Object moved, String prevId);
    void onItemAdded(Object toAdd);
    void onItemInserted(Object toInsert, String prevId);
    void onItemUpdated(Object item, DataSnapshot snapshot);
}
