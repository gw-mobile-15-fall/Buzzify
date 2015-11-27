package edu.gwu.buzzify;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.queues.SpotifyItem;
import edu.gwu.buzzify.queues.SpotifyItemAdapter;

/**
 * Created by cheng on 11/23/15.
 */
public class SpotifySearchActivity extends AppCompatActivity implements SpotifyQueryListener {
    private static final String TAG = SpotifySearchActivity.class.getName();

    private RecyclerView mRvSongQueue;
    private SpotifyItemAdapter mSpotifyItemAdapter;
    private ArrayList<SpotifyItem> mSongInfos;

    private NavDrawer mDrawer;
    private SpotifyQueryManager mQueryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_search);

        //Set the ActionBar for pre-Lollipop devices
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        mSongInfos = new ArrayList<SpotifyItem>();
        setupQueue();

        mDrawer = new NavDrawer(this, toolbar, "username", "email", 0);
        mQueryManager = new SpotifyQueryManager(this, this);
    }

    private void setupQueue(){
        LinearLayoutManager queueLayoutManager;
        mRvSongQueue = (RecyclerView) findViewById(R.id.rvSearchResults);
        mRvSongQueue.setHasFixedSize(true);

        queueLayoutManager = new LinearLayoutManager(this);

        mRvSongQueue.setLayoutManager(queueLayoutManager);

        mSpotifyItemAdapter = new SpotifyItemAdapter(mSongInfos, this);
        mRvSongQueue.setAdapter(mSpotifyItemAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_spotify_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent){
        setIntent(intent);
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
            String query = intent.getStringExtra(SearchManager.QUERY);
            mSongInfos.clear();
            mSpotifyItemAdapter.notifyDataSetChanged();
            mQueryManager.searchSongs(query);
        }
    }


    @Override
    public void onTracksParsed(List<SpotifyItem> songs) {
        if(songs == null){
            Toast.makeText(this, "No songs found!", Toast.LENGTH_LONG).show();
            return;
        }

        mSongInfos.addAll(songs);
        mSpotifyItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryFailed(byte code) {
        switch(code){
            case SpotifyQueryManager.ERROR_CODES.ERROR_QUERY_FAILED:
                break;
        }
    }
}
