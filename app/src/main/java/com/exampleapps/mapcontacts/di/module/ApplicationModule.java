package com.exampleapps.mapcontacts.di.module;

import android.app.Application;
import android.content.Context;

import com.exampleapps.mapcontacts.data.AppDataManager;
import com.exampleapps.mapcontacts.data.DataManager;
import com.exampleapps.mapcontacts.data.db.AppDbHelper;
import com.exampleapps.mapcontacts.data.db.DbHelper;
import com.exampleapps.mapcontacts.data.location.AppLocationProvider;
import com.exampleapps.mapcontacts.data.location.LocationProvider;
import com.exampleapps.mapcontacts.di.ApplicationContext;
import com.exampleapps.mapcontacts.di.DatabaseInfo;
import com.exampleapps.mapcontacts.di.PreferenceInfo;
import com.exampleapps.mapcontacts.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application){
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext(){
        return mApplication;
    }

    @Provides
    Application provideApplication(){
        return mApplication;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName(){
        return AppConstants.DB_NAME;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName(){
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager){
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper){
        return appDbHelper;
    }

//    @Provides
//    LocationProvider provideLocationProvider(AppLocationProvider appLocationProvider){
//        return appLocationProvider;
//    }

}
