package edu.gwu.buzzify.common;

import com.parse.ParseUser;

/**
 * Utility functions for getting information about the currently logged in user.
 */
public class ParseUtils {
    private static final String TAG = ParseUtils.class.getName();

    /**
     * Retrieve the user's full name as inputted during registration.
     * @return User's name, or null otherwise.
     */
    public static String getUserActualName() {
        ParseUser user = ParseUser.getCurrentUser();

        if (user != null)
            return user.getString("name");
        return null;
    }

    /**
     * Retrieve the user's email as inputted during registration. Also doubles as their
     * username.
     * @return User's email, or null otherwise.
     */
    public static String getUserEmail() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null)
            return user.getEmail();
        return null;
    }

    /**
     * Retrieve a URL for the user's profile picture on Parse.
     * @return User's profile pic URL, or null otherwise.
     */
    public static String getUserProfilePhotoUrl() {
        ParseUser user = ParseUser.getCurrentUser();

        if (user != null)
            return user.getParseFile("userPhoto").getUrl();
        return null;
    }
}
