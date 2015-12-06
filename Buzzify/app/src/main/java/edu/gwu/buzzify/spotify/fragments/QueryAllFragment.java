package edu.gwu.buzzify.spotify.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Given a query, search artists, albums, and tracks and display them in their own lists.
 */
public class QueryAllFragment extends Fragment implements SpotifyQueryListener, ViewHolderClickListener {
    private static final String TAG = QueryAllFragment.class.getName();

    //Indices of the different lists in the mRecyclerViews array
    private static final byte INDEX_ARTISTS = 0;
    private static final byte INDEX_ALBUMS = 1;
    private static final byte INDEX_SONGS = 2;

    /**
     * One wrapper per list <artists, albums, tracks>
     */
    private RecyclerViewWrapper mRecyclerViews[] = new RecyclerViewWrapper[3];

    /**
     * Executes search queries.
     */
    private SpotifyQueryManager mQueryManager;

    /**
     * The query to run.
     */
    private String mQuery;

    /**
     * Don't want to redo the search if onResumes called again if the user leaves/comes back
     */
    private boolean mFirstSearch;

    /**
     * Alert the activity when result items are clicked.
     */
    private SpotifyFragmentListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "QueryAllFragment onCreateView");
        View view = inflater.inflate(R.layout.fragment_spotify_search_all, container, false);

        //Setup variables/views for displaying results in the RecyclerViews
        mRecyclerViews[INDEX_ARTISTS] = new RecyclerViewWrapper((RecyclerView)view.findViewById(R.id.rvArtistSearch), getActivity());
        mRecyclerViews[INDEX_ALBUMS] = new RecyclerViewWrapper((RecyclerView)view.findViewById(R.id.rvAlbumSearch), getActivity());
        mRecyclerViews[INDEX_SONGS] = new RecyclerViewWrapper((RecyclerView)view.findViewById(R.id.rvSongSearch), getActivity());

        mQueryManager = new SpotifyQueryManager(getActivity(), this);
        mQuery = getArguments().getString(BundleKeys.KEY_SEARCH_QUERY);

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
        Log.d(TAG, "Fragment onResume");
        if(mQuery != null && !mFirstSearch) {
            mQueryManager.searchAll(mQuery);
            mFirstSearch = true;
        }
    }

    /**
     * Given a new query, refresh the lists.
     * @param query
     */
    public void newQuery(String query){
        for(RecyclerViewWrapper wrapper : mRecyclerViews){
            wrapper.data.clear();
            wrapper.adapter.notifyDataSetChanged();
            Log.d(TAG, "Test: " + wrapper.data.size());
        }

        mQueryManager.searchAll(query);
    }

    /**
     * Fill the artists' RecyclerView with the results.
     * @param artists Artists matching the search query.
     */
    @Override
    public void onArtistsParsed(List<SpotifyItem> artists) {
        if(artists == null)
            return;

        mRecyclerViews[INDEX_ARTISTS].data.addAll(artists);
        mRecyclerViews[INDEX_ARTISTS].adapter.notifyDataSetChanged();
        Log.d(TAG, "Artists parsed");
    }

    /**
     * Fill the albums' RecyclerView with the results.
     * @param albums Albums matching the search query.
     */
    @Override
    public void onAlbumsParsed(List<SpotifyItem> albums) {
        if(albums == null)
            return;

        mRecyclerViews[INDEX_ALBUMS].data.addAll(albums);
        mRecyclerViews[INDEX_ALBUMS].adapter.notifyDataSetChanged();
        Log.d(TAG, "Albums parsed");
    }

    /**
     * Fill the songs' RecyclerView with the results.
     * @param songs Songs matching the search query.
     */
    @Override
    public void onSongsParsed(List<SpotifyItem> songs) {
        if(songs == null)
            return;

        mRecyclerViews[INDEX_SONGS].data.addAll(songs);
        mRecyclerViews[INDEX_SONGS].adapter.notifyDataSetChanged();
        Log.d(TAG, "Songs parsed");
    }

    @Override
    public void onSingleSongParsed(SpotifyItem song) {

    }

    @Override
    public void onQueryFailed(byte code) {

    }

    /**
     * Alert the activity with whatever type of view was tapped.
     * @param view
     * @param title
     * @param position
     */
    @Override
    public void onClick(View view, String title, int position) {
        View parent = (View)view.getParent();

        if(parent == mRecyclerViews[INDEX_ARTISTS].view){
            mListener.onArtistSelected(mRecyclerViews[INDEX_ARTISTS].data.get(position));
        }else if(parent == mRecyclerViews[INDEX_ALBUMS].view){
            mListener.onAlbumSelected(mRecyclerViews[INDEX_ALBUMS].data.get(position));
        }else if(parent == mRecyclerViews[INDEX_SONGS].view){
            mListener.onSongSelected(mRecyclerViews[INDEX_SONGS].data.get(position));
        }
    }

    /**
     * Wrapper for a RecyclerView, its adapter, and data model list.
     */
    private class RecyclerViewWrapper{
        public RecyclerView view;
        public SpotifyItemAdapter adapter;
        public List<SpotifyItem> data;

        public RecyclerViewWrapper(RecyclerView view, Context context){
            this.view = view;
            this.view.setLayoutManager(new LinearLayoutManager(context));
            this.data = new ArrayList<>();
            adapter = new SpotifyItemAdapter(data, context, QueryAllFragment.this);

            this.view.setAdapter(adapter);
        }
    }
}
