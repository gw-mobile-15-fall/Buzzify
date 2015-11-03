package edu.gwu.buzzify.queues;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.gwu.buzzify.R;

/**
 * Created by cheng on 11/2/15.
 */
public class SongViewHolder extends RecyclerView.ViewHolder {
    private View mContainer;
    private ImageView mIvUpArrow, mIvDownArrow, mIvAlbumThumb;
    private TextView mTvSong, mTvArtist, mTvAlbum;

    public SongViewHolder(View view) {
        super(view);

        mContainer = view;
        mIvUpArrow = (ImageView)view.findViewById(R.id.ivVoteUp);
        mIvDownArrow = (ImageView)view.findViewById(R.id.ivVoteDown);
        mIvAlbumThumb = (ImageView)view.findViewById(R.id.ivAlbumThumb);
        mTvSong = (TextView)view.findViewById(R.id.tvSong);
        mTvArtist = (TextView)view.findViewById(R.id.tvArtist);
        mTvAlbum = (TextView)view.findViewById(R.id.tvAlbum);
    }

    public void setSongTitle(String title){
        mTvSong.setText(title);
    }

    public void setArtist(String artist){
        mTvArtist.setText(artist);
    }

    public void setAlbum(String album){
        mTvAlbum.setText(album);
    }

    public void setAlbumIcon(String url){
        //TODO Picasso
    }
}