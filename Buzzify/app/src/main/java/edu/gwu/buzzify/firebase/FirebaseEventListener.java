package edu.gwu.buzzify.firebase;

import com.firebase.client.DataSnapshot;

/**
 * Listen to Firebase events. Called by the FirebaseManager.
 */
public interface FirebaseEventListener {
    void onItemRemoved(Object toRemove);
    void onItemMoved(Object moved, String prevId);
    void onItemAdded(Object toAdd);
    void onItemInserted(Object toInsert, String prevId);
    void onItemUpdated(Object item, DataSnapshot snapshot);
}
