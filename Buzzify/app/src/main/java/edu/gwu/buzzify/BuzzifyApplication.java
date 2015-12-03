package edu.gwu.buzzify;

import com.parse.Parse;

/**
 * Created by tito on 12/3/15.
 *
 * Application class is created since Parse initialize can only be called once, such that it shouldn't be called from the LoginActivity
 */
public class BuzzifyApplication extends android.app.Application {

    public void onCreate() {
        super.onCreate();

        //Setup Parse
        Parse.initialize(this);
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
    }
}
