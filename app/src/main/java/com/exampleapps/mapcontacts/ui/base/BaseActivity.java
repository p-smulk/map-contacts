package com.exampleapps.mapcontacts.ui.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.exampleapps.mapcontacts.MapContactsApp;
import com.exampleapps.mapcontacts.R;
import com.exampleapps.mapcontacts.di.component.ActivityComponent;
import com.exampleapps.mapcontacts.di.component.DaggerActivityComponent;
import com.exampleapps.mapcontacts.di.module.ActivityModule;
import com.exampleapps.mapcontacts.utils.CommonUtils;
import com.exampleapps.mapcontacts.utils.NetworkUtils;

import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements MvpView, BaseFragment.Callback {

    private ProgressDialog mProgressDialog;

    private ActivityComponent mActivityComponent;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((MapContactsApp) getApplication()).getApplicationComponent())
                .build();
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    public void showLoading(int resId) {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this, resId);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.cancel();
        }
    }

    private void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public void onError(String message) {
        if (message != null){
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.some_error));
        }
    }

    @Override
    public void showMessage(String message) {
        if (message != null){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void setUnBinder(Unbinder unBinder){
        mUnbinder = unBinder;
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null){
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    protected abstract void setUp();
}
