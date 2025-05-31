package com.vrushil.subscription_management.exception;

public class SubscriptionNotFoundException extends RuntimeException{

    public SubscriptionNotFoundException(String message){
        super(message);
    }
}
