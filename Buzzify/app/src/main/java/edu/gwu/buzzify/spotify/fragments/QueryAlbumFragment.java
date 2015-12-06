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
import edu.gwu.buzzify.ViewHolderClickListener;
import edu.gwu.buzzify.common.BundleKeys;
import edu.gwu.buzzify.spotify.SpotifyItem;
import edu.gwu.buzzify.spotify.SpotifyItemAdapter;
import edu.gwu.buzzify.spotify.SpotifyQueryListener;
import edu.gwu.buzzify.spotify.SpotifyQueryManager;

/**
 * Retrieves information about a given album and displays the album's songs in a RecyclerView.
 */
public class QueryAlbumFragment extends Fragment implements SpotifyQueryListener, ViewHolderClickListener {
    private static final String TAG = QueryAlbumFragment.class.getName();

    /**
     * Album's Spotify ID to search for.
     */
    private String mAlbumId;

    /**
     * Executes querys to Spotify.
     */
    private SpotifyQueryManager mQueryManager;

    /**
     * Displays results in a list.
     */
    private RecyclerView mRvAlbumSongs;

    /**
     * Adapts SpotifyItem search results into the RecyclerView.
     */
    private SpotifyItemAdapter mAlbumSongsAdapter;

    /**
     * Model objects to be displayed.
     */
    private List<SpotifyItem> mAlbumSongsInfo;

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
        View view = inflater.inflate(R.layout.fragment_spotify_search_album, container, false);

        mQueryManager = new SpotifyQueryManager(getActivity(), this);
        mAlbumId = getArguments().getString(BundleKeys.KEY_ALBUM_ID);

        //Setup variables/views for displaying results in a RecyclerView
        mRvAlbumSongs = (RecyclerView)view.findViewById(R.id.rvAlbumSongs);
        mRvAlbumSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAlbumSongsInfo = new ArrayList<>();
        mAlbumSongsAdapter = new SpotifyItemAdapter(mAlbumSongsInfo, getActivity(), this);
        mRvAlbumSongs.setAdapter(mAlbumSongsAdapter);

        String albumName = getArguments().getString(BundleKeys.KEY_ALBUM_NAME);

        //Set the title to reflect the current album being searched
        ((TextView)view.findViewById(R.id.tvLblAlbumSongs)).setText(albumName + " - Songs");

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
        if(mAlbumId != null && !mFirstSearch) {
            mQueryManager.searchAlbumById(mAlbumId);
            mFirstSearch = true;
        }
    }

    /**
     * When a song result is pressed, go get its detailed results so we can return it to the MainActivity.
     * @param view
     * @param title
     * @param position
     */
    @Override
    public void onClick(View view, String title, int position) {
        View parent = (View)view.getParent();

        if(parent == mRvAlbumSongs)
            mQueryManager.searchSongById(mAlbumSongsInfo.get(position).getId());

    }

    @Override
    public void onArtistsParsed(List<SpotifyItem> artists) {}

    @Override
    public void onAlbumsParsed(List<SpotifyItem> albums) {}

    /**
     * When the tracks on an album have been found, display them in the list.
     * @param songs
     */
    @Override
    public void onSongsParsed(List<SpotifyItem> songs) {
        if(songs == null)
            return;

        mAlbumSongsInfo.addAll(songs);
        mAlbumSongsAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the single song the user taps has been queried and retrieved. Notifies the listener.
     * @param song
     */
    @Override
    public void onSingleSongParsed(SpotifyItem song) {
        if(song == null)
            return;

        mListener.onSongSelected(song);
    }

    @Override
    public void onQueryFailed(byte code) {

    }
}
