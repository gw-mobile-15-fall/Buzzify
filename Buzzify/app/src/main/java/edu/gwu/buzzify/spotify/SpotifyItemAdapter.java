package edu.gwu.buzzify.spotify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import java.util.List;

import edu.gwu.buzzify.R;
import edu.gwu.buzzify.ViewHolderClickListener;


public class SpotifyItemAdapter extends RecyclerView.Adapter<SpotifyItemViewHolder> {
    private static final String TAG = SpotifyItemAdapter.class.getName();
    private List<SpotifyItem> mSpotifyItems;
    private Context mContext;
    private ViewHolderClickListener mListener;

    private int mLastAnimated = -1;

    public SpotifyItemAdapter(List<SpotifyItem> items, Context context, ViewHolderClickListener listener){
        mSpotifyItems = items;
        mContext = context;
        mListener = listener;
    }

    @Override
    public SpotifyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_spotify_item, parent, false);
        return new SpotifyItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SpotifyItemViewHolder holder, int position){
        SpotifyItem info = mSpotifyItems.get(position);
        holder.setSongTitle(info.getLine1());
        holder.setArtist(info.getLine2());
        holder.setAlbum(info.getLine3());
        holder.setAlbumIcon(info.getThumbnailUrl(), mContext);
        holder.setCount("" + info.getCount());
        holder.setListener(mListener);
        holder.setPosition(position);

        if(position > mLastAnimated) {
            holder.getContainer().startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in));
            mLastAnimated = position;
        }
    }

    @Override
    public int getItemCount(){
        return mSpotifyItems.size();
    }

    public void resetAnimationCount(){
        mLastAnimated = -1;
    }
}
