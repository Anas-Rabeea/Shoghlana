package com.shoghlana.backend.common.exception.exceptions;

import com.shoghlana.backend.common.exception.BuisenessException;
import com.shoghlana.backend.common.exception.ErrorCodes;
import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends BuisenessException {

    public UnAuthorizedException(String message, ErrorCodes errorCode, HttpStatus status) {
        super(
                "UnAuthorized Resource",
                ErrorCodes.UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED
        );
    }
}
