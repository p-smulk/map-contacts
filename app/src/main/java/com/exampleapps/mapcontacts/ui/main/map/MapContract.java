package com.exampleapps.mapcontacts.ui.main.map;

import android.location.Location;

import com.exampleapps.mapcontacts.data.db.model.Contact;
import com.exampleapps.mapcontacts.ui.base.MvpPresenter;
import com.exampleapps.mapcontacts.ui.base.MvpView;

import java.util.List;

public interface MapContract {

    interface View extends MvpView {

        void animateMapCamera(Location location);

        void showNoLocationAvailable();

        void showGenericError();

        void addMapMarkers(List<Contact> contacts);

    }

    interface Presenter<V extends View> extends MvpPresenter<V> {

        void setupMap();

        void centerMapOnLocation();

        void getContactsFromDb();

    }

}
