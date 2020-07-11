package com.nedaluof.magweath.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherModel implements Parcelable{

    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose
    private double lon;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("hourly")
    @Expose
    private List<Hourly> hourly;
    @SerializedName("daily")
    @Expose
    private List<Daily> daily;

    public WeatherModel(double lat, double lon, String timezone, Current current, List<Hourly> hourly, List<Daily> daily) {
        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.current = current;
        this.hourly = hourly;
        this.daily = daily;
    }

    public WeatherModel(Parcel in) {
        lat = in.readDouble();
        lon = in.readDouble();
        timezone = in.readString();
        current = in.readParcelable(Current.class.getClassLoader());
        hourly = in.createTypedArrayList(Hourly.CREATOR);
        daily = in.createTypedArrayList(Daily.CREATOR);
    }

    public static final Creator<WeatherModel> CREATOR = new Creator<WeatherModel>() {
        @Override
        public WeatherModel createFromParcel(Parcel in) {
            return new WeatherModel(in);
        }

        @Override
        public WeatherModel[] newArray(int size) {
            return new WeatherModel[size];
        }
    };

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public Current getCurrent() {
        return current;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeString(timezone);
        dest.writeParcelable(current, flags);
        dest.writeTypedList(hourly);
        dest.writeTypedList(daily);
    }
}
