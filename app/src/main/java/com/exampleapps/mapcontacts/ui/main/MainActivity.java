package com.exampleapps.mapcontacts.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.exampleapps.mapcontacts.R;
import com.exampleapps.mapcontacts.ui.base.BaseActivity;
import com.exampleapps.mapcontacts.ui.main.ImportAlertDialog.AlertDialogResult;
import com.exampleapps.mapcontacts.ui.main.map.MapFragment;
import com.exampleapps.mapcontacts.utils.AppConstants;
import com.exampleapps.mapcontacts.utils.PermissionUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainContract.View{

    public static final String TAG = "MainActivity";

    private static final int LOCATION_REQUEST_CODE = PermissionUtils.LOCATION_REQUEST_CODE;
    private static final int READ_CONTACTS_REQUEST_CODE = PermissionUtils.READ_CONTACTS_REQUEST_CODE;
    private static final String READ_CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS;
    private static final int REQUEST_CHECK_SETTINGS = AppConstants.REQUEST_CHECK_SETTINGS;

    @Inject
    MainContract.Presenter<MainContract.View> mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private Callback mCallback;

    public interface Callback {
        void updateMarkers();
        void deleteMarkers();
        void startLocationFromOnActivityResult();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();

        if (savedInstanceState == null){
            openMapFragment();
        }
    }

    @Override
    protected void setUp() {
        setSupportActionBar(mToolbar);
        if (!mPresenter.hasContacts()) {
            if (PermissionUtils.hasPermission(this, READ_CONTACTS_PERMISSION)) {
                showImportAlertDialog(R.string.import_alert_empty_db);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS_PERMISSION}, READ_CONTACTS_REQUEST_CODE);
            }
        }
        mPresenter.onViewInitialized();
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public void openMapFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .disallowAddToBackStack()
                .replace(R.id.frame, MapFragment.newInstance(), MapFragment.TAG)
                .commit();
    }

    @Override
    public void showGenericError() {
        showMessage(R.string.error_generic);
    }

    @Override
    public void showNoContactsProvided() {
        showMessage(R.string.error_accessing_contacts);
    }

    @Override
    public void showContactsProvidedMsg() {
        showMessage("Contacts have been imported to application.");
    }

    @Override
    public void showCustomError(int resId) {
        showMessage(resId);
    }

    @Override
    public void showImportAlertDialog(int resId) {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putInt("import_alert_db_status", resId);
        ImportAlertDialog importAlertDialog = ImportAlertDialog.newInstance(bundle);
        importAlertDialog.show(fm, ImportAlertDialog.TAG);
    }

    @Override
    public void importAlertDialogResponse(AlertDialogResult clickedResponse) {
        switch (clickedResponse) {
            case OK:
                mPresenter.getContacts();
                break;
            case CANCEL_EMPTY_DB:
                showMessage("You need to import contacts from your phone for this app to be functional!");
                break;
            case CANCEL_HAS_DB:

                break;
            default:
                Timber.i("Unexpected response in importAlertDialogResponse()");
        }
    }

    @Override
    public void updateMarkersInMapFragment() {
        mCallback.updateMarkers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_import:
                if (PermissionUtils.hasPermission(this, READ_CONTACTS_PERMISSION)){
                    mPresenter.importClick();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{READ_CONTACTS_PERMISSION}, READ_CONTACTS_REQUEST_CODE);
                }
                return true;
            case R.id.action_delete:
                mPresenter.deleteDb();
                mCallback.deleteMarkers();
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case READ_CONTACTS_REQUEST_CODE: {
                if (PermissionUtils.hasAllPermissionsGranted(grantResults)) {
                    mPresenter.importClick();
                } else {
                    showMessage(R.string.permission_contacts_denied_explanation);
                }
            }
            case LOCATION_REQUEST_CODE: {
                if (PermissionUtils.hasAllPermissionsGranted(grantResults)){
                    mCallback.startLocationFromOnActivityResult();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        Timber.i("User agreed to make required location settings changes.");
                        mCallback.startLocationFromOnActivityResult();
                        break;
                    case Activity.RESULT_CANCELED:
                        Timber.i("User chose not to make required location settings changes.");
                        break;
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
   //     mPresenter.deleteDb();
        mCallback = null;
    }
}
