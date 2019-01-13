package com.exampleapps.mapcontacts.data.contentResolver;

import com.exampleapps.mapcontacts.data.db.model.Contact;

import java.util.List;

import io.reactivex.Single;

public interface ContactsProvider {

    Single<List<ContactObject>> getContactsInSingleList();

}
