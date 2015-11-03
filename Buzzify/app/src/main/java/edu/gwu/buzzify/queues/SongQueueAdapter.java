package edu.gwu.buzzify.queues;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.gwu.buzzify.R;


public class SongQueueAdapter extends RecyclerView.Adapter<SongViewHolder> {
    private static final String TAG = SongQueueAdapter.class.getName();
    private List<SongInfo> mSongInfos;

    public SongQueueAdapter(List<SongInfo> songs){
        mSongInfos = songs;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_song_queue_item, parent, false);
        return new SongViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position){
        SongInfo info = mSongInfos.get(position);
        holder.setSongTitle(info.title);
        holder.setArtist(info.artist);
        holder.setAlbum(info.album);
        holder.setAlbumIcon(info.albumArtUrl);
    }

    @Override
    public int getItemCount(){
        return mSongInfos.size();
    }
}
