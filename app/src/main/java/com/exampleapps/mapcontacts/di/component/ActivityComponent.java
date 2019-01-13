package com.exampleapps.mapcontacts.di.component;

import com.exampleapps.mapcontacts.di.PerActivity;
import com.exampleapps.mapcontacts.di.module.ActivityModule;
import com.exampleapps.mapcontacts.ui.main.MainActivity;
import com.exampleapps.mapcontacts.ui.main.map.MapFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(MapFragment fragment);


}
