package com.sivalabs.devzone.config;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.sivalabs.devzone.common.exceptions.BadRequestException;
import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.common.exceptions.ResourceAlreadyExistsException;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import com.sivalabs.devzone.common.exceptions.UnauthorisedAccessException;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handle(ResourceNotFoundException e) {
        return createProblemDetail(NOT_FOUND, "Resource Not Found", e.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    ProblemDetail handle(ResourceAlreadyExistsException e) {
        return createProblemDetail(CONFLICT, "Resource Already Exists", e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    ProblemDetail handle(BadRequestException e) {
        return createProblemDetail(BAD_REQUEST, "Bad Request", e.getMessage());
    }

    @ExceptionHandler(UnauthorisedAccessException.class)
    ProblemDetail handle(UnauthorisedAccessException e) {
        return createProblemDetail(FORBIDDEN, "Forbidden", e.getMessage());
    }

    @ExceptionHandler(DevZoneException.class)
    ProblemDetail handleGenericException(DevZoneException e) {
        return createProblemDetail(INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}
