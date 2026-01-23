package com.shoghlana.backend.common.exception;

import org.springframework.http.HttpStatus;

public class BuisenessException extends RuntimeException {

   private final ErrorCodes errorCode;
   private final HttpStatus status;

    public BuisenessException(String message, ErrorCodes errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public ErrorCodes getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
