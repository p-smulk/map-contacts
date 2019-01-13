package com.exampleapps.mapcontacts.data.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.exampleapps.mapcontacts.di.ActivityContext;
import com.exampleapps.mapcontacts.utils.AppConstants;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Single;
import timber.log.Timber;

public class AppLocationProvider implements LocationProvider {

    private static final int LOCATION_FASTEST_INTERVAL_MILLIS = 250;
    private static final int LOCATION_INTERVAL_MILLIS = 1000*60;
    private static final int REQUEST_CHECK_SETTINGS = AppConstants.REQUEST_CHECK_SETTINGS;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private final Context mContext;
    private AppCompatActivity mActivity;

    @Inject
    public AppLocationProvider(@ActivityContext Context context, AppCompatActivity activity) {
        mContext = context;
        mActivity = activity;
    }

    private LocationRequest createLocationRequest(){
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(LOCATION_FASTEST_INTERVAL_MILLIS)
                .setNumUpdates(1);

        return locationRequest;
    }

    private LocationSettingsRequest buildLocationSettingsRequest(LocationRequest locationRequest){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        return builder.build();
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public Single<Location> getCurrentLocation() {
        return Single.create(emitter -> {

            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
            mSettingsClient = LocationServices.getSettingsClient(mContext);
            mLocationRequest = createLocationRequest();
            LocationSettingsRequest locationSettingsRequest = buildLocationSettingsRequest(mLocationRequest);

            mSettingsClient.checkLocationSettings(locationSettingsRequest)
                    .addOnSuccessListener(
                            locationSettingsResponse -> {
                                mFusedLocationProviderClient
                                        .requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                                Timber.i("Requesting location updates!");
                                })//;
                    .addOnFailureListener(e -> {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode){
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Timber.i("Location setting are not satisfied. Attempting to upgrade location settings.");
                                try {
                                    ResolvableApiException resolvable = (ResolvableApiException) e;
                                    emitter.onError(new LocationSettingsException());
                                    resolvable.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie){
                                    Timber.i("PendingIntent unable to execute request");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMsg = "Location settings are inadequate, and cannot be fixed here. Fix in Setting.";
                                Timber.e(errorMsg);
                                Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    });

            mLocationCallback = new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult == null){
                        return;
                    }

                    Location currentLocation = locationResult.getLastLocation();
                    if (currentLocation != null){
                        Timber.i("Location updated! location = " + currentLocation + "; location.date() = " + new Date(currentLocation.getTime()));
//                        if (mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
//                                .addOnCompleteListener(task -> {
//                                    mLocationCallback = null;
//                                    mFusedLocationProviderClient = null;
//                                    mLocationRequest = null;
//                                    Timber.i("Removed location updates");
//                                }).isSuccessful()) {
//                            emitter.onSuccess(currentLocation);
//                        }
                        emitter.onSuccess(currentLocation);
                    } else {
                        if (!emitter.isDisposed()) {
                            Timber.w("Location couldn't be found!!!");
                            emitter.onError(new NoLocationAvailableException());
                        }
                    }

                }
            };

//            emitter.setCancellable(() -> mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
//                    .addOnCompleteListener(task -> mLocationCallback = null));

        });
    }

    @Override
    public void stopLocation() {
        if (mFusedLocationProviderClient != null){
            Timber.i("mFusedLocationProviderClient is != null");
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                    .addOnSuccessListener(task -> {
                        Timber.i("LocationUpdates are stopped in onSuccess!!!");
                        mFusedLocationProviderClient = null;
                        mLocationRequest = null;
                        mLocationCallback = null;
             //           mActivity = null;
                    })
                    .addOnFailureListener(task -> Timber.i("LocationUpdates FAILED to stop in stopLocationUpdates!!!"));
        }
    }


}
