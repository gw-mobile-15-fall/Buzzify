package edu.gwu.buzzify.spotify.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;

import edu.gwu.buzzify.QueueFragmentInterface;
import edu.gwu.buzzify.R;
import edu.gwu.buzzify.ViewHolderClickListener;
import edu.gwu.buzzify.common.BundleKeys;
import edu.gwu.buzzify.drinks.DrinkInfo;
import edu.gwu.buzzify.firebase.FirebaseEventListener;
import edu.gwu.buzzify.firebase.FirebaseManager;
import edu.gwu.buzzify.spotify.SpotifyItem;
import edu.gwu.buzzify.spotify.SpotifyItemAdapter;

/**
 * Fragment that displays the songs queue, as maintained on Firebase.
 */
public class SongQueueFragment extends Fragment implements FirebaseEventListener, ViewHolderClickListener {
    private String TAG = SongQueueFragment.class.getName();

    /**
     * Displays the song queue in a list.
     */
    private RecyclerView mRvSongQueue;

    /**
     * Adapter to display SpotifyItem objects in mRvSongQueue
     */
    private SpotifyItemAdapter mSpotifyItemAdapter;

    /**
     * List of SpotifyItem objects in the song queue
     */
    private ArrayList<SpotifyItem> mSongInfos;

    /**
     * Interface to deliver events back to the Activity.
     */
    private QueueFragmentInterface mActivity;

    /**
     * Manager to handle retreiving/storing data to/from Firebase. Although Lint
     * marks this as "never accessed," the creation of the object involves registering for Firebase
     * callbacks, which is all that is needed.
     */
    private FirebaseManager mFirebaseManager;

    /**
     * Name of the bar/club the user is currently at. Determines which Firebase queue to listen on.
     */
    private String mLocationName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view =  inflater.inflate(R.layout.fragment_song_queue, container, false);

        mSongInfos = new ArrayList<SpotifyItem>();
        //createPlaceholderSongs(mSongInfos);
        setupQueue(view);

        //Get location name from Bundle (passed by activity)
        mLocationName = getArguments().getString(BundleKeys.BUNDLE_KEY_LOCATION_NAME);

        //Create Firebase manager (which also begins listening for changes)
        mFirebaseManager = new FirebaseManager(this, getActivity(), mLocationName);

        //Hide button if requested by the activity
        if(getArguments() != null){
            Bundle args = getArguments();
            if(args.getBoolean(BundleKeys.KEY_HIDE_BUTTON)){
                view.findViewById(R.id.btnVoteSong).setVisibility(View.GONE);
            }
        }
        return view;
    }

    /**
     * Setup the views and variables needed to display the song queue
     * @param view
     */
    private void setupQueue(View view){
        LinearLayoutManager queueLayoutManager;
        mRvSongQueue = (RecyclerView) view.findViewById(R.id.rvSongQueue);
        mRvSongQueue.setHasFixedSize(true);

        queueLayoutManager = new LinearLayoutManager(getContext());

        mRvSongQueue.setLayoutManager(queueLayoutManager);

        mSpotifyItemAdapter = new SpotifyItemAdapter(mSongInfos, getContext(), this);
        mRvSongQueue.setAdapter(mSpotifyItemAdapter);
    }

    /**
     * Called by the Firebase manager if an object was removed (dequeued) from the song queue.
     * @param toRemove
     */
    @Override
    public void onItemRemoved(Object toRemove) {
        if(toRemove instanceof DrinkInfo)
            return;

        //Reflect the change in the list of SpotifyInfo objects
        SpotifyItem item = (SpotifyItem)toRemove;
        mSongInfos.remove(item);
        mSpotifyItemAdapter.notifyDataSetChanged();
        Log.d(TAG, "Removed: " + item.getLine1());

        //Notify the activity that a new song is playing
        if(mActivity != null)
            mActivity.onNewSongPlaying(item);
    }

    /**
     * Called by Firebase manager if a song moves up or down (changes priority) in the queue.
     * @param moved
     * @param prevId
     */
    @Override
    public void onItemMoved(Object moved, String prevId) {
        if(moved instanceof DrinkInfo)
            return;

        //Remove the song that will be moved
        SpotifyItem toMove = (SpotifyItem)moved;
        mSongInfos.remove(toMove);

        SpotifyItem prevItem = new SpotifyItem();
        prevItem.setId(prevId);

        //Find the index of the preceeding item
        int prevIndex = mSongInfos.indexOf(prevItem);
        if(prevIndex == -1)
            prevIndex = 0;

        //Perform the move
        mSongInfos.add(prevIndex, toMove);
        Log.d(TAG, "Moved: " + toMove.getLine1() + ", to index: " + prevIndex);
        mSpotifyItemAdapter.notifyDataSetChanged();

    }

    /**
     * Called by the FirebaseManager when a new item (song) has been added to the queue.
     * @param toAdd
     */
    @Override
    public void onItemAdded(Object toAdd) {
        if(toAdd instanceof DrinkInfo)
            return;

        //Reflect changes in the song list
        SpotifyItem item = (SpotifyItem)toAdd;
        mSongInfos.add(item);
        mSpotifyItemAdapter.notifyDataSetChanged();
        Log.d(TAG, "Added song: " + item.getLine1());
    }

    /**
     * Called by the FirebaseManager when a new item (song) has been inserted at a specific position
     * @param toInsert
     * @param prevId
     */
    @Override
    public void onItemInserted(Object toInsert, String prevId) {
        if(toInsert instanceof DrinkInfo)
            return;
        SpotifyItem prevItem = new SpotifyItem();
        prevItem.setId(prevId);

        //Get position of the preceeding item
        int prevIndex = mSongInfos.indexOf(prevItem);

        if(prevIndex == -1)
            prevIndex = 0;

        //Perform insertion
        mSongInfos.add(prevIndex, (SpotifyItem)toInsert);
        Log.d(TAG, "Added: " + ((SpotifyItem) toInsert).getLine1() + ", at index: " + prevIndex);
        mSpotifyItemAdapter.notifyDataSetChanged();
    }

    /**
     * Called by the FirebaseManager when an item has changed (ex. its vote count)
     * @param item
     * @param snapshot
     */
    @Override
    public void onItemUpdated(Object item, DataSnapshot snapshot) {
        if(item instanceof DrinkInfo)
            return;

        //Update the item with its new count
        long count = (long)snapshot.child(SpotifyItem.KEY_COUNT).getValue();
        mSongInfos.get(mSongInfos.indexOf((SpotifyItem)item)).setCount(count);
        mSpotifyItemAdapter.notifyDataSetChanged();
    }

    //Retrieves the given item from the SongInfo list.
    public SpotifyItem getSpotifyItem(SpotifyItem search){
        int index = mSongInfos.indexOf(search);

        if(index == -1)
            return null;
        return mSongInfos.get(index);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof QueueFragmentInterface)
            mActivity = (QueueFragmentInterface)context;
    }

    /**
     * Notify the activity when a song in the queue has been pressed.
     * @param view
     * @param title
     * @param position
     */
    @Override
    public void onClick(View view, String title, int position) {
        if(mActivity != null)
            mActivity.onItemPressed(mSongInfos.get(position));
    }
}
