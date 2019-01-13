package com.exampleapps.mapcontacts.di.component;

import android.app.Application;
import android.content.Context;

import com.exampleapps.mapcontacts.MapContactsApp;
import com.exampleapps.mapcontacts.data.DataManager;
import com.exampleapps.mapcontacts.di.ApplicationContext;
import com.exampleapps.mapcontacts.di.module.ActivityModule;
import com.exampleapps.mapcontacts.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MapContactsApp app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();

}
