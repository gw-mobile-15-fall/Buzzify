package edu.gwu.buzzify.drawer;

/**
 * A drawer item consists of a title and an icon (drawable resource ID)
 */
public class DrawerItem {
    public int iconId;
    public String text;

    public DrawerItem(String text, int iconId){
        this.text = text;
        this.iconId = iconId;
    }
}
