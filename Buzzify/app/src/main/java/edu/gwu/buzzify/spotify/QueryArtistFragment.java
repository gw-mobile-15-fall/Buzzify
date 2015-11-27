package edu.gwu.buzzify.spotify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.gwu.buzzify.R;
import edu.gwu.buzzify.models.SpotifyItem;
import edu.gwu.buzzify.models.SpotifyItemAdapter;
import edu.gwu.buzzify.models.SpotifyItemViewHolderClickListener;

/**
 * Created by Nick on 11/26/2015.
 */
public class QueryArtistFragment extends Fragment implements SpotifyQueryListener, SpotifyItemViewHolderClickListener {
    public static final String KEY_ARTIST_ID = "artistId";
    public static final String KEY_ARTIST_NAME = "artistName";

    private static final String TAG = QueryArtistFragment.class.getName();

    private String mArtistId;
    private SpotifyQueryManager mQueryManager;

    private RecyclerView mRvArtistAlbums;
    private SpotifyItemAdapter mArtistAlbumsAdapter;
    private List<SpotifyItem> mArtistAlbumsInfo;

    private RecyclerView mRvTopSongs;
    private SpotifyItemAdapter mTopSongsAdapter;
    private List<SpotifyItem> mTopSongsInfo;

    private boolean mFirstSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spotify_search_artist, container, false);

        mRvArtistAlbums = (RecyclerView)view.findViewById(R.id.rvArtistAlbums);
        mRvArtistAlbums.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArtistAlbumsInfo = new ArrayList<>();
        mArtistAlbumsAdapter = new SpotifyItemAdapter(mArtistAlbumsInfo, getActivity(), this);
        mRvArtistAlbums.setAdapter(mArtistAlbumsAdapter);

        mRvTopSongs = (RecyclerView)view.findViewById(R.id.rvArtistTopSongs);
        mRvTopSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTopSongsInfo = new ArrayList<>();
        mTopSongsAdapter = new SpotifyItemAdapter(mTopSongsInfo, getActivity(), this);
        mRvTopSongs.setAdapter(mTopSongsAdapter);

        mQueryManager = new SpotifyQueryManager(getActivity(), this);
        mArtistId = getArguments().getString(KEY_ARTIST_ID);

        String artistName = getArguments().getString(KEY_ARTIST_NAME);

        ((TextView)view.findViewById(R.id.tvLblAlbums)).setText(artistName + " - " + "Albums");
        ((TextView)view.findViewById(R.id.tvLblTopSongs)).setText(artistName + " - " + "Top Songs");
        return view;
    }

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

    @Override
    public void onAlbumsParsed(List<SpotifyItem> albums) {
        if(albums == null)
            return;
        mArtistAlbumsInfo.addAll(albums);
        mArtistAlbumsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSongsParsed(List<SpotifyItem> songs) {
        if(songs == null)
            return;
        mTopSongsInfo.addAll(songs);
        mTopSongsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryFailed(byte code) {

    }

    @Override
    public void onClick(View view, String title, int position) {
        View parent = (View)view.getParent();

        if(parent == mRvArtistAlbums){
            Log.d(TAG, "Album " + title + " clicked, position = " + position);
        }else if(parent == mRvTopSongs){
            Log.d(TAG, "Song " + title + " clicked, position = " + position);
        }
    }
}
