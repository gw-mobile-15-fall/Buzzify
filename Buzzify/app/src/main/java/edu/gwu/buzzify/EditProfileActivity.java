package edu.gwu.buzzify;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginFragment;
import com.parse.ui.ParseLoginHelpFragment;
import com.parse.ui.ParseOnLoadingListener;
import com.parse.ui.ParseOnLoginSuccessListener;

/**
 * Created by cheng on 12/4/15.
 */
public class EditProfileActivity extends AppCompatActivity implements
        ParseLoginFragment.ParseLoginFragmentListener,
        ParseLoginHelpFragment.ParseOnLoginHelpSuccessListener,
        ParseOnLoginSuccessListener, ParseOnLoadingListener {
    private static final String TAG = EditProfileActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Bundle configOptions = getMergedOptions();
        ParseUser user = ParseUser.getCurrentUser();

        getSupportFragmentManager().beginTransaction().add(R.id.mainLayout,
                EditProfileFragment.newInstance(configOptions, user.getUsername(), "")).commit();
    }


    public Bundle getMergedOptions() {
        // Read activity metadata from AndroidManifest.xml
        ActivityInfo activityInfo = null;
        try {
            activityInfo = getPackageManager().getActivityInfo(
                    this.getComponentName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            if (Parse.getLogLevel() <= Parse.LOG_LEVEL_ERROR &&
                    Log.isLoggable(TAG, Log.WARN)) {
                Log.w(TAG, e.getMessage());
            }
        }

        // The options specified in the Intent (from ParseLoginBuilder) will
        // override any duplicate options specified in the activity metadata
        Bundle mergedOptions = new Bundle();
        if (activityInfo != null && activityInfo.metaData != null) {
            mergedOptions.putAll(activityInfo.metaData);
        }
        if (getIntent().getExtras() != null) {
            mergedOptions.putAll(getIntent().getExtras());
        }

        return mergedOptions;
    }

    @Override
    public void onSignUpClicked(String username, String password) {

    }

    @Override
    public void onLoginHelpClicked() {

    }

    @Override
    public void onLoginSuccess() {
        finish();
    }

    @Override
    public void onLoadingStart(boolean showSpinner) {

    }

    @Override
    public void onLoadingFinish() {

    }

    @Override
    public void onLoginHelpSuccess() {

    }
}
