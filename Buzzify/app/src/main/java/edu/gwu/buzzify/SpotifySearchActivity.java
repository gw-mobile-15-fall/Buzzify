package edu.gwu.buzzify;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.parse.GetDataCallback;
import com.parse.ParseException;

import edu.gwu.buzzify.common.ParseUtils;
import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.models.SpotifyItem;
import edu.gwu.buzzify.spotify.QueryAlbumFragment;
import edu.gwu.buzzify.spotify.QueryAllFragment;
import edu.gwu.buzzify.spotify.QueryArtistFragment;
import edu.gwu.buzzify.spotify.SpotifyFragmentListener;

/**
 * Created by cheng on 11/23/15.
 */
public class SpotifySearchActivity extends AppCompatActivity implements SpotifyFragmentListener, GetDataCallback {
    public static final String KEY_CHOSEN_SONG = "chosen_song";
    public static final String KEY_STARTED_FOR_RESULT = "started_for_result";

    private static final String TAG = SpotifySearchActivity.class.getName();
    private NavDrawer mDrawer;
    private FragmentManager mFragmentManager;
    private SearchView mSearchView;
    private Toolbar mToolbar;

    private String mName;
    private String mEmail;
    private Bitmap mProfilePhoto = null;

    private boolean mStartedForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_search);

        //Set the ActionBar for pre-Lollipop devices
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mName = ParseUtils.getUserActualName();
        mEmail = ParseUtils.getUserEmail();
        ParseUtils.getUserProfilePhoto(this);

        mFragmentManager = getSupportFragmentManager();
        mStartedForResult = getIntent().getBooleanExtra(KEY_STARTED_FOR_RESULT, false);
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

    private void startAlbumFragment(String albumId, String albumName){
        Log.d(TAG, "Starting QueryAlbumFragment");
        QueryAlbumFragment fragment = new QueryAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putString(QueryAlbumFragment.KEY_ALBUM_ID, albumId);
        bundle.putString(QueryAlbumFragment.KEY_ALBUM_NAME, albumName);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.spotifyFragmentContainer, fragment, "QueryAlbumFragment");
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
        startAlbumFragment(album.getId(), album.getLine1());
    }

    @Override
    public void onSongSelected(SpotifyItem song) {
        Log.d(TAG, "Song " + song.getLine1() + " selected");

        if(mStartedForResult) {
            Intent resultData = new Intent();
            resultData.putExtra(KEY_CHOSEN_SONG, song);

            setResult(MainActivity.CODE_RESULT_SONG_CHOSEN, resultData);
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        if(mDrawer.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            mDrawer.getDrawerLayout().closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void done(byte[] data, ParseException e) {
        if (e == null) {
            mProfilePhoto = BitmapFactory.decodeByteArray(data, 0, data.length);

            if (mProfilePhoto == null) {
                Log.e(TAG, "BitmapFactory failed at creating bitmap from ByteArray");
            }

            //TODO placeholders if name, email, or bitmap is null
            mDrawer = new NavDrawer(this, mToolbar, mName, mEmail, mProfilePhoto);

        } else {
            Log.e(TAG,"ParseFile getDataInBackground returned an exception");
        }
    }
}
