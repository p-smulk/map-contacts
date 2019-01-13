package com.exampleapps.mapcontacts.utils;

import android.location.Location;
import android.support.annotation.NonNull;

public class LocationUtils {

    private LocationUtils(){
        throw new UnsupportedOperationException("No Instantiation!");
    }

    @NonNull
    public static Location buildLocation(double lat, double lon){
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);

        return location;
    }

}
