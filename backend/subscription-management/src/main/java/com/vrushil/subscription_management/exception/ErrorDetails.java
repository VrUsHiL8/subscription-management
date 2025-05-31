package com.vrushil.subscription_management.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorDetails {
    private LocalDateTime timeStamp = LocalDateTime.now();
    private String message;
    private String details;
}
