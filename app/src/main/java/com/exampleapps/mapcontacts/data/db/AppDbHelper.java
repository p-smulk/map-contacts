package com.exampleapps.mapcontacts.data.db;

import com.exampleapps.mapcontacts.data.db.model.Contact;
import com.exampleapps.mapcontacts.data.db.model.DaoMaster;
import com.exampleapps.mapcontacts.data.db.model.DaoSession;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class AppDbHelper implements DbHelper {

    private final DaoSession mDaoSession;

    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper){
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public void addContacts(List<Contact> contactList) {
        mDaoSession.getContactDao().insertInTx(contactList);
        Timber.i("Added " + contactList.size() + " contacts!");
    }

    @Override
    public List<Contact> getContacts() {
        return mDaoSession.getContactDao().loadAll();
    }

    @Override
    public void deleteContacts() {
        mDaoSession.getContactDao().deleteAll();
    }

    @Override
    public boolean hasContacts() {
        return mDaoSession.getContactDao().count() > 0;
    }

}
