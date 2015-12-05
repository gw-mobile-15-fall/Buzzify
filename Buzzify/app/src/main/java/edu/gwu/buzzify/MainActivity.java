package edu.gwu.buzzify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import edu.gwu.buzzify.common.ParseUtils;
import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.drinks.DrinkInfo;
import edu.gwu.buzzify.drinks.fragments.DrinkQueueFragment;
import edu.gwu.buzzify.firebase.FirebaseManager;
import edu.gwu.buzzify.spotify.SpotifyItem;
import edu.gwu.buzzify.spotify.fragments.SongQueueFragment;
import edu.gwu.buzzify.tabs.ViewPagerAdapter;


public class MainActivity extends AppCompatActivity implements QueueFragmentInterface, EditTextDialog.EditTextDialogListener{
    public static final int CODE_REQUEST_SEARCH_SONG = 0x00;
    public static final int CODE_RESULT_SONG_CHOSEN = 0x0A;

    public static final String BUNDLE_KEY_LOCATION = "location";


    private static final String TAG = MainActivity.class.getName();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NavDrawer mDrawer;
    private Toolbar mToolbar;

    private String mName;
    private String mEmail;
    private String mProfilePicUrl;
    private Bitmap mProfilePhoto = null;

    private FirebaseManager mFirebaseManager;

    private SongQueueFragment mSongQueueFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the ActionBar for pre-Lollipop devices
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTabLayout = (TabLayout) findViewById(R.id.mainTabs);
        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mSongQueueFragment = new SongQueueFragment();
        adapter.addFragment(mSongQueueFragment, "Songs");
        adapter.addFragment(new DrinkQueueFragment(), "Drinks");
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mName = ParseUtils.getUserActualName();
        mEmail = ParseUtils.getUserEmail();
        //ParseUtils.getUserProfilePhoto(this);
        mProfilePicUrl = ParseUtils.getUserProfilePhotoUrl();

        Log.d(TAG, "Profile pic URL: " + mProfilePicUrl);
        mDrawer = new NavDrawer(this, mToolbar, mName, mEmail, mProfilePicUrl);


        mFirebaseManager = new FirebaseManager(null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case CODE_REQUEST_SEARCH_SONG:
                if(resultCode == CODE_RESULT_SONG_CHOSEN){
                    SpotifyItem chosenSong = data.getParcelableExtra(SpotifySearchActivity.KEY_CHOSEN_SONG);
                    Log.d(TAG, "Chosen song: " + chosenSong.getLine1() + " by " + chosenSong.getLine2() + " on " + chosenSong.getLine3());

                    SpotifyItem searchInFragment = mSongQueueFragment.getSpotifyItem(chosenSong);

                    if(searchInFragment != null) {
                        Log.d(TAG, "Song already in queue");
                        long count = searchInFragment.getCount();
                        searchInFragment.setCount(++count);
                        mFirebaseManager.pushSpotifyItem(searchInFragment);
                    } else {
                        chosenSong.setCount(1);
                        mFirebaseManager.pushSpotifyItem(chosenSong);
                    }
                }
                break;
        }
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnVoteSong:
                Intent voteSongIntent = new Intent(this, SpotifySearchActivity.class);
                voteSongIntent.putExtra(SpotifySearchActivity.KEY_STARTED_FOR_RESULT, true);
                startActivityForResult(voteSongIntent, CODE_REQUEST_SEARCH_SONG);
                break;
            case R.id.btnOrderDrink:
                showDrinksDialog();
                break;
        }
    }

    private void showDrinksDialog(){
        new EditTextDialog().show(getSupportFragmentManager(), "Show Drinks Dialog");
    }

    @Override
    public void onBackPressed(){
        if(mDrawer.getDrawerLayout().isDrawerOpen(GravityCompat.START)){
            mDrawer.getDrawerLayout().closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onItemPressed(Object clickedItem) {
        if(clickedItem instanceof DrinkInfo)
            return;

        SpotifyItem item = (SpotifyItem)clickedItem;
        long count = item.getCount();
        item.setCount(++count);
        mFirebaseManager.pushSpotifyItem(item);
    }

    @Override
    public void onPositiveClicked(String input) {
        Log.d(TAG, "User wants to order: " + input);
        DrinkInfo newDrink = new DrinkInfo(input, mName, "");
        mFirebaseManager.pushDrink(newDrink);
    }

    @Override
    public void onCancelClicked() {

    }
}
