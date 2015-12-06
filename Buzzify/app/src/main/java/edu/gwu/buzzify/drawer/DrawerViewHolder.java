package edu.gwu.buzzify.drawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.gwu.buzzify.R;

/**
 * Holds views for either the header list item or a menu list item
 */
public class DrawerViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = DrawerViewHolder.class.getName();

    //View types, determines the content of this ViewHolder
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    /**
     * Either TYPE_HEADER or TYPE_ITEM
     */
    private int mType;

    private View mContainer;
    //Menu item views
    private ImageView mIvIcon;
    private TextView mTvText;

    //Header views
    private TextView mTvUsername;
    private TextView mTvEmail;
    private ImageView mIvCircle;

    /**
     * OnClick listener for menu items
     */
    private DrawerViewHolderClickListener mListener;

    /**
     * Given the item's view and type, save references to the correct widgets.
     * @param view
     * @param type
     */
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

    public void setProfilePic(String profilePicUrl, Context context){
        Log.d(TAG, "Using URL: " + profilePicUrl);
        Picasso.with(context).load(profilePicUrl).into(mIvCircle);
    }

    public void setListener(DrawerViewHolderClickListener listener){
        mListener = listener;
    }

    public int getType(){
        return mType;
    }
}
