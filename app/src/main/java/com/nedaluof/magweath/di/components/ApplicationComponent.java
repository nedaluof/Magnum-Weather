package com.nedaluof.magweath.di.components;

import android.app.Application;
import android.content.Context;

import com.nedaluof.magweath.data.DataManager;
import com.nedaluof.magweath.data.PrefsManager.PreferencesHelper;
import com.nedaluof.magweath.di.ApplicationContext;
import com.nedaluof.magweath.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    PreferencesHelper preferencesHelper();

    DataManager dataManager();


}
