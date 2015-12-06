package edu.gwu.buzzify.drinks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.gwu.buzzify.R;
import edu.gwu.buzzify.ViewHolderClickListener;

/**
 * Contains the views that represent a DrinkInfo item in a RecyclerView.
 */
public class DrinkViewHolder extends RecyclerView.ViewHolder{
    private View mContainer;
    private ImageView mIvThumb;
    private TextView mTvDrinkName, mTvCustomerName;
    private ViewHolderClickListener mListener;

    private int mPosition;

    public DrinkViewHolder(View view) {
        super(view);
        mContainer = view;
        mIvThumb = (ImageView)view.findViewById(R.id.ivDrinkThumb);
        mTvDrinkName = (TextView)view.findViewById(R.id.tvDrinkName);
        mTvCustomerName = (TextView)view.findViewById(R.id.tvCustomerName);

        mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onClick(mContainer, mTvDrinkName.getText().toString(), mPosition);
            }
        });
    }

    public void setDrinkName(String name){
        mTvDrinkName.setText(name);
    }

    public void setCustomerName(String name){
        mTvCustomerName.setText(name);
    }

    public void setDrinkIcon(String url, Context context){
        if(url == null || url.equals(""))
            return;
        Picasso.with(context).load(url).into(mIvThumb);
    }

    public void setPosition(int position){
        mPosition = position;
    }

    public void setListener(ViewHolderClickListener listener){
        mListener = listener;
    }
}

