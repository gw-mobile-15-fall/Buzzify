package edu.gwu.buzzify;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.ParseUser;

import edu.gwu.buzzify.location.LocationWrapper;

/**
 * Uses the Google Places API to determine the user's current location (include address / building).
 * Passes this information on the MainActivity.
 */
public class LocationActivity extends AppCompatActivity {
    /**
     * Logcat tag
     */
    private static final String TAG = LocationActivity.class.getName();

    /**
     * Request code used when starting the PlacePicker activity for result.
     */
    private static final byte CODE_LOCATION_PICKER = 0x00;

    /**
     * Request code used when displaying the Google Play services installation dialog.
     */
    private static final byte CODE_INSTALL_PLAY_SERVICES = 0x01;

    /**
     * Reference to the main linear layout for displaying SnackBars if an error occurs.
     */
    private LinearLayout mMainLayout;

    /**
     * Determines whether or not to show the place picker in onResume
     */
    private boolean mShowPlacePicker = true;


    /**
     * Setup widgets.
     * @param savedInstanceState unused (PlacePicker activity already takes care of screen rotations)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //Set the ActionBar for pre-Lollipop devices
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMainLayout = (LinearLayout)findViewById(R.id.layoutActivityLocation);
    }


    /**
     * Show the PlacePicker if the user has not chosen a location yet.
     */
    @Override
    public void onResume(){
        super.onResume();

        if(mShowPlacePicker)
            showPlacePicker();
        else
            mShowPlacePicker = true; //If the user comes back to this activity, show the PlacePicker again
    }


    /**
     * Display the PlacePicker using the Google Places API. Requires the user to have
     * Google Play Services installed on their devices. If it isn't, a dialog will
     * prompt them to install.
     */
    private void showPlacePicker(){
        try {
            //Used to construct the intent to launch the PlacePicker
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            //Start the PlacePicker activity
            startActivityForResult(builder.build(this), CODE_LOCATION_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            //Exception occurs if the user doesn't have Google Play Services installed.
            //Display a dialog to prompt them to install it (provided by the Google APIs).
            GoogleApiAvailability.getInstance().getErrorDialog
                    (this, e.getConnectionStatusCode(),CODE_INSTALL_PLAY_SERVICES).show();

        } catch (GooglePlayServicesNotAvailableException e) {
            //Exception occurs if Google Play Services is not available. Perhaps a
            //network issue? Display a Snackbar to let the user retry the connection.
            Snackbar retryBar = Snackbar.make(mMainLayout,
                    getString(R.string.play_services_failed_connection), Snackbar.LENGTH_INDEFINITE);

            retryBar.setAction(getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPlacePicker(); //Try to connect to Play Services again
                }
            });
        }
    }

    /**
     * Called when the PlacePicker returns.
     * @param requestCode The code indicated when we started the activity
     * @param resultCode The code returned when the activity exited (RESULT_OK if successful, RESULT_CANCELLED if the user pressed the back button
     * @param data Includes data being returned by the activity (like the chosen Place).
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Request code = " + requestCode + ", result code = " + resultCode);
        switch(requestCode){
            case CODE_LOCATION_PICKER:
                mShowPlacePicker = false;

                //If the user actually selected a location
                if(resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);


                    //Start the next activity
                    String userType = ParseUser.getCurrentUser().getString("accountType");
                    Intent intent = null;
                    if(userType.equals("standard")){
                        intent = new Intent(this, MainActivity.class);

                        Log.d(TAG, "Preparing to start MainActivity");

                        //Package the location information up to send to the MainActivity
                        intent.putExtra(MainActivity.BUNDLE_KEY_LOCATION,
                                new LocationWrapper(place.getAddress(), place.getLatLng()));
                    }else if(userType.equals("dj") || userType.equals("bartender")){
                        intent = new Intent(this, AdminActivity.class);
                        intent.putExtra(AdminActivity.KEY_ADMIN_TYPE, userType);
                    }

                    startActivity(intent);
                }else if(resultCode == RESULT_CANCELED){
                    //If the user hit the back button, we'll send them back to the LoginActivity
                    finish();
                } else if (requestCode == RESULT_FIRST_USER) {

                }

                break;
        }
    }
}
