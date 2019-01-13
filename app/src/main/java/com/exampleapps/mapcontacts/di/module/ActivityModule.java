package com.exampleapps.mapcontacts.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.exampleapps.mapcontacts.data.contentResolver.AppContactsProvider;
import com.exampleapps.mapcontacts.data.contentResolver.ContactsProvider;
import com.exampleapps.mapcontacts.data.location.AppLocationProvider;
import com.exampleapps.mapcontacts.data.location.LocationProvider;
import com.exampleapps.mapcontacts.di.ActivityContext;
import com.exampleapps.mapcontacts.di.PerActivity;
import com.exampleapps.mapcontacts.ui.main.MainContract;
import com.exampleapps.mapcontacts.ui.main.MainPresenter;
import com.exampleapps.mapcontacts.ui.main.map.MapContract;
import com.exampleapps.mapcontacts.ui.main.map.MapPresenter;
import com.exampleapps.mapcontacts.utils.rx.AppSchedulerProvider;
import com.exampleapps.mapcontacts.utils.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity){
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext(){
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity(){
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable(){
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider(){
        return new AppSchedulerProvider();
    }

    @Provides
    ContactsProvider provideContactsLoader(AppContactsProvider appContactsLoader){
        return appContactsLoader;
    }

    @Provides
    @PerActivity
    MainContract.Presenter<MainContract.View> provideMainPresenter(
            MainPresenter<MainContract.View> presenter){
        return presenter;
    }

    @Provides
    MapContract.Presenter<MapContract.View> provideMapPresenter(
            MapPresenter<MapContract.View> presenter){
        return presenter;
    }

    @Provides
    LocationProvider provideLocationProvider(AppLocationProvider appLocationProvider){
        return appLocationProvider;
    }


}
