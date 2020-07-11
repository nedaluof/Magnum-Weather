package com.nedaluof.magweath;

import android.app.Application;
import android.content.Context;

import com.nedaluof.magweath.di.components.ApplicationComponent;
import com.nedaluof.magweath.di.components.DaggerApplicationComponent;
import com.nedaluof.magweath.di.modules.ApplicationModule;

public class MagApplication extends Application {

    private static MagApplication mInstance;
    private ApplicationComponent applicationComponent;

    public MagApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MagApplication getInstance() {
        return mInstance;
    }

    public static MagApplication get(Context context) {
        return (MagApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    /*
    * Testing usage
    * Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        applicationComponent = applicationComponent;
    }*/
}

