package edu.gwu.buzzify.common;

/**
 * Spotify URLs and parameter sfor obtaining different types of data.
 */
public class API_URLS {
    //Getting details about a specific artist
    public static final String SPOTIFY_GET_ARTIST = "https://api.spotify.com/v1/artists";
    public static final String SPOTIFY_GET_ARTIST_ALBUMS = "/albums?market=US";
    public static final String SPOTIFY_GET_ARTIST_TOP_TRACKS = "/top-tracks?country=US";

    //Getting details about a specific album
    public static final String SPOTIFY_GET_ALBUM = "https://api.spotify.com/v1/albums";
    public static final String SPOTIFY_GET_ALBUM_TRACKS = "/tracks?market=US";

    //Getting details about a specific track
    public static final String SPOTIFY_GET_TRACK = "https://api.spotify.com/v1/tracks";
    public static final String SPOTIFY_GET_TRACK_MARKET = "?market=US";

    //Searching for a track, artist, or album
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
