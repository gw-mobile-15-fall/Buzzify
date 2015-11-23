package edu.gwu.buzzify.queues;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import java.util.List;

import edu.gwu.buzzify.R;


public class SongQueueAdapter extends RecyclerView.Adapter<SongViewHolder> {
    private static final String TAG = SongQueueAdapter.class.getName();
    private List<SongInfo> mSongInfos;
    private Context mContext;

    private int mLastAnimated = -1;

    public SongQueueAdapter(List<SongInfo> songs, Context context){
        mSongInfos = songs;
        mContext = context;
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
        holder.setAlbumIcon(info.albumArtUrl, mContext);

        if(position > mLastAnimated) {
            holder.getContainer().startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
            mLastAnimated = position;
        }
    }

    @Override
    public int getItemCount(){
        return mSongInfos.size();
    }

    public void resetAnimationCount(){
        mLastAnimated = -1;
    }
}
