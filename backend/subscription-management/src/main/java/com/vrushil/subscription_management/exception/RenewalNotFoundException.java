package com.vrushil.subscription_management.exception;

public class RenewalNotFoundException extends RuntimeException{
    public RenewalNotFoundException(String message){
        super(message);
    }
}
