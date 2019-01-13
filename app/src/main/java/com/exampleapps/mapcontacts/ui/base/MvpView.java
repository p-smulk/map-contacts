package com.exampleapps.mapcontacts.ui.base;

import android.support.annotation.StringRes;

public interface MvpView {

    void showLoading(int resId);

    void hideLoading();

    void onError(@StringRes int resId);

    void onError(String message);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();
}
