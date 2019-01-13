package com.exampleapps.mapcontacts.data.db;

import com.exampleapps.mapcontacts.data.db.model.Contact;

import java.util.List;

public interface DbHelper {

    void addContacts(List<Contact> contactList);

    List<Contact> getContacts();

    void deleteContacts();

    boolean hasContacts();

}
