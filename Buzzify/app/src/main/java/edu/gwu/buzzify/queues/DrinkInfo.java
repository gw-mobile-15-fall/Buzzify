package edu.gwu.buzzify.queues;

import android.os.Parcel;
import android.os.Parcelable;

public class DrinkInfo implements Parcelable{
    public String name, iconUrl;

    public DrinkInfo(String name, String iconUrl){
        this.name = name;
        this.iconUrl = iconUrl;
    }

    //Recreate from a Parcel in the same order the data was written out
    public DrinkInfo(Parcel parcel){
        name = parcel.readString();
        iconUrl = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(iconUrl);
    }

    //Formally used to call the constructor to recreate a DrinkInfo from a parcel
    public static final Creator CREATOR = new Creator() {
        @Override
        public DrinkInfo createFromParcel(Parcel source) {
            return new DrinkInfo(source);
        }

        @Override
        public DrinkInfo[] newArray(int size) {
            return new DrinkInfo[size];
        }
    };
}
