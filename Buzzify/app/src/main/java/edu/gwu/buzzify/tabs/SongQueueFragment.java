package edu.gwu.buzzify.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.gwu.buzzify.R;
import edu.gwu.buzzify.queues.SongInfo;
import edu.gwu.buzzify.queues.SongQueueAdapter;

public class SongQueueFragment extends Fragment {
    private String TAG = SongQueueFragment.class.getName();

    private RecyclerView mRvSongQueue;
    private SongQueueAdapter mSongQueueAdapter;
    private ArrayList<SongInfo> mSongInfos;

    public SongQueueFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view =  inflater.inflate(R.layout.fragment_song_queue, container, false);

        mSongInfos = new ArrayList<SongInfo>();
        createPlaceholderSongs(mSongInfos);
        setupQueue(view);
        return view;
    }

    private void createPlaceholderSongs(List<SongInfo> output){
        for(int i = 0; i < 10; i++)
            output.add(new SongInfo(getString(R.string.placeholder_song),
                    getString(R.string.placeholder_artist),
                    getString(R.string.placeholder_album),
                    ""));
    }

    private void setupQueue(View view){
        LinearLayoutManager queueLayoutManager;
        mRvSongQueue = (RecyclerView) view.findViewById(R.id.rvSongQueue);
        mRvSongQueue.setHasFixedSize(true);

        queueLayoutManager = new LinearLayoutManager(getContext());

        mRvSongQueue.setLayoutManager(queueLayoutManager);

        mSongQueueAdapter = new SongQueueAdapter(mSongInfos);
        mRvSongQueue.setAdapter(mSongQueueAdapter);
    }


}
