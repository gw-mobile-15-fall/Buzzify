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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import edu.gwu.buzzify.common.API_URLS;
import edu.gwu.buzzify.queues.SongInfo;
import edu.gwu.buzzify.queues.SongQueueAdapter;

/**
 * Created by cheng on 11/23/15.
 */
public class SpotifySearchActivity extends AppCompatActivity {
    private static final String TAG = SpotifySearchActivity.class.getName();

    private RecyclerView mRvSongQueue;
    private SongQueueAdapter mSongQueueAdapter;
    private ArrayList<SongInfo> mSongInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_search);

        //Set the ActionBar for pre-Lollipop devices
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        mSongInfos = new ArrayList<SongInfo>();
        setupQueue();
    }

    private void setupQueue(){
        LinearLayoutManager queueLayoutManager;
        mRvSongQueue = (RecyclerView) findViewById(R.id.rvSearchResults);
        mRvSongQueue.setHasFixedSize(true);

        queueLayoutManager = new LinearLayoutManager(this);

        mRvSongQueue.setLayoutManager(queueLayoutManager);

        mSongQueueAdapter = new SongQueueAdapter(mSongInfos, this);
        mRvSongQueue.setAdapter(mSongQueueAdapter);
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
            handleSearch(query);
        }
    }

    private void handleSearch(String query){
        Log.d(TAG, "User is searching for: " + query);
        mSongInfos.clear();
        mSongQueueAdapter.notifyDataSetChanged();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        StringBuilder url = new StringBuilder(API_URLS.SPOTIFY_SEARCH)
                .append(API_URLS.SPOTIFY_SEARCH_QUERY)
                .append(query.replaceAll(" ", "+"))
                .append(API_URLS.SPOTIFY_SEARCH_TYPES)
                .append(API_URLS.SPOTIFY_SEARCH_MARKET)
                .append(API_URLS.SPOTIFY_SEARCH_LIMIT);

        Log.d(TAG, "Making request to URL: " + url.toString());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d(TAG, "Response is: "+ response);
                        parseSearchQuery(response);
                        mSongQueueAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseSearchQuery(String response){
        JsonParser parser = new JsonParser();
        JsonObject topObject = parser.parse(response).getAsJsonObject().get("tracks").getAsJsonObject();
        JsonArray trackListing = topObject.get("items").getAsJsonArray();

        if(topObject.get("total").getAsInt() == 0){
            Toast.makeText(this, "No songs found!", Toast.LENGTH_LONG).show();
            return;
        }

        for(int i = 0; i < trackListing.size(); i++){
            JsonObject track = trackListing.get(i).getAsJsonObject();
            String songTitle = track.get("name").getAsString();
            String artistName = track.get("artists").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
            String albumName = track.get("album").getAsJsonObject().get("name").getAsString();
            String albumArtUrl = track.get("album").getAsJsonObject().get("images").getAsJsonArray()
                    .get(0).getAsJsonObject().get("url").getAsString();

            Log.d(TAG, "Result " + i + ": " + songTitle + ", " + artistName + ", " + albumName + " (" + albumArtUrl
            + ")");

            mSongInfos.add(new SongInfo(songTitle, artistName, albumName, albumArtUrl));
        }
    }
}
