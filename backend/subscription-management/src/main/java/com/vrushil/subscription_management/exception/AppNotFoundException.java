package com.vrushil.subscription_management.exception;

public class AppNotFoundException extends RuntimeException{
    public AppNotFoundException(String message){
        super(message);
    }
}