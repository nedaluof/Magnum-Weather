package com.nedaluof.magweath.ui.main;

import android.location.Location;
import android.util.Log;

import com.nedaluof.magweath.MagApplication;
import com.nedaluof.magweath.data.DataManager;
import com.nedaluof.magweath.data.PrefsManager.PreferencesKey;
import com.nedaluof.magweath.di.ConfigPersistent;
import com.nedaluof.magweath.ui.base.BasePresenter;
import com.nedaluof.magweath.util.Const;
import com.nedaluof.magweath.util.RxUtil;
import com.nedaluof.magweath.util.Utility;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@ConfigPersistent
public class MainActivityPresenter extends BasePresenter<MainActivityView> {

    private final String TAG = MainActivityPresenter.class.getSimpleName();
    private Disposable disposable;
    private DataManager dataManager;

    @Inject
    public MainActivityPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(MainActivityView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (disposable != null) {
            disposable.dispose();
        }
    }



    public void loadWeatherDataWithLocation(Location location) {
        checkViewAttached();
        RxUtil.dispose(disposable);
        if (location != null) {
            dataManager.getPreferences().saveToPrefs(MagApplication.getInstance(), PreferencesKey.LATITUDE, location.getLatitude());
            dataManager.getPreferences().saveToPrefs(MagApplication.getInstance(), PreferencesKey.LONGITUDE, location.getLongitude());
            disposable = dataManager.getApiHelper().getWeatherData(location.getLatitude(), location.getLongitude(), "metric", Utility.getLanguage(), Const.API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weatherModel -> {
                        if (weatherModel != null) {
                            getMvpView().hideLoadingCurrentDay();
                            getMvpView().hideLoadingCurrentDayForecast();
                            getMvpView().hideLoadingCurrentWeekForecast();
                            getMvpView().showCurrentDay(weatherModel);
                            getMvpView().showCurrentDayForecast(weatherModel);
                            getMvpView().showCurrentWeekForecast(weatherModel);
                        } else {
                            getMvpView().hideLoadingCurrentDay();
                            getMvpView().hideLoadingCurrentDayForecast();
                            getMvpView().hideLoadingCurrentWeekForecast();
                            getMvpView().showEmptyCurrentDay();
                            getMvpView().showEmptyCurrentDayForecast();
                            getMvpView().showEmptyCurrentWeekForecast();
                        }
                    }, throwable -> {
                        getMvpView().hideLoadingCurrentDay();
                        getMvpView().hideLoadingCurrentDayForecast();
                        getMvpView().hideLoadingCurrentWeekForecast();
                        getMvpView().showErrorCurrentDay(throwable.getMessage());
                        getMvpView().showErrorCurrentDayForecast(throwable.getMessage());
                        getMvpView().showErrorCurrentWeekForecast(throwable.getMessage());
                    }, () -> Log.d(TAG, "onComplete:loadWeatherData "));


        } else if ((double) dataManager.getPreferences().
                getFromPrefs(MagApplication.getInstance(), PreferencesKey.LATITUDE, 0.0) != 0.0) {
            double lat = (double) dataManager.getPreferences().getFromPrefs(MagApplication.getInstance(), PreferencesKey.LATITUDE, 0.0);
            double lon = (double) dataManager.getPreferences().getFromPrefs(MagApplication.getInstance(), PreferencesKey.LONGITUDE, 0.0);
            loadWeatherDataWithLocationCoordinates(lat, lon);
        } else {
            // TODO: 7/9/2020  request location updates and turn on location
            getMvpView().hideLoadingCurrentDay();
            getMvpView().hideLoadingCurrentDayForecast();
            getMvpView().hideLoadingCurrentWeekForecast();
            getMvpView().showErrorCurrentDay("location null");
            getMvpView().showErrorCurrentDayForecast("location null");
            getMvpView().showErrorCurrentWeekForecast("location null");
        }

    }

    public void loadWeatherDataWithLocationCoordinates(double lat, double lon) {
        checkViewAttached();
        RxUtil.dispose(disposable);
        disposable = dataManager.getApiHelper().getWeatherData(lat, lon, "metric", Utility.getLanguage(), Const.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherModel -> {
                    if (weatherModel != null) {
                        getMvpView().hideLoadingCurrentDay();
                        getMvpView().hideLoadingCurrentDayForecast();
                        getMvpView().hideLoadingCurrentWeekForecast();
                        getMvpView().showCurrentDay(weatherModel);
                        getMvpView().showCurrentDayForecast(weatherModel);
                        getMvpView().showCurrentWeekForecast(weatherModel);
                    } else {
                        getMvpView().hideLoadingCurrentDay();
                        getMvpView().hideLoadingCurrentDayForecast();
                        getMvpView().hideLoadingCurrentWeekForecast();
                        getMvpView().showEmptyCurrentDay();
                        getMvpView().showEmptyCurrentDayForecast();
                        getMvpView().showEmptyCurrentWeekForecast();
                    }
                }, throwable -> {
                    getMvpView().hideLoadingCurrentDay();
                    getMvpView().hideLoadingCurrentDayForecast();
                    getMvpView().hideLoadingCurrentWeekForecast();
                    getMvpView().showErrorCurrentDay(throwable.getMessage());
                    getMvpView().showErrorCurrentDayForecast(throwable.getMessage());
                    getMvpView().showErrorCurrentWeekForecast(throwable.getMessage());
                }, () -> Log.d(TAG, "onComplete:loadWeatherData "));

    }


}
