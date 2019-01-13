package com.exampleapps.mapcontacts.ui.main.map;

import android.Manifest;
import android.support.annotation.RequiresPermission;

import com.exampleapps.mapcontacts.R;
import com.exampleapps.mapcontacts.data.DataManager;
import com.exampleapps.mapcontacts.data.db.model.Contact;
import com.exampleapps.mapcontacts.data.location.LocationProvider;
import com.exampleapps.mapcontacts.data.location.NoLocationAvailableException;
import com.exampleapps.mapcontacts.data.location.LocationSettingsException;
import com.exampleapps.mapcontacts.ui.base.BasePresenter;
import com.exampleapps.mapcontacts.utils.rx.SchedulerProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class MapPresenter<V extends MapContract.View> extends BasePresenter<V>
        implements MapContract.Presenter<V> {

    @Inject
    public LocationProvider mLocationProvider;

    @Inject
    public MapPresenter(DataManager dataManager,
                        SchedulerProvider schedulerProvider,
                        CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void setupMap() {
        Timber.i("Opened setupMap()!!!");
        if (getDataManager().hasContacts()){
            getContactsFromDb();
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    @Override
    public void centerMapOnLocation() {
        if (!isViewAttached()){
            return;
        } else {
            getMvpView().showLoading(R.string.loading_location);
        }

        getCompositeDisposable().add(
                mLocationProvider.getCurrentLocation()
                        .timeout(10, TimeUnit.SECONDS)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .subscribe(location -> {
                            if (isViewAttached()){
                                getMvpView().hideLoading();
                                getMvpView().animateMapCamera(location);
                                mLocationProvider.stopLocation();
                                Timber.i("Location -> latitude:" + location.getLatitude()
                                        + ", longitude:" + location.getLongitude());
                            }
                        }, throwable -> {
                            if (isViewAttached()) {
                                getMvpView().hideLoading();
                                Timber.e(throwable, "Error fetching location!");
                                mLocationProvider.stopLocation();
                                if (throwable instanceof LocationSettingsException){

                                } else if (throwable instanceof NoLocationAvailableException){
                                    getMvpView().showNoLocationAvailable();
                                } else {
                                    getMvpView().showGenericError();
                                }
                            }
                        })
        );
    }

    @Override
    public void getContactsFromDb() {
        List<Contact> contacts = getDataManager().getContacts();
        getMvpView().addMapMarkers(contacts);
    }
}
