package edu.gwu.buzzify.firebase;

import android.content.Context;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import edu.gwu.buzzify.drinks.DrinkInfo;
import edu.gwu.buzzify.spotify.SpotifyItem;

/**
 * Handles interactions with Firebase.
 */
public class FirebaseManager {
    private static final String TAG = FirebaseManager.class.getName();

    //References to the root of the Firebase repo as well as the song / drink queues depending on location.
    private Firebase mRootRef, mLocationRef, mSongQueueRef, mDrinkQueueRef;

    /**
     * Receives callbacks on Firebase events.
     */
    private FirebaseEventListener mListener;

    /**
     * Sets up Firebase references and begins listening for changes.
     * @param listener For callbacks.
     * @param context
     * @param locationName Determines which song/drink queue to listen on.
     */
    public FirebaseManager(FirebaseEventListener listener, Context context, String locationName){
        mListener = listener;

        Firebase.setAndroidContext(context);
        mRootRef = new Firebase(Firebase_Constants.FIREBASE_URL);

        Log.d(TAG, "Location name: " + locationName);
        mLocationRef = mRootRef.child(locationName);
        mSongQueueRef = mLocationRef.child(Firebase_Constants.KEY_SONG_QUEUE);
        mDrinkQueueRef = mLocationRef.child(Firebase_Constants.KEY_DRINK_QUEUE);

        if(listener != null) {
            mSongQueueRef.addChildEventListener(new SongChildListener());
            mDrinkQueueRef.addChildEventListener(new DrinkChildListener());
        }
    }

    public void pushSpotifyItem(SpotifyItem item){
        Firebase itemRef = mSongQueueRef.child(item.getId());
        itemRef.setValue(item, item.getCount());
    }

    public void deleteSpotifyItem(SpotifyItem item){
        //Setting an item to null deletes it on Firebase.
        mSongQueueRef.child(item.getId()).setValue(null);
    }

    public void pushDrink(DrinkInfo drink){
        Firebase pushRef = mDrinkQueueRef.push();
        drink.setFirebaseId(pushRef.getKey());
        pushRef.setValue(drink);
    }

    public void deleteDrinkItem(DrinkInfo item){
        //Setting an item to null deletes it on Firebase.
        mDrinkQueueRef.child(item.getFirebaseId()).setValue(null);
    }

    /**
     * Listener for events on the SongQueue.
     */
    private class SongChildListener implements ChildEventListener{
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prev) {

            SpotifyItem item = dataSnapshot.getValue(SpotifyItem.class);

            if(prev != null)
                mListener.onItemInserted(item, prev);
            else
                mListener.onItemAdded(item);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            SpotifyItem item = dataSnapshot.getValue(SpotifyItem.class);
            mListener.onItemUpdated(item, dataSnapshot);

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

            SpotifyItem item = dataSnapshot.getValue(SpotifyItem.class);
            mListener.onItemRemoved(item);

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String prev) {
            if(prev != null){
                SpotifyItem itemMoved = dataSnapshot.getValue(SpotifyItem.class);
                mListener.onItemMoved(itemMoved, prev);
            }
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {}
    }

    /**
     * Listen for events on the DrinkQueue.
     */
    private class DrinkChildListener implements ChildEventListener{
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prev) {
            DrinkInfo item = dataSnapshot.getValue(DrinkInfo.class);

            if(prev != null)
                mListener.onItemInserted(item, prev);
            else
                mListener.onItemAdded(item);

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            DrinkInfo item = dataSnapshot.getValue(DrinkInfo.class);
            mListener.onItemUpdated(item, dataSnapshot);

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            DrinkInfo item = dataSnapshot.getValue(DrinkInfo.class);
            mListener.onItemRemoved(item);

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String prev) {
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {}
    }
}
