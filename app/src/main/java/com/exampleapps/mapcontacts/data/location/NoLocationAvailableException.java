package com.exampleapps.mapcontacts.data.location;

public class NoLocationAvailableException extends Throwable {

    public NoLocationAvailableException(String message){
        super(message);
    }

    public NoLocationAvailableException(){

    }
}
