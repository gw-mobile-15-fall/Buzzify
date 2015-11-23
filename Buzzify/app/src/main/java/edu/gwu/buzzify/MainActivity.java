package edu.gwu.buzzify;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.gwu.buzzify.tabs.DrinkQueueFragment;
import edu.gwu.buzzify.tabs.SongQueueFragment;
import edu.gwu.buzzify.tabs.ViewPagerAdapter;


public class MainActivity extends AppCompatActivity {
    public static final String BUNDLE_KEY_LOCATION = "location";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the ActionBar for pre-Lollipop devices
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTabLayout = (TabLayout) findViewById(R.id.mainTabs);
        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongQueueFragment(), "Songs");
        adapter.addFragment(new DrinkQueueFragment(), "Drinks");
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void onClick(View v){}
}
