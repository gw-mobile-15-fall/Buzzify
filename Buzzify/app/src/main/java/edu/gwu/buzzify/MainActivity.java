package edu.gwu.buzzify;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import edu.gwu.buzzify.common.BundleKeys;
import edu.gwu.buzzify.common.ParseUtils;
import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.drinks.DrinkInfo;
import edu.gwu.buzzify.drinks.fragments.DrinkQueueFragment;
import edu.gwu.buzzify.firebase.FirebaseManager;
import edu.gwu.buzzify.spotify.SpotifyItem;
import edu.gwu.buzzify.spotify.fragments.SongQueueFragment;
import edu.gwu.buzzify.tabs.ViewPagerAdapter;

/**
 * MainActivity for regular user. Allows them to view both the song and drink quques.
 */
public class MainActivity extends AppCompatActivity implements QueueFragmentInterface, EditTextDialog.EditTextDialogListener{
    public static final int CODE_REQUEST_SEARCH_SONG = 0x00;
    public static final int CODE_RESULT_SONG_CHOSEN = 0x0A;

    private static final String TAG = MainActivity.class.getName();

    //Widgets for a tabbed layout
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private NavDrawer mDrawer;

    private Toolbar mToolbar;

    //Information about the currently logged in user
    private String mName;
    private String mEmail;
    private String mProfilePicUrl;
    private String mLocationName;

    /**
     * Used to push new items up to Firebase
     */
    private FirebaseManager mFirebaseManager;

    private SongQueueFragment mSongQueueFragment;
    private DrinkQueueFragment mDrinkQueueFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the ActionBar for pre-Lollipop devices
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Setup tabbed layout
        mTabLayout = (TabLayout) findViewById(R.id.mainTabs);
        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Retrieve information about the currently logged in user
        mName = ParseUtils.getUserActualName();
        mEmail = ParseUtils.getUserEmail();
        mProfilePicUrl = ParseUtils.getUserProfilePhotoUrl();
        mLocationName = getIntent().getStringExtra(BundleKeys.BUNDLE_KEY_LOCATION_NAME);

        //Create fragments
        mSongQueueFragment = new SongQueueFragment();
        mDrinkQueueFragment = new DrinkQueueFragment();

        //Set data to send to fragments (location name and user's name)
        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putString(BundleKeys.BUNDLE_KEY_LOCATION_NAME, mLocationName);
        fragmentBundle.putString(BundleKeys.BUNDLE_KEY_FULLNAME, mName);

        mSongQueueFragment.setArguments(fragmentBundle);
        mDrinkQueueFragment.setArguments(fragmentBundle);

        //Add fragments to view
        adapter.addFragment(mSongQueueFragment, "Songs");
        adapter.addFragment(mDrinkQueueFragment, "Drinks");

        //Prepare tabbed layout
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //Setup navigation drawer
        mDrawer = new NavDrawer(this, mToolbar, mName, mEmail, mProfilePicUrl);

        //Create FirebaseManager with no callbacks (only used to push items up)
        mFirebaseManager = new FirebaseManager(null, this, mLocationName);
    }

    /**
     * Reference the NavDrawer's header if the user is returning from the EditProfile activity
     * Otherwise nothing will change.
     */
    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
        mName = ParseUtils.getUserActualName();
        mEmail = ParseUtils.getUserEmail();
        mProfilePicUrl = ParseUtils.getUserProfilePhotoUrl();

        if(mDrawer != null){
            mDrawer.setFullName(mName);
            mDrawer.setEmail(mEmail);
            mDrawer.setProfilePic(mProfilePicUrl);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_share:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm at: " + mLocationName + " listening to music and ordering drinks!");
                shareIntent.setType("text/plain");
                startActivity(shareIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the SpotifySearchActivity is finished (i.e. the user has chosen a song to vote for).
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case CODE_REQUEST_SEARCH_SONG:
                if(resultCode == CODE_RESULT_SONG_CHOSEN){
                    //Grab the chosen song from the Intent
                    SpotifyItem chosenSong = data.getParcelableExtra(BundleKeys.KEY_CHOSEN_SONG);
                    Log.d(TAG, "Chosen song: " + chosenSong.getLine1() + " by " + chosenSong.getLine2() + " on " + chosenSong.getLine3());

                    //The song chosen may already be in the song queue, search for it.
                    SpotifyItem searchInFragment = mSongQueueFragment.getSpotifyItem(chosenSong);

                    if(searchInFragment != null) {
                        //Increment the vote on the item
                        Log.d(TAG, "Song already in queue");
                        long count = searchInFragment.getCount();
                        searchInFragment.setCount(++count);
                        mFirebaseManager.pushSpotifyItem(searchInFragment);
                    } else {
                        //Add a new item with count = 1
                        chosenSong.setCount(1);
                        mFirebaseManager.pushSpotifyItem(chosenSong);
                    }
                }
                break;
        }
    }

    /**
     * If the user wants to vote for a song, go to the SpotifySearchActivity, otherwise
     * open the order drinks dialog.
     * @param v
     */
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnVoteSong:
                Intent voteSongIntent = new Intent(this, SpotifySearchActivity.class);
                startActivityForResult(voteSongIntent, CODE_REQUEST_SEARCH_SONG);
                break;
            case R.id.btnOrderDrink:
                showDrinksDialog();
                break;
        }
    }

    /**
     * Display an instance of the EditText dialog for ordering drinks
     */
    private void showDrinksDialog(){
        new EditTextDialog().show(getSupportFragmentManager(), "Show Drinks Dialog");
    }

    /**
     * If the back button is pressed, close the drawer if it is open
     */
    @Override
    public void onBackPressed(){
        if(mDrawer.getDrawerLayout().isDrawerOpen(GravityCompat.START)){
            mDrawer.getDrawerLayout().closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    /**
     * Push the chosen song up to Firebase
     * @param clickedItem
     */
    @Override
    public void onItemPressed(Object clickedItem) {
        if(clickedItem instanceof DrinkInfo)
            return;

        SpotifyItem item = (SpotifyItem)clickedItem;
        long count = item.getCount();
        item.setCount(++count);
        mFirebaseManager.pushSpotifyItem(item);
    }

    /**
     * When a new song starts playing, make a notification. Called from the SongQueueFragment.
     * @param item
     */
    @Override
    public void onNewSongPlaying(SpotifyItem item) {
        Log.d(TAG, "Currently playing: " + item.getLine1() + " by " + item.getLine2());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.placeholder_album)
                        .setContentTitle("Now Playing")
                        .setContentText(item.getLine1() + " - " + item.getLine2());

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, mBuilder.build());
    }

    /**
     * When the user's drink is finished, make a notification. Called from the DrinkQueueFragment.
     * @param info
     */
    @Override
    public void onDrinkFinished(DrinkInfo info) {
        Log.d(TAG, "My drink is done!");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.placeholder_drink)
                        .setContentTitle("Your Drink is Ready!")
                        .setContentText(info.getDrinkName());

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(0, mBuilder.build());
    }

    /**
     * Called from the EditTextDialog, push up the new drink order.
     * @param input
     */
    @Override
    public void onPositiveClicked(String input) {
        Log.d(TAG, "User wants to order: " + input);
        DrinkInfo newDrink = new DrinkInfo(input, mName, mProfilePicUrl);
        mFirebaseManager.pushDrink(newDrink);
    }

    @Override
    public void onCancelClicked() {

    }
}
