package com.exampleapps.mapcontacts.ui.main;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.RequiresPermission;

import com.exampleapps.mapcontacts.R;
import com.exampleapps.mapcontacts.data.DataManager;
import com.exampleapps.mapcontacts.data.contentResolver.ContactObject;
import com.exampleapps.mapcontacts.data.contentResolver.ContactsProvider;
import com.exampleapps.mapcontacts.data.contentResolver.NoContactsProvidedException;
import com.exampleapps.mapcontacts.data.db.model.Contact;
import com.exampleapps.mapcontacts.di.ActivityContext;
import com.exampleapps.mapcontacts.ui.base.BasePresenter;
import com.exampleapps.mapcontacts.utils.PermissionUtils;
import com.exampleapps.mapcontacts.utils.rx.SchedulerProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class MainPresenter<V extends MainContract.View> extends BasePresenter<V>
        implements MainContract.Presenter<V> {

    private ContactsProvider mContactsProvider;
    private final Context mContext;

    @Inject
    public MainPresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable,
                         ContactsProvider contactsProvider,
                         @ActivityContext Context context) {
        super(dataManager, schedulerProvider, compositeDisposable);
        mContactsProvider = contactsProvider;
        mContext = context;
    }

    @Override
    public void onViewInitialized() {
//        if (!getDataManager().hasContacts()){
//            getMvpView().showImportAlertDialog(R.string.import_alert_empty_db);
//        }
    }

    @Override
    public void importClick() {
        if (!getDataManager().hasContacts()){
            getMvpView().showImportAlertDialog(R.string.import_alert_empty_db);
        } else {
            getMvpView().showImportAlertDialog(R.string.import_alert_existing_db);
        }
    }

    @RequiresPermission(Manifest.permission.READ_CONTACTS)
    @Override
    public void getContacts(){
        if (!isViewAttached()){
            return;
        } else {
            getMvpView().showLoading(R.string.loading_contacts);
        }

        getCompositeDisposable().add(
                mContactsProvider.getContactsInSingleList()
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
//                        .doOnSubscribe(__ -> getMvpView().showLoading(R.string.loading_contacts))
//                        .doAfterTerminate(() -> getMvpView().hideLoading())
                        .subscribe(
                                contacts -> {
                                    if (isViewAttached()){
                                        List<Contact> contactsDb = new ArrayList<>();
                                        for (ContactObject contact : contacts){

                                            Contact dbContact = new Contact();
                                            dbContact.setId(contact.getId());
                                            dbContact.setDisplayName(contact.getDisplayName());
                                            if (contact.hasPhoneNumber()) {
                                                dbContact.setPhoneNumber(contact.getPhoneNumbers().get(0));
                                            }
                                            dbContact.setStreet(contact.getStreet());
                                            dbContact.setCity(contact.getCity());
                                            dbContact.setProfilePhotoUrl(contact.getPhoto());
                                            dbContact.setThumbnailUrl(contact.getThumbnail());

                                            Address address = getAddressCoordinates(contact.getStreet());
                                            if (address != null){
                                                dbContact.setLatitude(address.getLatitude());
                                                dbContact.setLongitude(address.getLongitude());
                                                contactsDb.add(dbContact);
                                            }

//                                            Timber.i("name = " + contact.getDisplayName() + "\naddress = "
//                                                    + contact.getStreet() + "\nphone = " + contact.getPhoneNumbers()
//                                                    + "\nphone.size() = " + contact.getPhoneNumbers().size());
//                                            Timber.i("nameDb = " + dbContact.getDisplayName() + "\naddressDb = "
//                                                    + dbContact.getStreet() + "\nphoneDb = " + dbContact.getPhoneNumber()
//                                                    + "\nphoneDb.size() = " + contact.getPhoneNumbers().size()
//                                                    + "\nlatitudeDb = " + dbContact.getLatitude() + "\nlongitudeDb = " + dbContact.getLongitude());
//                                            Timber.i("contactsDb.size() = " + contactsDb.size());
                                        }

                                        getDataManager().addContacts(contactsDb);
                                        getMvpView().updateMarkersInMapFragment();

                                        getMvpView().showContactsProvidedMsg();
                                        getMvpView().hideLoading();
                                    }
                                },
                                throwable -> {
                                    if (isViewAttached()){
                                        getMvpView().hideLoading();
                                        Timber.e("Error while importing contacts, " + throwable);
                                        if (throwable instanceof NoContactsProvidedException){
                                            getMvpView().showNoContactsProvided();
                                        } else {
                                            getMvpView().showGenericError();
                                        }
                                    }
                                }
                        )
        );
    }

    private Address getAddressCoordinates(String street){
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        String errorMsg = "";
        Address address = null;

        try {
            addresses = geocoder.getFromLocationName(street, 1);
            if (addresses != null && !addresses.isEmpty()){
                address = addresses.get(0);
            } else {
                getMvpView().showCustomError(R.string.error_lack_of_coordinates);
            }
        } catch (IOException e){
            getMvpView().showCustomError(R.string.error_geocoder_unavailable);
            Timber.e(e);
        } catch (IllegalArgumentException e){
            getMvpView().showCustomError(R.string.error_incorrect_address);
            Timber.e(e);
        }

        return address;
    }

    @Override
    public void deleteDb() {
        if (getDataManager().hasContacts()) {
            getDataManager().deleteContacts();
            Timber.i("Deleted contacts from DB!");
        }
    }

    @Override
    public boolean hasContacts() {
        return getDataManager().hasContacts();
    }
}
