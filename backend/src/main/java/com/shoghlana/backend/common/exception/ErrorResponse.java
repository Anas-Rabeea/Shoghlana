package com.shoghlana.backend.common.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;
@Builder
public record ErrorResponse(
        String code,
        String message,
        HttpStatus status,
        String path,
        Instant timestamp,
        Map<String, String> details
)
{
    public static ErrorResponse of(
            String code,
            String message,
            HttpStatus status,
            String path,
            Map<String, String> details
    )
    {
        return new ErrorResponse(code,message,status,path,Instant.now(),details) ;
    }


}
