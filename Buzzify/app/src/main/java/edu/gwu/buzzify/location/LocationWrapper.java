package edu.gwu.buzzify.location;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Simply wraps together an address (returned by Google Places - might also include the bar/club's name)
 * and its GPS coordinates.
 *
 * Implements Parcelable for portability (across activities or screen rotations)
 */
public class LocationWrapper implements Parcelable{
    /**
     * A place's name as returned by Google Places
     */
    public String address;

    /**
     * GPS latitude
     */
    public double latitude;

    /**
     * GPS longitude
     */
    public double longitude;


    public LocationWrapper(CharSequence address, LatLng coordinates){
        this.address = address.toString();
        latitude = coordinates.latitude;
        longitude = coordinates.longitude;
    }

    //Recreate from a Parcel in the same order the data was written out
    public LocationWrapper(Parcel parcel){
        address = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    //Formally used to call the constructor to recreate a LocationWrapper from a parcel
    public static final Creator CREATOR = new Creator() {
        @Override
        public LocationWrapper createFromParcel(Parcel source) {
            return new LocationWrapper(source);
        }

        @Override
        public LocationWrapper[] newArray(int size) {
            return new LocationWrapper[size];
        }
    };

    @Override
    public String toString(){
        return address + " (" + latitude + ", " + longitude + ")";
    }
}
