package edu.gwu.buzzify.drawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.gwu.buzzify.R;

/**
 * Adapter for DrawerItem objects to be displayed in the drawer's RecyclerView
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerViewHolder> {
    /**
     * Objects to be displayed as RecyclerView items.
     */
    private List<DrawerItem> mDrawerItems;

    /**
     * Reference to the header item for changing username/email/pic
     */
    private DrawerViewHolder mHeader;

    //Information for the header.
    private String mUsername, mEmail, mProfilePicUrl;

    /**
     * Receives callbacks when menu items are clicked.
     */
    private DrawerViewHolderClickListener mListener;

    private Context mContext;

    /**
     * Sole constructor. Takes in the list of objects to display and information for the header view.
     * @param context
     * @param list DrawerItem objects to be displayed.
     * @param username
     * @param email
     * @param profilePic
     * @param listener
     */
    public DrawerAdapter(Context context, List<DrawerItem> list, String username, String email, String profilePic, DrawerViewHolderClickListener listener){
        mDrawerItems = list;
        mUsername = username;
        mEmail = email;
        mProfilePicUrl = profilePic;
        mContext = context;
        mListener = listener;
    }

    /**
     * Depending on the view type (header or not), inflate the correct layout.
     * @param parent
     * @param viewType
     * @return
     */
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

    /**
     * Depending on the viewtype, fill the list item with the correct information.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        if(holder.getType() == DrawerViewHolder.TYPE_HEADER){
            holder.setUsername(mUsername);
            holder.setEmail(mEmail);
            holder.setProfilePic(mProfilePicUrl, mContext);
        }else{
            //Position - 1 because the first element in the list is the header
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

    /**
     * First list item will be the header.
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return DrawerViewHolder.TYPE_HEADER;

        return DrawerViewHolder.TYPE_ITEM;
    }

    /**
     * Used to change the full name in the header (ex. if the user goes to the EditProfile activity).
     * @param fullname
     */
    public void setHeaderFullname(String fullname){
        mUsername = fullname;

        if(mHeader != null)
            mHeader.setUsername(mUsername);
        notifyDataSetChanged();
    }

    /**
     * Used to change the email in the header (ex. if the user goes to the EditProfile activity).
     * @param email
     */
    public void setHeaderEmail(String email){
        mEmail = email;

        if(mHeader != null)
            mHeader.setEmail(mEmail);
        notifyDataSetChanged();
    }

    /**
     * Used to change the profile pic in the header (ex. if the user goes to the EditProfile activity.
     * @param picUrl
     */
    public void setProfilePicUrl(String picUrl){
        mProfilePicUrl = picUrl;

        if(mHeader != null)
            mHeader.setProfilePic(mProfilePicUrl, mContext);
        notifyDataSetChanged();
    }
}
