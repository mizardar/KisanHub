package in.mizardar.kisanhubtest.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mizardar on 13/03/18.
 */

public class ModelValues implements Parcelable{

    public static final Creator<ModelValues> CREATOR = new Creator<ModelValues>() {
        @Override
        public ModelValues createFromParcel(Parcel in) {
            return new ModelValues(in);
        }

        @Override
        public ModelValues[] newArray(int size) {
            return new ModelValues[size];
        }
    };
    private int year;
    private String[] values = new String[17];
    private int regionID;
    private int catID;

    public ModelValues() {
    }

    public ModelValues(int year, String[] values) {
        this.year = year;
        this.values = values;
    }

    protected ModelValues(Parcel in) {
        year = in.readInt();
        values = in.createStringArray();
        regionID = in.readInt();
        catID = in.readInt();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public int getRegionID() {
        return regionID;
    }

    public void setRegionID(int regionID) {
        this.regionID = regionID;
    }

    public int getCatID() {
        return catID;
    }

    public void setCatID(int catID) {
        this.catID = catID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(year);
        parcel.writeStringArray(values);
        parcel.writeInt(regionID);
        parcel.writeInt(catID);
    }
}
