package com.nedaluof.magweath.data.model;

public class MagForecastData {
    private int temp;
    private String dayName;
    private String imgPath;

    public MagForecastData(int temp, String dayName, String imgPath) {
        this.temp = temp;
        this.dayName = dayName;
        this.imgPath = imgPath;
    }

    public int getTemp() {
        return temp;
    }

    public String getDayName() {
        return dayName;
    }

    public String getImgPath() {
        return imgPath;
    }

}
