package com.nedaluof.magweath.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Hourly implements Parcelable {

    @SerializedName("dt")
    @Expose
    private int dt;
    @SerializedName("temp")
    @Expose
    private double temp;
    @SerializedName("feels_like")
    @Expose
    private double feelsLike;
    @SerializedName("pressure")
    @Expose
    private int pressure;
    @SerializedName("humidity")
    @Expose
    private int humidity;
    @SerializedName("dew_point")
    @Expose
    private double dewPoint;
    @SerializedName("clouds")
    @Expose
    private int clouds;
    @SerializedName("wind_speed")
    @Expose
    private double windSpeed;
    @SerializedName("wind_deg")
    @Expose
    private int windDeg;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;

    public Hourly(Parcel in) {
        dt = in.readInt();
        temp = in.readDouble();
        feelsLike = in.readDouble();
        pressure = in.readInt();
        humidity = in.readInt();
        dewPoint = in.readDouble();
        clouds = in.readInt();
        windSpeed = in.readDouble();
        windDeg = in.readInt();
        weather = in.createTypedArrayList(Weather.CREATOR);
    }

    public static final Creator<Hourly> CREATOR = new Creator<Hourly>() {
        @Override
        public Hourly createFromParcel(Parcel in) {
            return new Hourly(in);
        }

        @Override
        public Hourly[] newArray(int size) {
            return new Hourly[size];
        }
    };

    public int getDt() {
        return dt;
    }

    public double getTemp() {
        return temp;
    }

    public double getFeelsLike() {
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

    public int getClouds() {
        return clouds;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dt);
        dest.writeDouble(temp);
        dest.writeDouble(feelsLike);
        dest.writeInt(pressure);
        dest.writeInt(humidity);
        dest.writeDouble(dewPoint);
        dest.writeInt(clouds);
        dest.writeDouble(windSpeed);
        dest.writeInt(windDeg);
        dest.writeTypedList(weather);
    }
}
