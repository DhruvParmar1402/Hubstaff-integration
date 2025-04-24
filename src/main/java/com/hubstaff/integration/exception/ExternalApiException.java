package com.hubstaff.integration.exception;

public class ExternalApiException extends RuntimeException {
    private final int statusCode;

    public ExternalApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
