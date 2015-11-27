package edu.gwu.buzzify.drawer;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.gwu.buzzify.R;

/**
 * Created by Nick on 11/25/2015.
 */
public class DrawerViewHolder extends RecyclerView.ViewHolder {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private int mType;

    private View mContainer;
    private ImageView mIvIcon;
    private TextView mTvText;

    private TextView mTvUsername;
    private TextView mTvEmail;
    private ImageView mIvCircle;

    private DrawerViewHolderClickListener mListener;

    public DrawerViewHolder(View view, int type) {
        super(view);
        mContainer = view;

        if(type == TYPE_HEADER) {
            mTvUsername = (TextView) view.findViewById(R.id.drawerUsername);
            mTvEmail = (TextView) view.findViewById(R.id.drawerEmail);
            mIvCircle = (ImageView) view.findViewById(R.id.drawerCircleView);
        }else{
            mIvIcon = (ImageView) view.findViewById(R.id.drawerRowIcon);
            mTvText = (TextView) view.findViewById(R.id.drawerRowText);
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null)
                        mListener.onClick(mTvText.getText().toString());
                }
            });
        }

        mType = type;
    }

    public void setRowText(String text){
        mTvText.setText(text);
    }

    public void setRowIcon(Drawable icon){
        mIvIcon.setImageDrawable(icon);
    }

    public void setUsername(String username){
        mTvUsername.setText(username);
    }

    public void setEmail(String email){
        mTvEmail.setText(email);
    }

    public void setProfilePic(int id){
        //TODO Picasso
    }

    public void setListener(DrawerViewHolderClickListener listener){
        mListener = listener;
    }

    public int getType(){
        return mType;
    }
}
