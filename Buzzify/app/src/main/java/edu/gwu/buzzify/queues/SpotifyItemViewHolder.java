package edu.gwu.buzzify.queues;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.gwu.buzzify.R;

public class SpotifyItemViewHolder extends RecyclerView.ViewHolder {
    private View mContainer;
    private ImageView mIvThumb;
    private TextView mTvLine1, mTvLine2, mTvLine3, mTvCount;

    public SpotifyItemViewHolder(View view) {
        super(view);

        mContainer = view;
        mIvThumb = (ImageView)view.findViewById(R.id.ivThumb);
        mTvLine1 = (TextView)view.findViewById(R.id.tvLine1);
        mTvLine2 = (TextView)view.findViewById(R.id.tvLine2);
        mTvLine3 = (TextView)view.findViewById(R.id.tvLine3);
        mTvCount = (TextView)view.findViewById(R.id.tvCount);
    }

    public void setSongTitle(String title){
        mTvLine1.setText(title);
    }

    public void setArtist(String artist){
        mTvLine2.setText(artist);
    }

    public void setAlbum(String album){
        mTvLine3.setText(album);
    }

    public void setAlbumIcon(String url, Context context){
        if(!url.equals(""))
            Picasso.with(context).load(url).into(mIvThumb);
    }

    public void setCount(String count){
        mTvCount.setText(count);
    }

    public View getContainer(){
        return mContainer;
    }
}