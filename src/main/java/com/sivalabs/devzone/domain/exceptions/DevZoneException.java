package com.sivalabs.devzone.domain.exceptions;

public class DevZoneException extends RuntimeException {
    public DevZoneException(String message) {
        super(message);
    }

    public DevZoneException(String message, Exception e) {
        super(message, e);
    }
}
