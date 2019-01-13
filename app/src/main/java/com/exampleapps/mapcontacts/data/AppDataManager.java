package com.exampleapps.mapcontacts.data;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.exampleapps.mapcontacts.data.db.DbHelper;
import com.exampleapps.mapcontacts.data.db.model.Contact;
import com.exampleapps.mapcontacts.data.location.LocationProvider;
import com.exampleapps.mapcontacts.di.ApplicationContext;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final DbHelper mDbHelper;
    //private final LocationProvider mLocationProvider;

    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          DbHelper dbHelper
          //  ,LocationProvider locationProvider
    ){
        mContext = context;
        mDbHelper = dbHelper;
        //mLocationProvider = locationProvider;
    }

    /**
     * IMPL DbHelper
     */

    @Override
    public void addContacts(List<Contact> contactList) {
        mDbHelper.addContacts(contactList);
    }

    @Override
    public List<Contact> getContacts() {
        return mDbHelper.getContacts();
    }

    @Override
    public void deleteContacts() {
        mDbHelper.deleteContacts();
    }

    @Override
    public boolean hasContacts() {
        return mDbHelper.hasContacts();
    }


    /**
     * IMPL LocationProvider
     */

//    @NonNull
//    @Override
//    public Single<Location> getCurrentLocation() {
//        return mLocationProvider.getCurrentLocation();
//    }
//
//    @Override
//    public void stopLocation() {
//        mLocationProvider.stopLocation();
//    }
}
