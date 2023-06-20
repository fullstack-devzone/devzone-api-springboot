package com.sivalabs.devzone.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.sivalabs.devzone.common.exceptions.BadRequestException;
import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create("https://api.devzone.com/errors/not-found"));
        return problemDetail;
    }

    @ExceptionHandler(DevZoneException.class)
    ProblemDetail handleDevZoneException(DevZoneException e) {
        log.error(e.getLocalizedMessage(), e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, e.getMessage());
        problemDetail.setTitle("Unknown Problem");
        problemDetail.setType(URI.create("https://api.devzone.com/errors/unknown"));
        return problemDetail;
    }

    @ExceptionHandler(BadRequestException.class)
    ProblemDetail handleBadRequestException(BadRequestException e) {
        log.error(e.getLocalizedMessage(), e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Unknown Problem");
        problemDetail.setType(URI.create("https://api.devzone.com/errors/badrequest"));
        return problemDetail;
    }

    @ExceptionHandler(UnauthorisedAccessException.class)
    ProblemDetail handleUnauthorisedAccessException(UnauthorisedAccessException e) {
        log.error(e.getLocalizedMessage(), e);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(FORBIDDEN, e.getMessage());
        problemDetail.setTitle("Forbidden");
        problemDetail.setType(URI.create("https://api.devzone.com/errors/forbidden"));
        return problemDetail;
    }
}
