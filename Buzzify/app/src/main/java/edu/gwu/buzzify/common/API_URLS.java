package edu.gwu.buzzify.common;

/**
 * Created by cheng on 11/23/15.
 */
public class API_URLS {
    public static final String SPOTIFY_GET_ARTIST = "https://api.spotify.com/v1/artists";
    public static final String SPOTIFY_GET_ARTIST_ALBUMS = "/albums?market=US";
    public static final String SPOTIFY_GET_ARTIST_TOP_TRACKS = "/top-tracks?country=US";

    public static final String SPOTIFY_GET_ALBUM = "https://api.spotify.com/v1/albums";
    public static final String SPOTIFY_GET_ALBUM_TRACKS = "/tracks?market=US";

    public static final String SPOTIFY_SEARCH = "https://api.spotify.com/v1/search";
    public static final String SPOTIFY_SEARCH_QUERY = "?q=";
    public static final String SPOTIFY_SEARCH_TYPE_TRACK = "&type=track";
    public static final String SPOTIFY_SEARCH_TYPE_ARTIST = "&type=artist";
    public static final String SPOTIFY_SEARCH_TYPE_ALBUM = "&type=album";
    public static final String SPOTIFY_SEARCH_TYPE_ARTISTSALBUM = "&type=artist,album";
    public static final String SPOTIFY_SEARCH_MARKET = "&market=US";
    public static final String SPOTIFY_SEARCH_LIMIT_2 = "&limit=2";
    public static final String SPOTIFY_SEARCH_LIMIT_10 = "&limit=10";
}
