package edu.gwu.buzzify.spotify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.gwu.buzzify.R;
import edu.gwu.buzzify.ViewHolderClickListener;

/**
 * Contains the views that represent a SpotifyItem in a RecyclerView.
 */
public class SpotifyItemViewHolder extends RecyclerView.ViewHolder {
    private View mContainer;
    private ImageView mIvThumb;
    private TextView mTvLine1, mTvLine2, mTvLine3, mTvCount;
    private ViewHolderClickListener mListener;

    private int mPosition;

    public SpotifyItemViewHolder(View view) {
        super(view);

        mContainer = view;
        mIvThumb = (ImageView)view.findViewById(R.id.ivThumb);
        mTvLine1 = (TextView)view.findViewById(R.id.tvLine1);
        mTvLine2 = (TextView)view.findViewById(R.id.tvLine2);
        mTvLine3 = (TextView)view.findViewById(R.id.tvLine3);
        mTvCount = (TextView)view.findViewById(R.id.tvCount);

        mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onClick(mContainer, mTvLine1.getText().toString(), mPosition);
            }
        });
    }

    public void setLine1(String line1){
        mTvLine1.setText(line1);
    }

    public void setLine2(String line2){
        mTvLine2.setText(line2);
    }

    public void setLine3(String line3){
        mTvLine3.setText(line3);
    }

    public void setIcon(String url, Context context){
        if(url != null && !url.equals(""))
            Picasso.with(context).load(url).into(mIvThumb);
    }

    public void setCount(String count) {
        if (count.equals("0")){ //Don't display if the count is 0
            mTvCount.setText("");
            return;
        }
        mTvCount.setText(count);
    }

    public View getContainer(){
        return mContainer;
    }

    public void setListener(ViewHolderClickListener listener){
        mListener = listener;
    }

    public void setPosition(int position){
        mPosition = position;
    }
}
