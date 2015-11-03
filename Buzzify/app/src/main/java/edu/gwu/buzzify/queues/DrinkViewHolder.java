package edu.gwu.buzzify.queues;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.gwu.buzzify.R;

/**
 * Created by cheng on 11/2/15.
 */
public class DrinkViewHolder extends RecyclerView.ViewHolder{
    private View mContainer;
    private ImageView mIvThumb;
    private TextView mTvDrinkName;

    public DrinkViewHolder(View view) {
        super(view);
        mContainer = view;
        mIvThumb = (ImageView)view.findViewById(R.id.ivDrinkThumb);
        mTvDrinkName = (TextView)view.findViewById(R.id.tvDrinkName);
    }

    public void setDrinkName(String name){
        mTvDrinkName.setText(name);
    }

    public void setDrinkIcon(String url){
        //TODO Picasso
    }
}
