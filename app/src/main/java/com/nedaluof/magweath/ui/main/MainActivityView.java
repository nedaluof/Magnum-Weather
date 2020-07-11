package com.nedaluof.magweath.ui.main;

import com.nedaluof.magweath.data.model.WeatherModel;
import com.nedaluof.magweath.ui.base.MvpView;

public interface MainActivityView extends MvpView {
    //current Day

    void showCurrentDay(WeatherModel weatherDayModel);

    void showErrorCurrentDay(String message);

    void showEmptyCurrentDay();

    void hideLoadingCurrentDay();

    //Current Day Forecast

    void showCurrentDayForecast(WeatherModel model);

    void showErrorCurrentDayForecast(String message);

    void showEmptyCurrentDayForecast();

    void hideLoadingCurrentDayForecast();

    void onDayClickListener(int position);
    //current Week Forecast


    void showCurrentWeekForecast(WeatherModel model);

    void showErrorCurrentWeekForecast(String message);

    void showEmptyCurrentWeekForecast();

    void hideLoadingCurrentWeekForecast();

    void onWeekClickListener(int position);

}
