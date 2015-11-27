package edu.gwu.buzzify.queues;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nick on 11/26/2015.
 */
public class SpotifyItem implements Parcelable{
    private String line1;
    private String line2;
    private String line3;
    private String thumbnailUrl;
    private String count;

    public SpotifyItem(String line1, String line2, String line3, String thumbnailUrl, String count){
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.thumbnailUrl = thumbnailUrl;
        this.count = count;
    }

    //Recreate from a Parcel in the same order the data was written out
    public SpotifyItem(Parcel parcel){
        line1 = parcel.readString();
        line2 = parcel.readString();
        line3 = parcel.readString();
        thumbnailUrl = parcel.readString();
        count = parcel.readString();
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

    public void setCount(String text){
        count = text;
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

    public String getCount(){
        return count;
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
        dest.writeString(count);
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
}
