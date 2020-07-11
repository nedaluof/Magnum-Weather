package com.nedaluof.magweath.di.components;

import com.nedaluof.magweath.di.PerActivity;
import com.nedaluof.magweath.di.modules.ActivityModule;
import com.nedaluof.magweath.ui.main.MainActivity;

import dagger.Subcomponent;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
}
