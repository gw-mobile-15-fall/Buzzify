package edu.gwu.buzzify;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import edu.gwu.buzzify.location.LocationWrapper;

/**
 * Created by cheng on 10/19/15.
 */
public class MainActivity extends AppCompatActivity {
    public static final String BUNDLE_KEY_LOCATION = "location";

    private static final String TAG = MainActivity.class.getName();
    private LocationWrapper mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the ActionBar for pre-Lollipop devices
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLocation = (LocationWrapper)getIntent().getParcelableExtra(BUNDLE_KEY_LOCATION);
        ((TextView)findViewById(R.id.tvPlaceholder)).setText("Current Location: " + mLocation);
    }
}
