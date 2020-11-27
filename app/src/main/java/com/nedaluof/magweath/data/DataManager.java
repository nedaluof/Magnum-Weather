package com.nedaluof.magweath.data;

import com.nedaluof.magweath.data.PrefsManager.PreferencesHelper;
import com.nedaluof.magweath.data.api.ApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {
    private PreferencesHelper preferencesHelper;
    private ApiService client;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, ApiService client) {
        this.preferencesHelper = preferencesHelper;
        this.client = client;
    }

    public ApiService getApiHelper() {
        return client;
    }

    public PreferencesHelper getPreferences() {
        return preferencesHelper;
    }
}
