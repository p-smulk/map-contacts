package com.exampleapps.mapcontacts.data.contentResolver;

import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;

public class ContactObject {

    public final long id;
    public String displayName;
    public String photo;
    public String thumbnail;
    public ArrayList<String> phoneNumbers = new ArrayList<>();
    public String street;
    public String city;

    ContactObject(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if (displayName != null && !displayName.isEmpty()) {
            this.displayName = displayName;
        }
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        if (photo != null && !photo.isEmpty()) {
            this.photo = photo;
        }
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        if (thumbnail != null && !thumbnail.isEmpty()) {
            this.thumbnail = thumbnail;
        }
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public boolean hasPhoneNumber(){
        return phoneNumbers.size() > 0;
    }

    public void setPhoneNumber(String phoneNumber){
        if (!TextUtils.isEmpty(phoneNumber)){
            phoneNumber = Helper.getFormattedNumber(phoneNumber);
            if (Helper.verifyPhone(this.phoneNumbers, phoneNumber)){
                this.phoneNumbers.add(phoneNumber);
            }
        }
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        for (String phone : phoneNumbers){
            if (!TextUtils.isEmpty(phone)){
                phone = Helper.getFormattedNumber(phone);
                if (Helper.verifyPhone(phoneNumbers, phone)) {
                    this.phoneNumbers.add(phone);
                }
            }
        }
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (street != null && !street.isEmpty()) {
            this.street = street;
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city != null && !city.isEmpty()) {
            this.city = city;
        }
    }
}
