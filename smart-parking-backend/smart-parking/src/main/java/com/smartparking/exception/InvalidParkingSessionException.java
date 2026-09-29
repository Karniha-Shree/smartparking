package com.smartparking.exception;

public class InvalidParkingSessionException extends RuntimeException {
    public InvalidParkingSessionException(String message) {
        super(message);
    }
}
