package com.exampleapps.mapcontacts.data.db.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "contacts")
public class Contact {

    @Id(autoincrement = true)
    private Long ID;

    @Property
    private long id;

    @Property
    private String displayName;

    @Property
    private String phoneNumber;

    @Property
    private String street;

    @Property
    private String city;

    @Property
    private String country;

    @Property
    private String profilePhotoUrl;

    @Property
    private String thumbnailUrl;

    @Property
    private double latitude;

    @Property
    private double longitude;

    @Generated(hash = 303902779)
    public Contact(Long ID, long id, String displayName, String phoneNumber,
            String street, String city, String country, String profilePhotoUrl,
            String thumbnailUrl, double latitude, double longitude) {
        this.ID = ID;
        this.id = id;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.country = country;
        this.profilePhotoUrl = profilePhotoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }

    public Long getID() {
        return this.ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        if (this.phoneNumber == null){
            return "Unknown";
        }
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfilePhotoUrl() {
        return this.profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    


}


