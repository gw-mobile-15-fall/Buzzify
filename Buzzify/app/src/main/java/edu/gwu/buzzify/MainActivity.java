package edu.gwu.buzzify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.gwu.buzzify.location.LocationWrapper;
import edu.gwu.buzzify.queues.DrinkInfo;
import edu.gwu.buzzify.queues.DrinkQueueAdapter;
import edu.gwu.buzzify.queues.SongInfo;
import edu.gwu.buzzify.queues.SongQueueAdapter;

/**
 * Created by cheng on 10/19/15.
 */
public class MainActivity extends AppCompatActivity {
    public static final String BUNDLE_KEY_LOCATION = "location";

    private static final String TAG = MainActivity.class.getName();
    private LocationWrapper mLocation;

    private RecyclerView mRvSongQueue, mRvDrinkQueue;
    private SongQueueAdapter mSongQueueAdapter;
    private DrinkQueueAdapter mDrinkQueueAdapter;

    private ArrayList<SongInfo> mSongInfos;
    private ArrayList<DrinkInfo> mDrinkInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the ActionBar for pre-Lollipop devices
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLocation = (LocationWrapper)getIntent().getParcelableExtra(BUNDLE_KEY_LOCATION);

        mSongInfos = new ArrayList<SongInfo>();
        mDrinkInfos = new ArrayList<DrinkInfo>();
        createPlaceholderSongs(mSongInfos);
        createPlaceholderDrinks(mDrinkInfos);
        setupQueues();
    }

    private void setupQueues(){
        LinearLayoutManager queueLayoutManager;
        mRvSongQueue = (RecyclerView) findViewById(R.id.rvSongQueue);
        mRvSongQueue.setHasFixedSize(true);

        queueLayoutManager = new LinearLayoutManager(this);

        mRvSongQueue.setLayoutManager(queueLayoutManager);

        mSongQueueAdapter = new SongQueueAdapter(mSongInfos);
        mRvSongQueue.setAdapter(mSongQueueAdapter);

        LinearLayoutManager queueLayoutManager2;
        queueLayoutManager2 = new LinearLayoutManager(this);

        mRvDrinkQueue = (RecyclerView)findViewById(R.id.rvDrinkQueue);
        mRvDrinkQueue.setHasFixedSize(true);
        mRvDrinkQueue.setLayoutManager(queueLayoutManager2);

        mDrinkQueueAdapter = new DrinkQueueAdapter(mDrinkInfos);
        mRvDrinkQueue.setAdapter(mDrinkQueueAdapter);
    }

    private void createPlaceholderSongs(List<SongInfo> output){
        for(int i = 0; i < 10; i++)
            output.add(new SongInfo(getString(R.string.placeholder_song),
                    getString(R.string.placeholder_artist),
                    getString(R.string.placeholder_album),
                    ""));
    }

    private void createPlaceholderDrinks(List<DrinkInfo> output){
        for(int i = 0; i < 10; i++)
            output.add(new DrinkInfo(getString(R.string.placeholder_drink), null));
    }

    public void onClick(View v){

    }
}

