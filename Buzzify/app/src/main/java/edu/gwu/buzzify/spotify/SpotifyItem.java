package edu.gwu.buzzify.spotify;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents an item retrieved from Spotify. Modeled using "lines"
 * For example, a song is usually displayed:
 *      Title
 *      Artist
 *      Album
 *
 * An artist might be displayed:
 *      Artist
 *      Genre
 *
 * An album might be displayed:
 *      Album
 *      Artist
 *      Genre
 */
public class SpotifyItem implements Parcelable{
    public static final String KEY_LINE1 = "line1";
    public static final String KEY_LINE2 = "line2";
    public static final String KEY_LINE3 = "line3";
    public static final String KEY_THUMBNAIL_URL = "thumbnailUrl";
    public static final String KEY_COUNT = "count";
    public static final String KEY_ID = "id";

    private String line1;
    private String line2;
    private String line3;
    private String thumbnailUrl;
    private long count;
    private String id;

    public SpotifyItem(){}

    public SpotifyItem(String line1, String line2, String line3, String thumbnailUrl, String id){
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.thumbnailUrl = thumbnailUrl;
        this.id = id;

        count = 0;
    }

    //Recreate from a Parcel in the same order the data was written out
    public SpotifyItem(Parcel parcel){
        line1 = parcel.readString();
        line2 = parcel.readString();
        line3 = parcel.readString();
        thumbnailUrl = parcel.readString();
        count = parcel.readLong();
        id = parcel.readString();
    }

    public void setLine1(String text){
        line1 = text;
    }

    public void setLine2(String text){
        line2 = text;
    }

    public void setLine3(String text){
        line3 = text;
    }

    public void setThumbnailUrl(String text){
        thumbnailUrl = text;
    }

    public void setCount(long count){
        this.count = count;
    }

    public void setId(String id){
        this.id = id;
    }


    public String getLine1(){
        return line1;
    }

    public String getLine2(){
        return line2;
    }

    public String getLine3(){
        return line3;
    }

    public String getThumbnailUrl(){
        return thumbnailUrl;
    }

    public long getCount(){
        return count;
    }

    public String getId(){
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(line1);
        dest.writeString(line2);
        dest.writeString(line3);
        dest.writeString(thumbnailUrl);
        dest.writeLong(count);
        dest.writeString(id);
    }

    //Formally used to call the constructor to recreate a SongInfo from a parcel
    public static final Creator CREATOR = new Creator() {
        @Override
        public SpotifyItem createFromParcel(Parcel source) {
            return new SpotifyItem(source);
        }

        @Override
        public SpotifyItem[] newArray(int size) {
            return new SpotifyItem[size];
        }
    };

    @Override
    public boolean equals(Object o){
        return o instanceof SpotifyItem && ((SpotifyItem)o).getId().equals(id);
    }
}
