package com.exampleapps.mapcontacts;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.exampleapps.mapcontacts.data.DataManager;
import com.exampleapps.mapcontacts.di.component.ApplicationComponent;
import com.exampleapps.mapcontacts.di.component.DaggerApplicationComponent;
import com.exampleapps.mapcontacts.di.module.ActivityModule;
import com.exampleapps.mapcontacts.di.module.ApplicationModule;

import javax.inject.Inject;

import timber.log.Timber;

public class MapContactsApp extends Application {

    @Inject
    DataManager mDataManager;

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        AndroidNetworking.initialize(getApplicationContext());
        if (BuildConfig.DEBUG){
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

            Timber.plant(new Timber.DebugTree(){
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return String.format("%s:%s",
                            super.createStackElementTag(element),
                            element.getLineNumber());
                }
            });
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent){
        mApplicationComponent = applicationComponent;
    }
}
