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

import edu.gwu.buzzify.common.BundleKeys;
import edu.gwu.buzzify.common.ParseUtils;
import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.spotify.SpotifyItem;
import edu.gwu.buzzify.spotify.fragments.QueryAlbumFragment;
import edu.gwu.buzzify.spotify.fragments.QueryAllFragment;
import edu.gwu.buzzify.spotify.fragments.QueryArtistFragment;
import edu.gwu.buzzify.spotify.fragments.SpotifyFragmentListener;

/**
 * Allows the user to search for Spotify content.
 */
public class SpotifySearchActivity extends AppCompatActivity implements SpotifyFragmentListener {
    private static final String TAG = SpotifySearchActivity.class.getName();
    private NavDrawer mDrawer;
    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;

    /**
     * Allows users to enter search strings.
     */
    private SearchView mSearchView;

    //Information about the currently logged in user
    private String mName;
    private String mEmail;
    private String mProfilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_search);

        //Set the ActionBar for pre-Lollipop devices
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Get information about current user
        mName = ParseUtils.getUserActualName();
        mEmail = ParseUtils.getUserEmail();
        mProfilePicUrl = ParseUtils.getUserProfilePhotoUrl();

        //Setup navigation drawer
        mDrawer = new NavDrawer(this, mToolbar, mName, mEmail, mProfilePicUrl);

        mFragmentManager = getSupportFragmentManager();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_spotify_search, menu);

        //Setup searching from the action bar
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called when the user "searches" something. The intent will contain the search query.
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent){
        if(intent.getAction().equals(Intent.ACTION_SEARCH)){
            String query = intent.getStringExtra(SearchManager.QUERY);
            mSearchView.clearFocus();

            //Start a fragment to make a Spotify query
            startAllResultsFragment(query);
        }
    }

    /**
     * Start the Fragment to search artists, albums, and tracks matching the search query
     * @param query
     */
    private void startAllResultsFragment(String query){
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //Clear all fragments on backstack
        QueryAllFragment fragment = new QueryAllFragment();

        Log.d(TAG, "Starting QueryAllFragment");
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.KEY_SEARCH_QUERY, query); //Pass the search query to the Fragment
        fragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.spotifyFragmentContainer, fragment, "QueryAllFragment");
        transaction.commit();
    }

    /**
     * Start the Fragment to search artists given the ID and name.
     * @param artistId
     * @param artistName
     */
    private void startArtistFragment(String artistId, String artistName){
        Log.d(TAG, "Starting QueryArtistFragment");
        QueryArtistFragment fragment = new QueryArtistFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.KEY_ARTIST_ID, artistId); //Pass ID to the Fragment
        bundle.putString(BundleKeys.KEY_ARTIST_NAME, artistName); //Pass name to the Fragment
        fragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.spotifyFragmentContainer, fragment, "QueryArtistFragment");
        transaction.addToBackStack(null); //Add to backstack so the user can use the back button
        transaction.commit();
    }

    /**
     * Start the Fragment to search albums given the ID and name.
     * @param albumId
     * @param albumName
     */
    private void startAlbumFragment(String albumId, String albumName){
        Log.d(TAG, "Starting QueryAlbumFragment");
        QueryAlbumFragment fragment = new QueryAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BundleKeys.KEY_ALBUM_ID, albumId); //Pass ID to the Fragment
        bundle.putString(BundleKeys.KEY_ALBUM_NAME, albumName); //Pass name to the Fragment
        fragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.spotifyFragmentContainer, fragment, "QueryAlbumFragment");
        transaction.addToBackStack(null); //Add to backstack so the user can use the back button
        transaction.commit();
    }

    /**
     * When the user has selected an artist, start the Fragment to query for the details of that artist.
     * @param artist
     */
    @Override
    public void onArtistSelected(SpotifyItem artist) {
        Log.d(TAG, "Artist " + artist.getLine1() + " selected " + "(" + artist.getId() + ")");
        startArtistFragment(artist.getId(), artist.getLine1());
    }

    /**
     * When the user has selected an album, start the Fragment to fetch the tracks on the album
     * @param album
     */
    @Override
    public void onAlbumSelected(SpotifyItem album) {
        Log.d(TAG, "Album " + album.getLine1() + " selected");
        startAlbumFragment(album.getId(), album.getLine1());
    }

    /**
     * When the user has selected a song, send it back to the MainActivity in an Intent
     * @param song
     */
    @Override
    public void onSongSelected(SpotifyItem song) {
        Log.d(TAG, "Song " + song.getLine1() + " selected");

        Intent resultData = new Intent();
        resultData.putExtra(BundleKeys.KEY_CHOSEN_SONG, song);
        Log.d(TAG, "Returning song in result intent");
        setResult(MainActivity.CODE_RESULT_SONG_CHOSEN, resultData);
        finish();
    }

    /**
     * If the back button was pressed, close the drawer if open
     */
    @Override
    public void onBackPressed(){
        if(mDrawer.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            mDrawer.getDrawerLayout().closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

}
