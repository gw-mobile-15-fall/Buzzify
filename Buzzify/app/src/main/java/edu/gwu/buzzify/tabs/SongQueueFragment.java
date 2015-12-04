package edu.gwu.buzzify.tabs;

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
import java.util.List;

import edu.gwu.buzzify.R;
import edu.gwu.buzzify.firebase.FirebaseEventListener;
import edu.gwu.buzzify.firebase.FirebaseManager;
import edu.gwu.buzzify.models.SpotifyItem;
import edu.gwu.buzzify.models.SpotifyItemAdapter;
import edu.gwu.buzzify.models.SpotifyItemViewHolderClickListener;

public class SongQueueFragment extends Fragment implements FirebaseEventListener, SpotifyItemViewHolderClickListener {
    private String TAG = SongQueueFragment.class.getName();

    private RecyclerView mRvSongQueue;
    private SpotifyItemAdapter mSpotifyItemAdapter;
    private ArrayList<SpotifyItem> mSongInfos;
    private FirebaseManager mFirebaseManager;

    private QueueFragmentInterface mActivity;

    public SongQueueFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view =  inflater.inflate(R.layout.fragment_song_queue, container, false);

        mSongInfos = new ArrayList<SpotifyItem>();
        //createPlaceholderSongs(mSongInfos);
        setupQueue(view);

        mFirebaseManager = new FirebaseManager(this, getActivity());
        return view;
    }

    private void createPlaceholderSongs(List<SpotifyItem> output){
        for(int i = 0; i < 10; i++)
            output.add(new SpotifyItem(getString(R.string.placeholder_song),
                    getString(R.string.placeholder_artist),
                    getString(R.string.placeholder_album),
                    "",""));
    }

    private void setupQueue(View view){
        LinearLayoutManager queueLayoutManager;
        mRvSongQueue = (RecyclerView) view.findViewById(R.id.rvSongQueue);
        mRvSongQueue.setHasFixedSize(true);

        queueLayoutManager = new LinearLayoutManager(getContext());

        mRvSongQueue.setLayoutManager(queueLayoutManager);

        mSpotifyItemAdapter = new SpotifyItemAdapter(mSongInfos, getContext(), this);
        mRvSongQueue.setAdapter(mSpotifyItemAdapter);
    }

    @Override
    public void onItemRemoved(Object toRemove) {
        SpotifyItem item = (SpotifyItem)toRemove;
        mSongInfos.remove(item);
        mSpotifyItemAdapter.notifyDataSetChanged();
        Log.d(TAG, "Removed: " + item.getLine1());
    }

    @Override
    public void onItemMoved(Object moved, String prevId) {
        SpotifyItem toMove = (SpotifyItem)moved;
        mSongInfos.remove(toMove);

        SpotifyItem prevItem = new SpotifyItem();
        prevItem.setId(prevId);

        int prevIndex = mSongInfos.indexOf(prevItem);

        mSongInfos.add(prevIndex, toMove);
        Log.d(TAG, "Moved: " + toMove.getLine1() + ", to index: " + prevIndex);
        mSpotifyItemAdapter.notifyDataSetChanged();

    }

    @Override
    public void onItemAdded(Object toAdd) {
        SpotifyItem item = (SpotifyItem)toAdd;
        mSongInfos.add(item);
        mSpotifyItemAdapter.notifyDataSetChanged();
        Log.d(TAG, "Added song: " + item.getLine1());
    }

    @Override
    public void onItemInserted(Object toInsert, String prevId) {
        SpotifyItem prevItem = new SpotifyItem();
        prevItem.setId(prevId);

        int prevIndex = mSongInfos.indexOf(prevItem);

        mSongInfos.add(prevIndex, (SpotifyItem)toInsert);
        Log.d(TAG, "Added: " + prevIndex + ", at index: " + prevIndex);
        mSpotifyItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemUpdated(Object item, DataSnapshot snapshot) {
        long count = (long)snapshot.child(SpotifyItem.KEY_COUNT).getValue();
        mSongInfos.get(mSongInfos.indexOf((SpotifyItem)item)).setCount(count);
        mSpotifyItemAdapter.notifyDataSetChanged();
    }

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

    @Override
    public void onClick(View view, String title, int position) {
        mActivity.onItemPressed(mSongInfos.get(position));
    }
}
