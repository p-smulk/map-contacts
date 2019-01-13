package com.exampleapps.mapcontacts.data.contentResolver;

import android.os.Build;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    private final static String COUNTRY_CODE = "PL";

    public static boolean verifyAndAddContact(ContactObject contact){
        return contact.street != null && !contact.street.isEmpty()
                && !TextUtils.isEmpty(contact.displayName);
    }

    public static void verifyAndAddContact(List<ContactObject> contacts, ContactObject contact){
        if (verifyAndAddContact(contact)){
            contacts.add(contact);
        }
    }

    public static String getFormattedNumber(String number){
        number = PhoneNumberUtils.formatNumber(number, COUNTRY_CODE).replaceAll("\\s+", "");
        return number;
    }

    public static boolean verifyPhone(ArrayList<String> phoneNumbers, String phone){
        return !TextUtils.isEmpty(phone) &&
                Patterns.PHONE.matcher(phone).matches() &&
                isUniquePhoneNumber(phoneNumbers, phone);
    }

    public static boolean isUniquePhoneNumber(ArrayList<String> phoneNumbers, String phone){
        if (phoneNumbers != null){
            for (int i = 0; i < phoneNumbers.size(); i++){
                if (matchPhoneNumber(phoneNumbers.get(i), phone, null)){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean matchPhoneNumber(String listPhone, String singlePhone, String countryCode){
        if (countryCode == null) countryCode = "";
        if (listPhone.equals(singlePhone)) return true;
        listPhone = listPhone.replace(countryCode, "");
        if (listPhone.equals(singlePhone)) return true;
        if (listPhone.length() <= singlePhone.length()){
            singlePhone = singlePhone.substring(singlePhone.length() - listPhone.length());
            return singlePhone.equals(listPhone);
        } else {
            listPhone = listPhone.substring(listPhone.length() - singlePhone.length());
            return singlePhone.equals(listPhone);
        }
    }

}
