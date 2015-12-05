package edu.gwu.buzzify.drawer;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import edu.gwu.buzzify.EditProfileActivity;
import edu.gwu.buzzify.LocationActivity;
import edu.gwu.buzzify.LoginActivity;
import edu.gwu.buzzify.MainActivity;
import edu.gwu.buzzify.R;

/**
 * Created by Nick on 11/26/2015.
 */
public class NavDrawer implements DrawerViewHolderClickListener {
    //TODO string constants
    private static final String MAIN_MENU_TEXT = "Main Menu";
    private static final String LOCATION_TEXT = "Location";
    private static final String SEARCH_SONGS_TEXT = "Search Songs";
    private static final String ORDER_DRINKS_TEXT = "Order Drinks";
    private static final String EDIT_PROFILE_TEXT = "Edit Profile";
    private static final String LOGOUT_TEXT = "Logout";

    private static final String TAG = NavDrawer.class.getName();
    private static final String[] ROW_ITEMS = {MAIN_MENU_TEXT, LOCATION_TEXT, EDIT_PROFILE_TEXT, LOGOUT_TEXT};
    private static final int[] ICON_IDS = {android.R.drawable.ic_menu_myplaces, android.R.drawable.ic_menu_compass,
            android.R.drawable.ic_menu_edit, android.R.drawable.ic_menu_close_clear_cancel};

    private RecyclerView mRecyclerView;
    private DrawerAdapter mAdapter;
    private List<DrawerItem> mDrawerItems;

    private DrawerLayout mDrawer;
    private Activity mActivity;
    private LogOutUser mLogOutUser;

    public interface LogOutUser {
        void logOutUser();
    }

    public NavDrawer(Activity activity, Toolbar toolbar, String username, String email, String profileIconUrl){
        mActivity = activity;
        mRecyclerView = (RecyclerView) activity.findViewById(R.id.navDrawer);
        mRecyclerView.setHasFixedSize(true);

        mDrawerItems = new ArrayList<>();
        for(int i = 0; i < ROW_ITEMS.length; i++)
            mDrawerItems.add(new DrawerItem(ROW_ITEMS[i], ICON_IDS[i]));

        mAdapter = new DrawerAdapter(activity, mDrawerItems, username, email, profileIconUrl, this);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

        mRecyclerView.setLayoutManager(layoutManager);

        mDrawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle mListener = new ActionBarDrawerToggle(activity, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close){

        };


        mDrawer.setDrawerListener(mListener);
        mListener.syncState();
    }

    @Override
    public void onClick(String itemText) {
        Log.d(TAG, itemText + " clicked");
        if(itemText.equals(MAIN_MENU_TEXT)){
            if(mActivity instanceof MainActivity)
                return;

            mActivity.startActivity(new Intent(mActivity, MainActivity.class));
        }else if(itemText.equals(LOCATION_TEXT)){
            if(mActivity instanceof LocationActivity)
                return;

            //TODO maybe clear back stack here
            Intent intent = new Intent(mActivity, LocationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        /*
        }else if(itemText.equals(SEARCH_SONGS_TEXT)){
            if(mActivity instanceof SpotifySearchActivity)
                return;

            mActivity.startActivity(new Intent(mActivity, SpotifySearchActivity.class));
        }else if(itemText.equals(ORDER_DRINKS_TEXT)){
            if(mActivity instanceof MainActivity)
                return;
            //TODO
            //mActivity.startActivity(new Intent(mActivity, MainActivity.class));\
        */
        }else if(itemText.equals(EDIT_PROFILE_TEXT)){
            if(mActivity instanceof EditProfileActivity)
                return;

            mActivity.startActivity(new Intent(mActivity, EditProfileActivity.class));
        }else if(itemText.equals(LOGOUT_TEXT)){
            ParseUser.logOut();

            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        }
    }

    public DrawerLayout getDrawerLayout(){
        return mDrawer;
    }
}
