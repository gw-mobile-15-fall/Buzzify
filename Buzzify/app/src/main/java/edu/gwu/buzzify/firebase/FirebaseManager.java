package edu.gwu.buzzify.firebase;

import android.content.Context;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import edu.gwu.buzzify.models.SpotifyItem;

/**
 * Created by cheng on 12/4/15.
 */
public class FirebaseManager {
    private static final String TAG = FirebaseManager.class.getName();

    private Firebase mRootRef, mQueueRef;
    private FirebaseEventListener mListener;

    public FirebaseManager(FirebaseEventListener listener, Context context){
        mListener = listener;

        Firebase.setAndroidContext(context);
        mRootRef = new Firebase(Firebase_Constants.FIREBASE_URL);
        mQueueRef = mRootRef.child(Firebase_Constants.QUEUE_KEY);

        if(listener != null)
            mQueueRef.addChildEventListener(new SongQueueEventListener());
    }

    public void pushSpotifyItem(SpotifyItem item){
        Firebase itemRef = mQueueRef.child(item.getId());
        itemRef.setValue(item, item.getCount());
    }

    public void deleteItem(SpotifyItem item, String key){
        mQueueRef.child(key).setValue(null);
    }

    private class SongQueueEventListener implements ChildEventListener{
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
}
