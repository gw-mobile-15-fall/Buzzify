package edu.gwu.buzzify.queues;

import android.os.Parcel;
import android.os.Parcelable;

public class SongInfo implements Parcelable{
    public String title, artist, album, albumArtUrl;

    public SongInfo(String title, String artist, String album, String albumArtUrl){
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumArtUrl = albumArtUrl;
    }

    //Recreate from a Parcel in the same order the data was written out
    public SongInfo(Parcel parcel){
        title = parcel.readString();
        artist = parcel.readString();
        album = parcel.readString();
        albumArtUrl = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(albumArtUrl);
    }

    //Formally used to call the constructor to recreate a SongInfo from a parcel
    public static final Creator CREATOR = new Creator() {
        @Override
        public SongInfo createFromParcel(Parcel source) {
            return new SongInfo(source);
        }

        @Override
        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };
}
