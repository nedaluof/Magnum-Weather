package com.nedaluof.magweath.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeelsLike implements Parcelable {

    @SerializedName("day")
    @Expose
    private double day;
    @SerializedName("night")
    @Expose
    private double night;
    @SerializedName("eve")
    @Expose
    private double eve;
    @SerializedName("morn")
    @Expose
    private double morn;

    public FeelsLike(Parcel in) {
        day = in.readDouble();
        night = in.readDouble();
        eve = in.readDouble();
        morn = in.readDouble();
    }

    public static final Creator<FeelsLike> CREATOR = new Creator<FeelsLike>() {
        @Override
        public FeelsLike createFromParcel(Parcel in) {
            return new FeelsLike(in);
        }

        @Override
        public FeelsLike[] newArray(int size) {
            return new FeelsLike[size];
        }
    };

    public double getDay() {
        return day;
    }

    public double getNight() {
        return night;
    }

    public double getEve() {
        return eve;
    }

    public double getMorn() {
        return morn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(day);
        dest.writeDouble(night);
        dest.writeDouble(eve);
        dest.writeDouble(morn);
    }
}
