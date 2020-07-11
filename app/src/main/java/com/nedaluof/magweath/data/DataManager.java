package com.nedaluof.magweath.data;

import android.util.Log;

import com.nedaluof.magweath.data.PrefsManager.PreferencesHelper;
import com.nedaluof.magweath.data.api.ApiService;
import com.nedaluof.magweath.data.api.RetrofitClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {
    private PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public ApiService getApiHelper() {
        return RetrofitClient.getInstance().create(ApiService.class);
    }

    public PreferencesHelper getPreferences() {
        return preferencesHelper;
    }
}
