package edu.gwu.buzzify;

import android.content.Context;
import android.util.Log;

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
import java.util.List;

import edu.gwu.buzzify.common.API_URLS;
import edu.gwu.buzzify.queues.SpotifyItem;

/**
 * Created by Nick on 11/26/2015.
 */
public class SpotifyQueryManager {
    private static final String TAG= SpotifyQueryManager.class.getName();
    private Context mContext;
    private SpotifyQueryListener mListener;

    public SpotifyQueryManager(Context context, SpotifyQueryListener listener){
        mContext = context;
        mListener = listener;
    }

    public void searchSongs(String query){
        handleQuery(query,
                API_URLS.SPOTIFY_SEARCH_TYPE_TRACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mListener.onTracksParsed(parseSongsFromQuery(response));
                    }
                });
    }

    public void handleQuery(String query, String searchTypes, Response.Listener<String> responseListener){
        Log.d(TAG, "User is searching for: " + query);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringBuilder url = new StringBuilder(API_URLS.SPOTIFY_SEARCH)
                .append(API_URLS.SPOTIFY_SEARCH_QUERY)
                .append(query.replaceAll(" ", "+"))
                .append(searchTypes)
                .append(API_URLS.SPOTIFY_SEARCH_MARKET)
                .append(API_URLS.SPOTIFY_SEARCH_LIMIT);

        Log.d(TAG, "Making request to URL: " + url.toString());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(), responseListener, mErrorListener);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private List<SpotifyItem> parseSongsFromQuery(String response){
        JsonParser parser = new JsonParser();
        JsonObject topObject = parser.parse(response).getAsJsonObject().get("tracks").getAsJsonObject();
        JsonArray trackListing = topObject.get("items").getAsJsonArray();
        List<SpotifyItem> songItems = new ArrayList<>();

        if(topObject.get("total").getAsInt() == 0)
            return null;


        for(int i = 0; i < trackListing.size(); i++){
            JsonObject track = trackListing.get(i).getAsJsonObject();
            String songTitle = track.get("name").getAsString();
            String artistName = track.get("artists").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
            String albumName = track.get("album").getAsJsonObject().get("name").getAsString();
            String albumArtUrl = track.get("album").getAsJsonObject().get("images").getAsJsonArray()
                    .get(0).getAsJsonObject().get("url").getAsString();

            Log.d(TAG, "Result " + i + ": " + songTitle + ", " + artistName + ", " + albumName + " (" + albumArtUrl
                    + ")");

            songItems.add(new SpotifyItem(songTitle, artistName, albumName, albumArtUrl, ""));
        }
        return songItems;
    }

    private final Response.ErrorListener mErrorListener = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            mListener.onQueryFailed(ERROR_CODES.ERROR_QUERY_FAILED);
        }
    };

    public static class ERROR_CODES{
        public static final byte ERROR_QUERY_FAILED = 0x00;
    }
}
