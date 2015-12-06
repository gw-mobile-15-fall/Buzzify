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


public class DrinkQueueFragment extends Fragment implements FirebaseEventListener, ViewHolderClickListener{
    public String KEY_HIDE_BUTTON = "hideButton";

    private String TAG = DrinkQueueFragment.class.getName();

    private RecyclerView mRvDrinkQueue;
    private DrinkItemAdapter mDrinkQueueAdapter;
    private ArrayList<DrinkInfo> mDrinkInfos;

    private FirebaseManager mFirebaseManager;
    private String mLocationName, mUserFullName;

    private QueueFragmentInterface mActivity;

    public DrinkQueueFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view =  inflater.inflate(R.layout.fragment_drink_queue, container, false);

        mDrinkInfos = new ArrayList<DrinkInfo>();
        //createPlaceholderDrinks(mDrinkInfos);
        setupQueue(view);

        mLocationName = getArguments().getString(BundleKeys.BUNDLE_KEY_LOCATION_NAME);
        mUserFullName = getArguments().getString(BundleKeys.BUNDLE_KEY_FULLNAME);
        mFirebaseManager = new FirebaseManager(this, getActivity(), mLocationName);

        if(getArguments() != null){
            Bundle args = getArguments();
            if(args.getBoolean(KEY_HIDE_BUTTON)){
                view.findViewById(R.id.btnOrderDrink).setVisibility(View.GONE);
            }
        }

        return view;
    }

    private void setupQueue(View view){
        LinearLayoutManager queueLayoutManager;
        mRvDrinkQueue = (RecyclerView) view.findViewById(R.id.rvDrinkQueue);
        mRvDrinkQueue.setHasFixedSize(true);

        queueLayoutManager = new LinearLayoutManager(getContext());

        mRvDrinkQueue.setLayoutManager(queueLayoutManager);

        mDrinkQueueAdapter = new DrinkItemAdapter(mDrinkInfos, getActivity(), this);
        mRvDrinkQueue.setAdapter(mDrinkQueueAdapter);
    }

    @Override
    public void onItemRemoved(Object toRemove) {
        if(toRemove instanceof SpotifyItem)
            return;
        DrinkInfo item = (DrinkInfo)toRemove;
        mDrinkInfos.remove(item);
        mDrinkQueueAdapter.notifyDataSetChanged();
        Log.d(TAG, "Removed: " + item.getDrinkName());

        if(mActivity != null && item.getCustomerName().equals(mUserFullName))
            mActivity.onDrinkFinished(item);
    }

    @Override
    public void onItemMoved(Object moved, String prevId) {
        if(moved instanceof SpotifyItem)
            return;
        DrinkInfo toMove = (DrinkInfo)moved;
        mDrinkInfos.remove(toMove);

        DrinkInfo prevItem = new DrinkInfo();
        prevItem.setFirebaseId(prevId);

        int prevIndex = mDrinkInfos.indexOf(prevItem);

        if(prevIndex == -1)
            prevIndex = 0;

        Log.d(TAG, "Array size = " + mDrinkInfos.size() + ", prevIndex = " + prevIndex);
        mDrinkInfos.add(mDrinkInfos.size(), toMove);
        Log.d(TAG, "Moved: " + toMove.getDrinkName() + ", to index: " + prevIndex);
        mDrinkQueueAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemAdded(Object toAdd) {
        if(toAdd instanceof SpotifyItem)
            return;
        DrinkInfo item = (DrinkInfo)toAdd;
        Log.d(TAG, "Array size = " + mDrinkInfos.size());
        mDrinkInfos.add(mDrinkInfos.size(), item);
        mDrinkQueueAdapter.notifyDataSetChanged();
        Log.d(TAG, "Added song: " + item.getDrinkName());
    }

    @Override
    public void onItemInserted(Object toInsert, String prevId) {
        if(toInsert instanceof SpotifyItem)
            return;
        DrinkInfo prevItem = new DrinkInfo();
        prevItem.setFirebaseId(prevId);

        int prevIndex = mDrinkInfos.indexOf(prevItem);

        if(prevIndex == -1)
            prevIndex = 0;
        Log.d(TAG, "Array size = " + mDrinkInfos.size() + ", prevIndex = " + prevIndex);
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

        if(context instanceof QueueFragmentInterface)
            mActivity = (QueueFragmentInterface)context;
    }

    @Override
    public void onClick(View view, String title, int position) {
        if(mActivity != null)
            mActivity.onItemPressed(mDrinkInfos.get(position));
    }
}
