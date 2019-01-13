package com.exampleapps.mapcontacts.data.contentResolver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.util.LongSparseArray;

import com.exampleapps.mapcontacts.di.ActivityContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import timber.log.Timber;

public class AppContactsProvider implements ContactsProvider {

    private final Context mContext;

    @Inject
    public AppContactsProvider(@ActivityContext Context context){
        mContext = context;
    }

    private List<ContactObject> getContacts(){
        List<ContactObject> contacts = new ArrayList<>();
        LongSparseArray<ContactObject> contactsArray = new LongSparseArray<>();

        ContentResolver cr = mContext.getContentResolver();
        String[] PROJECTION = new String[] {
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.Data.MIMETYPE,
                ContactsContract.Data.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                ContactsContract.Data.PHOTO_URI,
                ContactsContract.Data.PHOTO_THUMBNAIL_URI};

        String filter = ContactsContract.CommonDataKinds.StructuredPostal.DATA + " NOT LIKE ''";
        String order = ContactsContract.Data.CONTACT_ID;

        Cursor mainCursor = cr.query(ContactsContract.Data.CONTENT_URI, PROJECTION, filter, null, order);

        if ((mainCursor != null ? mainCursor.getCount() : 0) > 0){
            if (mainCursor.moveToFirst()){
                do {
                    String mimetype="", name="", number="", photo="", thumbnail="", street="", city="";

                    long id = mainCursor.getLong(mainCursor.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                    mimetype = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.Data.MIMETYPE));

                    ContactObject contact = contactsArray.get(id, null);

                    if (contact == null){
                        contact = new ContactObject(id);
                        name = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                        photo = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.Data.PHOTO_URI));
                        thumbnail = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI));

                        contact.setDisplayName(name);
                        contact.setPhoto(photo);
                        contact.setThumbnail(thumbnail);
                        contactsArray.put(id, contact);
                    }

                    switch (mimetype){
                        case ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE: {
                            number = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contact.setPhoneNumber(number);
                            break;
                        }
                        case ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE: {
                            street = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                            city = mainCursor.getString(mainCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                            contact.setStreet(street);
                            contact.setCity(city);
                            break;
                        }
                    }

                    Timber.i("id = " + id + "; name = " + name + "; number = " + number + "; street = " + street + "; city = " + city + "; thumbnail = " + thumbnail);

                } while (mainCursor.moveToNext());
            }

            mainCursor.close();
        }

        for (int i = 0; i < contactsArray.size(); i++){
            ContactObject contactObject = contactsArray.valueAt(i);
            Helper.verifyAndAddContact(contacts, contactObject);
        }

        return contacts;
    }

    @Override
    public Single<List<ContactObject>> getContactsInSingleList() {
        List<ContactObject> contacts = getContacts();

        return Single.create(
                emitter -> {

                    if (!contacts.isEmpty()) {
                        emitter.onSuccess(contacts);
                    } else {
                        Timber.i("No contacts!!!");
                        emitter.onError(new NoContactsProvidedException());
                    }

                }
        );
    }

}
