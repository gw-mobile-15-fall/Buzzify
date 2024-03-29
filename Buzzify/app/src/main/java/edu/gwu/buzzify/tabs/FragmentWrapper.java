package edu.gwu.buzzify.tabs;

import android.support.v4.app.Fragment;

/**
 * Wraps a Fragment with its String title
 */
public class FragmentWrapper {
    public Fragment fragment;
    public String title;

    public FragmentWrapper(Fragment fragment, String title){
        this.fragment = fragment;
        this.title = title;
    }
}
