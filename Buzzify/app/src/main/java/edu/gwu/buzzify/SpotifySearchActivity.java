package edu.gwu.buzzify;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.models.SpotifyItem;
import edu.gwu.buzzify.spotify.QueryAllFragment;
import edu.gwu.buzzify.spotify.QueryArtistFragment;
import edu.gwu.buzzify.spotify.SpotifyFragmentListener;

/**
 * Created by cheng on 11/23/15.
 */
public class SpotifySearchActivity extends AppCompatActivity implements SpotifyFragmentListener{
    private static final String TAG = SpotifySearchActivity.class.getName();
    private NavDrawer mDrawer;
    private FragmentManager mFragmentManager;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_search);

        //Set the ActionBar for pre-Lollipop devices
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawer = new NavDrawer(this, toolbar, "username", "email", 0);
        mFragmentManager = getSupportFragmentManager();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_spotify_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent){
        setIntent(intent);
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
            String query = intent.getStringExtra(SearchManager.QUERY);
            mSearchView.clearFocus();
            startAllResultsFragment(query);
        }
    }

    private void startAllResultsFragment(String query){
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //Clear all fragments on backstack
        QueryAllFragment fragment = new QueryAllFragment();

        Log.d(TAG, "Starting QueryAllFragment");
        Bundle bundle = new Bundle();
        bundle.putString(QueryAllFragment.KEY_SEARCH_QUERY, query);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.spotifyFragmentContainer, fragment, "QueryAllFragment");
        transaction.commit();
    }

    private void startArtistFragment(String artistId, String artistName){
        Log.d(TAG, "Starting QueryArtistFragment");
        QueryArtistFragment fragment = new QueryArtistFragment();
        Bundle bundle = new Bundle();
        bundle.putString(QueryArtistFragment.KEY_ARTIST_ID, artistId);
        bundle.putString(QueryArtistFragment.KEY_ARTIST_NAME, artistName);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.spotifyFragmentContainer, fragment, "QueryArtistFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onArtistSelected(SpotifyItem artist) {
        Log.d(TAG, "Artist " + artist.getLine1() + " selected " + "(" + artist.getId() + ")");
        startArtistFragment(artist.getId(), artist.getLine1());
    }

    @Override
    public void onAlbumSelected(SpotifyItem album) {
        Log.d(TAG, "Album " + album.getLine1() + " selected");
    }

    @Override
    public void onSongSelected(SpotifyItem song) {
        Log.d(TAG, "Song " + song.getLine1() + " selected");
    }

    @Override
    public void onBackPressed(){
        if(mDrawer.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            mDrawer.getDrawerLayout().closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}
