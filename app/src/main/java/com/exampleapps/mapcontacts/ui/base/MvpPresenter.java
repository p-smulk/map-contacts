package com.exampleapps.mapcontacts.ui.base;

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

   // void handleApiError();

   // void setUserAsLoggedOut();
}
