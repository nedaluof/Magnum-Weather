package com.nedaluof.magweath.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Daily implements Parcelable {

    @SerializedName("dt")
    @Expose
    private int dt;
    @SerializedName("sunrise")
    @Expose
    private int sunrise;
    @SerializedName("sunset")
    @Expose
    private int sunset;
    @SerializedName("temp")
    @Expose
    private Temp temp;
    @SerializedName("feels_like")
    @Expose
    private FeelsLike feelsLike;
    @SerializedName("pressure")
    @Expose
    private int pressure;
    @SerializedName("humidity")
    @Expose
    private int humidity;
    @SerializedName("dew_point")
    @Expose
    private double dewPoint;
    @SerializedName("wind_speed")
    @Expose
    private double windSpeed;
    @SerializedName("wind_deg")
    @Expose
    private int windDeg;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("clouds")
    @Expose
    private int clouds;
    @SerializedName("uvi")
    @Expose
    private double uvi;

    public Daily(Parcel in) {
        dt = in.readInt();
        sunrise = in.readInt();
        sunset = in.readInt();
        temp = in.readParcelable(Temp.class.getClassLoader());
        feelsLike = in.readParcelable(FeelsLike.class.getClassLoader());
        pressure = in.readInt();
        humidity = in.readInt();
        dewPoint = in.readDouble();
        windSpeed = in.readDouble();
        windDeg = in.readInt();
        weather = in.createTypedArrayList(Weather.CREATOR);
        clouds = in.readInt();
        uvi = in.readDouble();
    }

    public static final Creator<Daily> CREATOR = new Creator<Daily>() {
        @Override
        public Daily createFromParcel(Parcel in) {
            return new Daily(in);
        }

        @Override
        public Daily[] newArray(int size) {
            return new Daily[size];
        }
    };

    public int getDt() {
        return dt;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeelsLike() {
        return feelsLike;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getWindDeg() {
        return windDeg;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public int getClouds() {
        return clouds;
    }

    public double getUvi() {
        return uvi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dt);
        dest.writeInt(sunrise);
        dest.writeInt(sunset);
        dest.writeParcelable(temp, flags);
        dest.writeParcelable(feelsLike, flags);
        dest.writeInt(pressure);
        dest.writeInt(humidity);
        dest.writeDouble(dewPoint);
        dest.writeDouble(windSpeed);
        dest.writeInt(windDeg);
        dest.writeTypedList(weather);
        dest.writeInt(clouds);
        dest.writeDouble(uvi);
    }
}