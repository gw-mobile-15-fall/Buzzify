package edu.gwu.buzzify.spotify;

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
import edu.gwu.buzzify.models.SpotifyItem;
import edu.gwu.buzzify.models.SpotifyItemAdapter;
import edu.gwu.buzzify.models.SpotifyItemViewHolderClickListener;

/**
 * Created by Nick on 11/26/2015.
 */
public class QueryAlbumFragment extends Fragment implements SpotifyQueryListener, SpotifyItemViewHolderClickListener {
    public static final String KEY_ALBUM_ID = "albumId";
    public static final String KEY_ALBUM_NAME = "albumName";
    public static final String KEY_ARTIST_NAME = "artistName";

    private static final String TAG = QueryAlbumFragment.class.getName();

    private String mAlbumId;
    private SpotifyQueryManager mQueryManager;

    private RecyclerView mRvAlbumSongs;
    private SpotifyItemAdapter mAlbumSongsAdapter;
    private List<SpotifyItem> mAlbumSongsInfo;

    private boolean mFirstSearch;

    private SpotifyFragmentListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spotify_search_album, container, false);

        mQueryManager = new SpotifyQueryManager(getActivity(), this);
        mAlbumId = getArguments().getString(KEY_ALBUM_ID);

        mRvAlbumSongs = (RecyclerView)view.findViewById(R.id.rvAlbumSongs);
        mRvAlbumSongs.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAlbumSongsInfo = new ArrayList<>();
        mAlbumSongsAdapter = new SpotifyItemAdapter(mAlbumSongsInfo, getActivity(), this);
        mRvAlbumSongs.setAdapter(mAlbumSongsAdapter);

        String albumName = getArguments().getString(KEY_ALBUM_NAME);

        //TODO string constants
        ((TextView)view.findViewById(R.id.tvLblAlbumSongs)).setText(albumName + " - Songs");

        mListener = (SpotifyFragmentListener)getActivity();
        mFirstSearch = false;
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mAlbumId != null && !mFirstSearch) {
            mQueryManager.searchAlbumById(mAlbumId);
            mFirstSearch = true;
        }
    }


    @Override
    public void onClick(View view, String title, int position) {
        View parent = (View)view.getParent();

        if(parent == mRvAlbumSongs){
            //TODO need to re-add album name and icon
            mListener.onSongSelected(mAlbumSongsInfo.get(position));
        }
    }

    @Override
    public void onArtistsParsed(List<SpotifyItem> artists) {}

    @Override
    public void onAlbumsParsed(List<SpotifyItem> albums) {}

    @Override
    public void onSongsParsed(List<SpotifyItem> songs) {
        if(songs == null)
            return;

        mAlbumSongsInfo.addAll(songs);
        mAlbumSongsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryFailed(byte code) {

    }
}
