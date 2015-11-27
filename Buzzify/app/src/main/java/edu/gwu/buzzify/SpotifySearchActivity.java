package edu.gwu.buzzify;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.spotify.AllResultsFragment;

/**
 * Created by cheng on 11/23/15.
 */
public class SpotifySearchActivity extends AppCompatActivity {
    private static final String TAG = SpotifySearchActivity.class.getName();
    private NavDrawer mDrawer;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_search);

        //Set the ActionBar for pre-Lollipop devices
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawer = new NavDrawer(this, toolbar, "username", "email", 0);
        mFragmentManager = getFragmentManager();
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
            startAllResultsFragment(query);
        }
    }

    private void startAllResultsFragment(String query){
        mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //Clear all fragments
        AllResultsFragment fragment = (AllResultsFragment)mFragmentManager.findFragmentByTag("AllResultsFragment");

        if(fragment != null){
            Log.d(TAG, "Fragment still in stack, reusing");
            fragment.newQuery(query);
            return;
        }

        Log.d(TAG, "Starting AllResultsFragment");
        fragment = new AllResultsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AllResultsFragment.KEY_SEARCH_QUERY, query);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.spotifyFragmentContainer, fragment, "AllResultsFragment");
        transaction.commit();
    }
}
