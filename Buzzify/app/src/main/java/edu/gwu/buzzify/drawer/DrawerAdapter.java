package edu.gwu.buzzify.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.gwu.buzzify.R;

/**
 * Created by Nick on 11/25/2015.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerViewHolder> {
    private List<DrawerItem> mDrawerItems;
    private String mUsername, mEmail;
    private int mProfilePicId;
    private Bitmap mProfilePic;
    private Context mContext;
    private DrawerViewHolderClickListener mListener;

    public DrawerAdapter(Context context, List<DrawerItem> list, String username, String email, Bitmap profilePic, DrawerViewHolderClickListener listener){
        mDrawerItems = list;
        mUsername = username;
        mEmail = email;
        mProfilePic = profilePic;
        mContext = context;
        mListener = listener;
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == DrawerViewHolder.TYPE_HEADER){
            View header = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_drawer_header, parent, false);
            return new DrawerViewHolder(header, DrawerViewHolder.TYPE_HEADER);
        }else{
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_drawer_item, parent, false);
            return new DrawerViewHolder(item, DrawerViewHolder.TYPE_ITEM);
        }
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        if(holder.getType() == DrawerViewHolder.TYPE_HEADER){
            holder.setUsername(mUsername);
            holder.setEmail(mEmail);
            holder.setProfilePic(mProfilePic);
        }else{
            holder.setRowText(mDrawerItems.get(position - 1).text);

            Drawable icon = mContext.getResources().getDrawable(mDrawerItems.get(position - 1).iconId);
            holder.setRowIcon(icon);

            holder.setListener(mListener);
        }
    }

    @Override
    public int getItemCount() {
        return mDrawerItems.size()+1; //Include header view
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return DrawerViewHolder.TYPE_HEADER;

        return DrawerViewHolder.TYPE_ITEM;
    }

}
