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
 * Creates a Material Design navigation drawer from an Activity which has a
 * DrawerLayout containing a RecyclerView with id = navDrawer.
 *
 * Also handles click events within the drawer.
 */
public class NavDrawer implements DrawerViewHolderClickListener {
    //Drawer item titles
    private static final String MAIN_MENU_TEXT = "Main Menu";
    private static final String LOCATION_TEXT = "Location";
    private static final String SEARCH_SONGS_TEXT = "Search Songs";
    private static final String ORDER_DRINKS_TEXT = "Order Drinks";
    private static final String EDIT_PROFILE_TEXT = "Edit Profile";
    private static final String LOGOUT_TEXT = "Logout";

    private static final String TAG = NavDrawer.class.getName();

    /**
     * Array of drawer item titles. Should match a corresponding icon in ICON_IDS/
     */
    private static final String[] ROW_ITEMS = {MAIN_MENU_TEXT, LOCATION_TEXT, EDIT_PROFILE_TEXT, LOGOUT_TEXT};

    /**
     * Array of icons for the drawer item titles in ROW_ITEMS.
     */
    private static final int[] ICON_IDS = {android.R.drawable.ic_menu_myplaces, android.R.drawable.ic_menu_compass,
            android.R.drawable.ic_menu_edit, android.R.drawable.ic_menu_close_clear_cancel};

    /**
     * RecyclerView where the items of the navigation drawer will be placed. The first element
     * will be a "header" item.
     */
    private RecyclerView mRecyclerView;

    /**
     * RecyclerView adapter for mRecylcerView.
     */
    private DrawerAdapter mAdapter;

    /**
     * List of DrawerItem objects to be displayed by mRecyclerView.
     */
    private List<DrawerItem> mDrawerItems;

    /**
     * Reference to the DrawerLayout within the layout file. Contains the RecyclerView to contain
     * the drawer items.
     */
    private DrawerLayout mDrawer;

    /**
     * Activity context. Used to start new activities when menu items are clicked.
     */
    private Activity mActivity;

    /**
     * Sole constructor to create a navigation drawer.
     * @param activity Activity context. Will be used to start other activities when items are clicked.
     * @param toolbar Activity's toolbar. Used to put the drawer icon onto that the user can tap to open the drawer.
     * @param username User's full name. Displayed in the header.
     * @param email User's email. Displayed in the header.
     * @param profileIconUrl URL to user's profile picture. Displayed in the header.
     */
    public NavDrawer(Activity activity, Toolbar toolbar, String username, String email, String profileIconUrl){
        mActivity = activity;
        mRecyclerView = (RecyclerView) activity.findViewById(R.id.navDrawer);
        mRecyclerView.setHasFixedSize(true);

        //Populate DrawerItems with the titles/icons
        mDrawerItems = new ArrayList<>();
        for(int i = 0; i < ROW_ITEMS.length; i++)
            mDrawerItems.add(new DrawerItem(ROW_ITEMS[i], ICON_IDS[i]));

        //Continue setting up the RecyclerView
        mAdapter = new DrawerAdapter(activity, mDrawerItems, username, email, profileIconUrl, this);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(layoutManager);

        mDrawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle mListener
                = new ActionBarDrawerToggle(activity, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close){};

        mDrawer.setDrawerListener(mListener);
        mListener.syncState();
    }

    /**
     * Called when drawer items are clicked. Starts the corresponding activity (if it is not the current activity).
     * @param itemText The text of the menu item clicked.
     */
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

            Intent intent = new Intent(mActivity, LocationActivity.class);

            //Clear back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        }else if(itemText.equals(EDIT_PROFILE_TEXT)){
            if(mActivity instanceof EditProfileActivity)
                return;

            mActivity.startActivity(new Intent(mActivity, EditProfileActivity.class));
        }else if(itemText.equals(LOGOUT_TEXT)){
            ParseUser.logOut();

            Intent intent = new Intent(mActivity, LoginActivity.class);

            //Clear back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.startActivity(intent);
        }
    }

    public DrawerLayout getDrawerLayout(){
        return mDrawer;
    }
}
