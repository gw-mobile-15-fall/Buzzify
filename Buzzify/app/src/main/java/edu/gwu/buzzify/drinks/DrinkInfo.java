package edu.gwu.buzzify.drinks;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a drink in the drink queue (i.e. an ordered drink). Contains
 * the requested drink, the customer's name, an icon URL, and a unique id (from Firebase).
 */
public class DrinkInfo implements Parcelable{
    public String drinkName, customerName, iconUrl, firebaseId;

    public DrinkInfo(){}

    public DrinkInfo(String drinkName, String customerName, String iconUrl){
        this.drinkName = drinkName;
        this.customerName = customerName;
        this.iconUrl = iconUrl;
        firebaseId = "";
    }

    //Recreate from a Parcel in the same order the data was written out
    public DrinkInfo(Parcel parcel){
        drinkName = parcel.readString();
        customerName = parcel.readString();
        iconUrl = parcel.readString();
        firebaseId = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(drinkName);
        dest.writeString(customerName);
        dest.writeString(iconUrl);
        dest.writeString(firebaseId);
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

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setFirebaseId(String firebaseId){
        this.firebaseId = firebaseId;
    }

    public String getFirebaseId(){
        return firebaseId;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof DrinkInfo && ((DrinkInfo)o).firebaseId.equals(firebaseId);
    }
}
