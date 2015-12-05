package edu.gwu.buzzify.common;

import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by cheng on 12/4/15.
 */
public class ParseUtils {
    private static final String TAG = ParseUtils.class.getName();
    public static String getUserActualName() {
        ParseUser user = ParseUser.getCurrentUser();

        if (user != null)
            return user.getString("name");
        return null;
    }

    public static String getUserEmail() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null)
            return user.getEmail();
        return null;
    }

    public static String getUserProfilePhotoUrl() {
        ParseUser user = ParseUser.getCurrentUser();

        if (user != null)
            return user.getParseFile("userPhoto").getUrl();
        return null;
    }

    public static void getUserProfilePhoto(GetDataCallback callback) {
        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            ParseFile file = (ParseFile) user.get("userPhoto");
            file.getDataInBackground(callback);
        }
    }
}
