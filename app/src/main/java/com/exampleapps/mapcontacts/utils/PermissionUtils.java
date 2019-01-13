package com.exampleapps.mapcontacts.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    public static final int LOCATION_REQUEST_CODE = 0;
    public static final int READ_CONTACTS_REQUEST_CODE = 1;

    private PermissionUtils() {
        throw new UnsupportedOperationException("No Instantiation!");
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull String permission){
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean hasAllPermissionsGranted(@NonNull int[] grantResults){
        // If the request is cancelled the results array will be empty
        if (isPermissionRequestCancelled(grantResults)){
            return false;
        }

        for (int grantResult : grantResults){
            if (grantResult == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }

        return true;
    }

    public static boolean isPermissionRequestCancelled(@NonNull int[] grantResults){
        return grantResults.length == 0;
    }
}
