package com.exampleapps.mapcontacts.ui.main;

import android.support.annotation.StringRes;

import com.exampleapps.mapcontacts.ui.base.MvpPresenter;
import com.exampleapps.mapcontacts.ui.base.MvpView;
import com.exampleapps.mapcontacts.ui.main.ImportAlertDialog.AlertDialogResult;

public interface MainContract {

    interface View extends MvpView{

        void openMapFragment();

        void showGenericError();

        void showNoContactsProvided();

        void showContactsProvidedMsg();

        void showCustomError(@StringRes int resId);

        void showImportAlertDialog(@StringRes int resId);

        void importAlertDialogResponse(AlertDialogResult clickedResponse);

        void updateMarkersInMapFragment();

    }

    interface Presenter<V extends View> extends MvpPresenter<V> {

        void onViewInitialized();

        void importClick();

        void getContacts();

        void deleteDb();

        boolean hasContacts();

    }

}
