package edu.gwu.buzzify;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import edu.gwu.buzzify.common.BundleKeys;
import edu.gwu.buzzify.common.ParseUtils;
import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.drinks.DrinkInfo;
import edu.gwu.buzzify.drinks.fragments.DrinkQueueFragment;
import edu.gwu.buzzify.firebase.FirebaseManager;
import edu.gwu.buzzify.spotify.SpotifyItem;
import edu.gwu.buzzify.spotify.fragments.SongQueueFragment;

/**
 * Main activity for admins (DJs and bartenders).
 */
public class AdminActivity extends AppCompatActivity implements QueueFragmentInterface {
    //Admin types
    public static final String TYPE_DJ = "dj";
    public static final String TYPE_BARTENDER = "bartender";

    private static final String TAG = AdminActivity.class.getName();

    private Toolbar mToolbar;
    private String mAdminType;

    private SongQueueFragment mSongQueueFragment;
    private DrinkQueueFragment mDrinkQueueFragment;

    //Information about the current logged in user
    private String mName;
    private String mEmail;
    private String mProfilePicUrl;

    private String mLocationName;

    private NavDrawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Set the ActionBar for pre-Lollipop devices
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mAdminType = getIntent().getStringExtra(BundleKeys.KEY_ADMIN_TYPE);

        //Retrieve information about the logged in user
        mName = ParseUtils.getUserActualName();
        mEmail = ParseUtils.getUserEmail();
        mProfilePicUrl = ParseUtils.getUserProfilePhotoUrl();
        mLocationName = getIntent().getStringExtra(BundleKeys.BUNDLE_KEY_LOCATION_NAME);

        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putBoolean(BundleKeys.KEY_HIDE_BUTTON, true);
        fragmentArgs.putString(BundleKeys.BUNDLE_KEY_LOCATION_NAME, mLocationName);
        fragmentArgs.putString(BundleKeys.BUNDLE_KEY_FULLNAME, mName);

        //DJs should see the song queue
        if(mAdminType.equals(TYPE_DJ)){
            mSongQueueFragment = new SongQueueFragment();
            mSongQueueFragment.setArguments(fragmentArgs);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentLayout, mSongQueueFragment);
            transaction.commit();

        //Bartenders should see the drink queue
        }else if(mAdminType.equals(TYPE_BARTENDER)){
            mDrinkQueueFragment = new DrinkQueueFragment();
            mDrinkQueueFragment.setArguments(fragmentArgs);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentLayout, mDrinkQueueFragment);
            transaction.commit();
        }

        Log.d(TAG, "Profile pic URL: " + mProfilePicUrl);
        mDrawer = new NavDrawer(this, mToolbar, mName, mEmail, mProfilePicUrl);
    }

    /**
     * Depending on the admin type, dequeue the item from the queue
     * @param item
     */
    @Override
    public void onItemPressed(Object item) {
        if(item instanceof SpotifyItem)
            new FirebaseManager(null, this, mLocationName).deleteSpotifyItem((SpotifyItem)item);
        else
            new FirebaseManager(null, this, mLocationName).deleteDrinkItem((DrinkInfo)item);
    }

    @Override
    public void onNewSongPlaying(SpotifyItem item) { }
    @Override
    public void onDrinkFinished(DrinkInfo drink) { }
}
