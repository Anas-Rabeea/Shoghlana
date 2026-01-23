package com.shoghlana.backend.common.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{

    @ExceptionHandler(BuisenessException.class)
    public ResponseEntity<ErrorResponse> handleBuisnessException(
            BuisenessException ex,
            HttpServletRequest request
    )
    {

        log.warn(
                "Business Error: {} ,Path: {} - {} at {} ".formatted(
                        ex.getErrorCode(),
                        request.getRequestURI(),
                        ex.getMessage(),
                        Instant.now()
                ));

        return ResponseEntity
                .status(ex.getStatus())
                .body(ErrorResponse
                        .builder()
                        .status(ex.getStatus())
                        .code(ex.getErrorCode().name())
                        .path(request.getRequestURI())
                        .message(ex.getMessage())
                        .build());
    }






}
