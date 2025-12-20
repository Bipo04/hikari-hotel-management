package org.web.hikarihotelmanagement.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
