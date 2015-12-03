package edu.gwu.buzzify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import edu.gwu.buzzify.drawer.NavDrawer;
import edu.gwu.buzzify.tabs.DrinkQueueFragment;
import edu.gwu.buzzify.tabs.SongQueueFragment;
import edu.gwu.buzzify.tabs.ViewPagerAdapter;


public class MainActivity extends AppCompatActivity {
    public static final String BUNDLE_KEY_LOCATION = "location";
    private static final String TAG = MainActivity.class.getName();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NavDrawer mDrawer;
    private Toolbar mToolbar;

    private String mName;
    private String mEmail;
    private Bitmap mProfilePhoto = null;

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

        getUserInfo();

    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnVoteSong:
                startActivity(new Intent(this, SpotifySearchActivity.class));
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

    private void getUserInfo() {
        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            mName = user.getString("name");
            mEmail = user.getEmail();
            getProfilePhoto(user);

        } else {
            Log.e(TAG, "Unable to get current ParseUser data");
        }
    }

    private void getProfilePhoto(ParseUser user) {

        if (user != null) {
            ParseFile file = (ParseFile)user.get("userPhoto");
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        mProfilePhoto = BitmapFactory.decodeByteArray(data, 0, data.length);

                        // TODO: 12/2/15 Only launches navdrawer if image download is successful...not sure if this is ok or not.
                        
                        mDrawer = new NavDrawer(MainActivity.this, mToolbar, mName, mEmail, mProfilePhoto);

                    } else {
                        Log.e(TAG,"ParseFile getDataInBackground returned an exception");
                    }
                }
            });
        }

    }

}
