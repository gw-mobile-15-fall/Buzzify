package edu.gwu.buzzify.spotify;

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

/**
 * Executes Spotify queries and notifies a SpotifyQueryListener.
 */
public class SpotifyQueryManager {
    public static final byte ERROR_QUERY_FAILED = 0x00;

    private static final String TAG= SpotifyQueryManager.class.getName();
    private Context mContext;
    private SpotifyQueryListener mListener;

    public SpotifyQueryManager(Context context, SpotifyQueryListener listener){
        mContext = context;
        mListener = listener;
    }

    /**
     * Given a Spotify song ID, executes a query to retrieve the song's information.
     * @param id
     */
    public void searchSongById(String id){
        handleGetSongQuery(id, API_URLS.SPOTIFY_GET_TRACK_MARKET, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                JsonParser parser = new JsonParser();
                JsonObject topObject = parser.parse(response).getAsJsonObject();
                mListener.onSingleSongParsed(parseSingleSong(topObject));
            }
        });
    }

    /**
     * Given a Spotify artist ID, executes a query to retrieve their albums and top songs.
     * @param id
     */
    public void searchArtistById(String id){
        handleGetArtistQuery(id,
                API_URLS.SPOTIFY_GET_ARTIST_ALBUMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mListener.onAlbumsParsed(parseAlbumsFromQuery(response));
                    }
                });

        handleGetArtistQuery(id,
                API_URLS.SPOTIFY_GET_ARTIST_TOP_TRACKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mListener.onSongsParsed(parseSongsFromQuery(response));
                    }
                });
    }

    /**
     * Given an album ID, executes a query to retrieve the songs on the track
     * @param id
     */
    public void searchAlbumById(String id){
        handleGetAlbumQuery(id, API_URLS.SPOTIFY_GET_ALBUM_TRACKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mListener.onSongsParsed(parseSongsFromQuery(response));
                    }
                });
    }

    /**
     * Given a string query, execute a query against artists, albums, and songs.
     * @param query
     */
    public void searchAll(String query){
        handleSearchQuery(query,
                API_URLS.SPOTIFY_SEARCH_TYPE_ARTISTSALBUM, API_URLS.SPOTIFY_SEARCH_LIMIT_2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mListener.onArtistsParsed(parseArtistsFromQuery(response));
                        mListener.onAlbumsParsed(parseAlbumsFromQuery(response));
                    }
                });

        searchSongs(query);
    }

    /**
     * Given a string query, search for matching song titles.
     * @param query
     */
    public void searchSongs(String query){
        handleSearchQuery(query,
                API_URLS.SPOTIFY_SEARCH_TYPE_TRACK, API_URLS.SPOTIFY_SEARCH_LIMIT_10,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mListener.onSongsParsed(parseSongsFromQuery(response));
                    }
                });
    }

    /**
     * Constructs and executes a search query with the given parameters.
     * @param query
     * @param searchTypes ex. artists, albums, tracks
     * @param searchLimit number of results to return
     * @param responseListener where to deliver the callback
     */
    private void handleSearchQuery(String query, String searchTypes, String searchLimit, Response.Listener<String> responseListener){
        Log.d(TAG, "User is searching for: " + query);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringBuilder url = new StringBuilder(API_URLS.SPOTIFY_SEARCH)
                .append(API_URLS.SPOTIFY_SEARCH_QUERY)
                .append(query.replaceAll(" ", "+"))
                .append(searchTypes)
                .append(API_URLS.SPOTIFY_SEARCH_MARKET)
                .append(searchLimit);

        Log.d(TAG, "Making request to URL: " + url.toString());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(), responseListener, mErrorListener);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Constructs and executes a artist query with the given parameters.
     * @param artistId Spotify ID.
     * @param artistDetails Market type, albums, etc.
     * @param responseListener where to deliver the callback
     */
    private void handleGetArtistQuery(String artistId, String artistDetails, Response.Listener<String> responseListener){
        Log.d(TAG, "User is searching for: " + artistId);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringBuilder url = new StringBuilder(API_URLS.SPOTIFY_GET_ARTIST)
                .append("/")
                .append(artistId)
                .append(artistDetails);

        Log.d(TAG, "Making request to URL: " + url.toString());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(), responseListener, mErrorListener);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Constructs and executes a album query with the given parameters.
     * @param albumId Spotify ID.
     * @param albumDetails ex. market type
     * @param responseListener where to deliver the callback.
     */
    private void handleGetAlbumQuery(String albumId, String albumDetails, Response.Listener<String> responseListener){
        Log.d(TAG, "User is searching for: " + albumId);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringBuilder url = new StringBuilder(API_URLS.SPOTIFY_GET_ALBUM)
                .append("/")
                .append(albumId)
                .append(albumDetails);

        Log.d(TAG, "Making request to URL: " + url.toString());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(), responseListener, mErrorListener);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Constructs and executes a track query with the given parameters.
     * @param trackId Spotify ID.
     * @param market US, EN, etc.
     * @param responseListener where to deliver the callback.
     */
    private void handleGetSongQuery(String trackId, String market, Response.Listener<String> responseListener){
        Log.d(TAG, "Retrieving track for id: " + trackId);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringBuilder url = new StringBuilder(API_URLS.SPOTIFY_GET_TRACK)
                .append("/")
                .append(trackId)
                .append(market);

        Log.d(TAG, "Making request to URL: " + url.toString());
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.toString(), responseListener, mErrorListener);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Parse results of an artist's query.
     * @param response
     * @return
     */
    private List<SpotifyItem> parseArtistsFromQuery(String response){
        JsonParser parser = new JsonParser();
        JsonObject topObject = parser.parse(response).getAsJsonObject().get("artists").getAsJsonObject();
        JsonArray artistListing = topObject.get("items").getAsJsonArray();
        List<SpotifyItem> artistItems = new ArrayList<>();

        if(topObject.get("total").getAsInt() == 0)
            return null;

        //Parse out each track.
        for(int i = 0; i < artistListing.size(); i++){
            JsonObject artist = artistListing.get(i).getAsJsonObject();
            String artistName = artist.get("name").getAsString();
            String id = artist.get("id").getAsString();

            JsonArray thumbnails = artist.get("images").getAsJsonArray();
            String thumbnail = "";
            if(thumbnails.size() != 0)
                thumbnail = thumbnails.get(0).getAsJsonObject().get("url").getAsString();

            Log.d(TAG, "Artist result " + i + ": " + artistName + " (" + thumbnail + ")");

            artistItems.add(new SpotifyItem(artistName, "", "", thumbnail, id));
        }
        return artistItems;
    }

    /**
     * Parse results of an album's query.
     * @param response
     * @return
     */
    private List<SpotifyItem> parseAlbumsFromQuery(String response){
        JsonParser parser = new JsonParser();
        JsonObject topObject = parser.parse(response).getAsJsonObject();

        if(topObject.has("albums")) //Normal entry point (as opposed to getting an artist's albums)
            topObject = topObject.get("albums").getAsJsonObject();

        JsonArray albumListing = topObject.get("items").getAsJsonArray();
        List<SpotifyItem> albumItems = new ArrayList<>();

        if(topObject.get("total").getAsInt() == 0)
            return null;

        //Parse out each album
        for(int i = 0; i < albumListing.size(); i++){
            JsonObject album = albumListing.get(i).getAsJsonObject();
            String albumName = album.get("name").getAsString();
            String temp = album.get("album_type").getAsString();
            String albumType = temp.substring(0, 1).toUpperCase() + temp.substring(1); //Capitalize album name
            String id = album.get("id").getAsString();

            JsonArray thumbnails = album.get("images").getAsJsonArray();
            String albumArtUrl = "";

            if(thumbnails.size() != 0)
                albumArtUrl = thumbnails.get(0).getAsJsonObject().get("url").getAsString();

            Log.d(TAG, "Album Result " + i + ": " + albumName + ", " + albumType + " (" + albumArtUrl + ")");

            albumItems.add(new SpotifyItem(albumName, albumType, "", albumArtUrl, id));
        }
        return albumItems;
    }

    /**
     * Parse results of an song's query.
     * @param response
     * @return
     */
    private List<SpotifyItem> parseSongsFromQuery(String response){
        JsonParser parser = new JsonParser();
        JsonObject topObject = parser.parse(response).getAsJsonObject();
        JsonArray trackListing;

        if(topObject.has("items")) { //Entry from get album tracks
            trackListing = topObject.get("items").getAsJsonArray();
        }else if(topObject.get("tracks").isJsonArray()){ //Entry from get top artist's songs
            trackListing = topObject.get("tracks").getAsJsonArray();
        }else{
            topObject = topObject.get("tracks").getAsJsonObject();
            trackListing = topObject.get("items").getAsJsonArray();

            if(topObject.get("total").getAsInt() == 0)
                return null;
        }

        List<SpotifyItem> songItems = new ArrayList<>();

        //Parse out the songs
        for(int i = 0; i < trackListing.size(); i++){
            JsonObject track = trackListing.get(i).getAsJsonObject();
            songItems.add(parseSingleSong(track));
        }
        return songItems;
    }

    /**
     * Parse a single track from its JsonObject.
     * @param track
     * @return
     */
    private SpotifyItem parseSingleSong(JsonObject track){
        String songTitle = track.get("name").getAsString();
        String artistName = track.get("artists").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
        String albumName = "";
        JsonArray thumbnails = null;

        if(track.has("album")) { //If the entry point is from getting an album's tracks, we already know the album name (and it is not included in the JSON result)
            albumName = track.get("album").getAsJsonObject().get("name").getAsString();
            thumbnails = track.get("album").getAsJsonObject().get("images").getAsJsonArray();
        }

        String id = track.get("id").getAsString();

        String albumArtUrl = "";

        if(thumbnails != null && thumbnails.size() != 0)
            albumArtUrl = thumbnails.get(0).getAsJsonObject().get("url").getAsString();

        return new SpotifyItem(songTitle, artistName, albumName, albumArtUrl, id);
    }

    private final Response.ErrorListener mErrorListener = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {
            mListener.onQueryFailed(ERROR_QUERY_FAILED);
        }
    };
}
