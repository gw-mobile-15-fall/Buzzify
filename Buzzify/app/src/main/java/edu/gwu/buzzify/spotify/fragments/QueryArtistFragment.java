package edu.gwu.buzzify.spotify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gwu.buzzify.R;
import edu.gwu.buzzify.common.BundleKeys;
import edu.gwu.buzzify.spotify.SpotifyItem;
import edu.gwu.buzzify.spotify.SpotifyItemAdapter;
import edu.gwu.buzzify.ViewHolderClickListener;
import edu.gwu.buzzify.spotify.SpotifyQueryListener;
import edu.gwu.buzzify.spotify.SpotifyQueryManager;

/**
 * Retrieves information about a given artist and displays the artist's albums and top songs in a RecyclerView.
 */
public class QueryArtistFragment extends Fragment implements SpotifyQueryListener, ViewHolderClickListener {
    private static final String TAG = QueryArtistFragment.class.getName();

    /**
     * Artists's Spotify ID to search for.
     */
    private String mArtistId;

    /**
     * Executes querys to Spotify.
     */
    private SpotifyQueryManager mQueryManager;

    //Artist's album list variables
    private RecyclerView mRvArtistAlbums;
    private SpotifyItemAdapter mArtistAlbumsAdapter;
    private List<SpotifyItem> mArtistAlbumsInfo;

    //Artist's top songs list variables
    private RecyclerView mRvTopSongs;
    private SpotifyItemAdapter mTopSongsAdapter;
    private List<SpotifyItem> mTopSongsInfo;

    /**
     * Alert the activity when result items are clicked.
     */
    private SpotifyFragmentListener mListener;

    /**
     * Don't want to redo the search if onResumes called again if the user leaves/comes back
     */
    private boolean mFirstSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spotify_search_artist, container, false);

        //Setup variables/views for displaying results in the albums RecyclerView
        mRvArtistAlbums = (RecyclerView)view.findViewById(R.id.rvArtistAlbums);
        mRvArtistAlbums.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArtistAlbumsInfo = new ArrayList<>();
        mArtistAlbumsAdapter = new SpotifyItemAdapter(mArtistAlbumsInfo, getActivity(), this);
        mRvArtistAlbums.setAdapter(mArtistAlbumsAdapter);

        //Setup variables/views for displaying results in the songs RecyclerView
        mRvTopSongs = (RecyclerView)view.findViewById(R.id.rvArtistTopSongs);
        mRvTopSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTopSongsInfo = new ArrayList<>();
        mTopSongsAdapter = new SpotifyItemAdapter(mTopSongsInfo, getActivity(), this);
        mRvTopSongs.setAdapter(mTopSongsAdapter);

        mQueryManager = new SpotifyQueryManager(getActivity(), this);
        mArtistId = getArguments().getString(BundleKeys.KEY_ARTIST_ID);

        String artistName = getArguments().getString(BundleKeys.KEY_ARTIST_NAME);

        //Set the title to reflect the current artist being searched
        ((TextView)view.findViewById(R.id.tvLblAlbums)).setText(artistName + " - " + "Albums");
        ((TextView)view.findViewById(R.id.tvLblTopSongs)).setText(artistName + " - " + "Top Songs");

        mListener = (SpotifyFragmentListener)getActivity();
        mFirstSearch = false;
        return view;
    }

    /**
     * Execute the search query.
     */
    @Override
    public void onResume(){
        super.onResume();
        if(mArtistId != null && !mFirstSearch) {
            mQueryManager.searchArtistById(mArtistId);
            mFirstSearch = true;
        }
    }

    @Override
    public void onArtistsParsed(List<SpotifyItem> artists) {}

    /**
     * Display the artist's albums in the list.
     * @param albums Artist's albums.
     */
    @Override
    public void onAlbumsParsed(List<SpotifyItem> albums) {
        if(albums == null)
            return;
        mArtistAlbumsInfo.addAll(albums);
        mArtistAlbumsAdapter.notifyDataSetChanged();
    }

    /**
     * Display the artist's top songs in the list.
     * @param songs Artist's top songs.
     */
    @Override
    public void onSongsParsed(List<SpotifyItem> songs) {
        if(songs == null)
            return;
        mTopSongsInfo.addAll(songs);
        mTopSongsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSingleSongParsed(SpotifyItem song) {

    }

    @Override
    public void onQueryFailed(byte code) {

    }

    /**
     * When a song result is pressed, deliver the information to the activity.
     * @param view
     * @param title
     * @param position
     */
    @Override
    public void onClick(View view, String title, int position) {
        View parent = (View)view.getParent();

        if(parent == mRvArtistAlbums){
            mListener.onAlbumSelected(mArtistAlbumsInfo.get(position));
        }else if(parent == mRvTopSongs){
            mListener.onSongSelected(mTopSongsInfo.get(position));
        }
    }
}
