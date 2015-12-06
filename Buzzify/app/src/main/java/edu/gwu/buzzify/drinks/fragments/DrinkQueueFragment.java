package edu.gwu.buzzify.drinks.fragments;

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
import edu.gwu.buzzify.drinks.DrinkItemAdapter;
import edu.gwu.buzzify.firebase.FirebaseEventListener;
import edu.gwu.buzzify.firebase.FirebaseManager;
import edu.gwu.buzzify.spotify.SpotifyItem;

/**
 * Fragment that displays the ordered drinks queue, as maintained on Firebase.
 */
public class DrinkQueueFragment extends Fragment implements FirebaseEventListener, ViewHolderClickListener{
    private String TAG = DrinkQueueFragment.class.getName();

    /**
     * Displays the drink queue in a list.
     */
    private RecyclerView mRvDrinkQueue;

    /**
     * Adapter to display DrinkInfo objects in mRvDrinkQueue
     */
    private DrinkItemAdapter mDrinkQueueAdapter;

    /**
     * List of DrinkInfo objects in the drink queue
     */
    private ArrayList<DrinkInfo> mDrinkInfos;

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

    /**
     * User's file name, to determine if a dequeued drink belongs to them.
     */
    private String mUserFullName;

    /**
     * Interface to deliver events back to the Activity.
     */
    private QueueFragmentInterface mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view =  inflater.inflate(R.layout.fragment_drink_queue, container, false);

        mDrinkInfos = new ArrayList<DrinkInfo>();
        setupQueue(view);

        //Get location name and username from Bundle (passed by activity)
        mLocationName = getArguments().getString(BundleKeys.BUNDLE_KEY_LOCATION_NAME);
        mUserFullName = getArguments().getString(BundleKeys.BUNDLE_KEY_FULLNAME);

        //Create Firebase manager (which also begins listening for changes)
        mFirebaseManager = new FirebaseManager(this, getActivity(), mLocationName);

        //Hide button if requested by the activity
        if(getArguments() != null){
            Bundle args = getArguments();
            if(args.getBoolean(BundleKeys.KEY_HIDE_BUTTON)){
                view.findViewById(R.id.btnOrderDrink).setVisibility(View.GONE);
            }
        }

        return view;
    }

    /**
     * Setup the views and variables needed to display the drink queue
     * @param view
     */
    private void setupQueue(View view){
        LinearLayoutManager queueLayoutManager;
        mRvDrinkQueue = (RecyclerView) view.findViewById(R.id.rvDrinkQueue);
        mRvDrinkQueue.setHasFixedSize(true);

        queueLayoutManager = new LinearLayoutManager(getContext());

        mRvDrinkQueue.setLayoutManager(queueLayoutManager);

        mDrinkQueueAdapter = new DrinkItemAdapter(mDrinkInfos, getActivity(), this);
        mRvDrinkQueue.setAdapter(mDrinkQueueAdapter);
    }

    /**
     * Called by the Firebase manager if an object was removed (dequeued) from the drink queue.
     * @param toRemove
     */
    @Override
    public void onItemRemoved(Object toRemove) {
        if(toRemove instanceof SpotifyItem)
            return;

        //Reflect the change in the list of DrinkInfo objects
        DrinkInfo item = (DrinkInfo)toRemove;
        mDrinkInfos.remove(item);
        mDrinkQueueAdapter.notifyDataSetChanged();
        Log.d(TAG, "Removed: " + item.getDrinkName());

        //Notify the activity if the dequeued drink was the current user's
        if(mActivity != null && item.getCustomerName().equals(mUserFullName))
            mActivity.onDrinkFinished(item);
    }

    /**
     * Called by Firebase manager if a drink moves up or down in the queue.
     * @param moved
     * @param prevId
     */
    @Override
    public void onItemMoved(Object moved, String prevId) {
        if(moved instanceof SpotifyItem)
            return;
        //Remove the drink that will be moved
        DrinkInfo toMove = (DrinkInfo)moved;
        mDrinkInfos.remove(toMove);

        DrinkInfo prevItem = new DrinkInfo();
        prevItem.setFirebaseId(prevId);

        //Find the index of the preceeding item
        int prevIndex = mDrinkInfos.indexOf(prevItem);

        if(prevIndex == -1)
            prevIndex = 0;

        Log.d(TAG, "Array size = " + mDrinkInfos.size() + ", prevIndex = " + prevIndex);

        //Perform the move
        mDrinkInfos.add(mDrinkInfos.size(), toMove);
        Log.d(TAG, "Moved: " + toMove.getDrinkName() + ", to index: " + prevIndex);
        mDrinkQueueAdapter.notifyDataSetChanged();
    }

    /**
     * Called by the FirebaseManager when a new item (drink) has been added to the queue.
     * @param toAdd
     */
    @Override
    public void onItemAdded(Object toAdd) {
        if(toAdd instanceof SpotifyItem)
            return;

        //Reflect changes in the drink list
        DrinkInfo item = (DrinkInfo)toAdd;
        Log.d(TAG, "Array size = " + mDrinkInfos.size());
        mDrinkInfos.add(mDrinkInfos.size(), item);
        mDrinkQueueAdapter.notifyDataSetChanged();
        Log.d(TAG, "Added song: " + item.getDrinkName());
    }

    /**
     * Called by the FirebaseManager when a new item (drink) has been inserted at a specific position
     * @param toInsert
     * @param prevId
     */
    @Override
    public void onItemInserted(Object toInsert, String prevId) {
        if(toInsert instanceof SpotifyItem)
            return;
        DrinkInfo prevItem = new DrinkInfo();
        prevItem.setFirebaseId(prevId);

        //Get position of the preceeding item
        int prevIndex = mDrinkInfos.indexOf(prevItem);

        if(prevIndex == -1)
            prevIndex = 0;
        Log.d(TAG, "Array size = " + mDrinkInfos.size() + ", prevIndex = " + prevIndex);

        //Perform insertion
        mDrinkInfos.add(mDrinkInfos.size(), (DrinkInfo)toInsert);
        Log.d(TAG, "Added: " + ((DrinkInfo) toInsert).getDrinkName() + ", at index: " + prevIndex);
        mDrinkQueueAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemUpdated(Object item, DataSnapshot snapshot) {

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        if(context instanceof QueueFragmentInterface) //Check if the activity wants callbacks
            mActivity = (QueueFragmentInterface)context;
    }

    /**
     * Notify the activity when a drink in the queue has been pressed.
     * @param view
     * @param title
     * @param position
     */
    @Override
    public void onClick(View view, String title, int position) {
        if(mActivity != null)
            mActivity.onItemPressed(mDrinkInfos.get(position));
    }
}
