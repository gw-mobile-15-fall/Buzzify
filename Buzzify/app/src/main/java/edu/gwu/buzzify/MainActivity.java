package edu.gwu.buzzify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.parse.GetDataCallback;
import com.parse.ParseException;

import edu.gwu.buzzify.common.ParseUtils;
import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.firebase.FirebaseManager;
import edu.gwu.buzzify.models.SpotifyItem;
import edu.gwu.buzzify.tabs.DrinkQueueFragment;
import edu.gwu.buzzify.tabs.SongQueueFragment;
import edu.gwu.buzzify.tabs.ViewPagerAdapter;


public class MainActivity extends AppCompatActivity implements GetDataCallback{
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
    private Bitmap mProfilePhoto = null;

    private FirebaseManager mFirebaseManager;

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
        adapter.addFragment(new SongQueueFragment(), "Songs");
        adapter.addFragment(new DrinkQueueFragment(), "Drinks");
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mName = ParseUtils.getUserActualName();
        mEmail = ParseUtils.getUserEmail();
        ParseUtils.getUserProfilePhoto(this);

        mFirebaseManager = new FirebaseManager(null, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case CODE_REQUEST_SEARCH_SONG:
                if(resultCode == CODE_RESULT_SONG_CHOSEN){
                    SpotifyItem chosenSong = data.getParcelableExtra(SpotifySearchActivity.KEY_CHOSEN_SONG);
                    Log.d(TAG, "Chosen song: " + chosenSong.getLine1() + " by " + chosenSong.getLine2() + " on " + chosenSong.getLine3());

                    mFirebaseManager.pushSpotifyItem(chosenSong);
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
                break;
        }
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
    public void done(byte[] data, ParseException e) {
        if (e == null) {
            mProfilePhoto = BitmapFactory.decodeByteArray(data, 0, data.length);

            if (mProfilePhoto == null) {
                Log.e(TAG, "BitmapFactory failed at creating bitmap from ByteArray");
            }

            //TODO placeholders if name, email, or bitmap is null
            mDrawer = new NavDrawer(this, mToolbar, mName, mEmail, mProfilePhoto);

        } else {
            Log.e(TAG,"ParseFile getDataInBackground returned an exception");
        }
    }
}
