package com.vrushil.subscription_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException
            (UserNotFoundException userNotFoundException, WebRequest request){
        ErrorDetails errorDetails= new ErrorDetails(
                LocalDateTime.now(),
                userNotFoundException.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<?> handleDuplicateUserException(DuplicateUserException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT); // 409 Conflict
    }

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<?> handleSubscriptionNotFoundException(SubscriptionNotFoundException subscriptionNotFoundException,WebRequest request){
        ErrorDetails errorDetails= new ErrorDetails(
                LocalDateTime.now(),
                subscriptionNotFoundException.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<?> handleCategoryNotFoundException(CategoryNotFoundException categoryNotFoundException, WebRequest request){
        ErrorDetails errorDetails= new ErrorDetails(
                LocalDateTime.now(),
                categoryNotFoundException.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AppNotFoundException.class)
    public ResponseEntity<?> handleAppNotFoundException(AppNotFoundException appNotFoundException, WebRequest request){
        ErrorDetails errorDetails= new ErrorDetails(
                LocalDateTime.now(),
                appNotFoundException.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<?> handlePlanNotFoundException(PlanNotFoundException planNotFoundException, WebRequest request){
        ErrorDetails errorDetails= new ErrorDetails(
                LocalDateTime.now(),
                planNotFoundException.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RenewalNotFoundException.class)
    public ResponseEntity<?> handleRenewalNotFoundException
            (RenewalNotFoundException renewalNotFoundException, WebRequest request){
        ErrorDetails errorDetails= new ErrorDetails(
                LocalDateTime.now(),
                renewalNotFoundException.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyExistsException(AlreadyExistsException alreadyExistsException, WebRequest request){
        ErrorDetails errorDetails= new ErrorDetails(
                LocalDateTime.now(),
                alreadyExistsException.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);
    }
}
