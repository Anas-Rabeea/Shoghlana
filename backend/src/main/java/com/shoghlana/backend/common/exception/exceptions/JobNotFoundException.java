package com.shoghlana.backend.common.exception.exceptions;

import com.shoghlana.backend.common.exception.BuisenessException;
import com.shoghlana.backend.common.exception.ErrorCodes;
import org.springframework.http.HttpStatus;

public class JobNotFoundException extends BuisenessException {


    public JobNotFoundException(int jobId)
    {
        super(
                "Job with id {} Not Found".formatted(jobId) ,
                ErrorCodes.JOB_NOT_FOUND,
                HttpStatus.NOT_FOUND);
    }


}
