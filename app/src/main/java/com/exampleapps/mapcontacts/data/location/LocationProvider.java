package com.exampleapps.mapcontacts.data.location;

import android.location.Location;
import android.support.annotation.NonNull;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface LocationProvider {

    @NonNull
    Single<Location> getCurrentLocation();

    void stopLocation();

}
