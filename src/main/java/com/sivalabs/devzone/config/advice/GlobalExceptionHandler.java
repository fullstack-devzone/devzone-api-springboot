package com.sivalabs.devzone.config.advice;

import com.sivalabs.devzone.common.exceptions.BadRequestException;
import com.sivalabs.devzone.common.exceptions.DevZoneException;
import com.sivalabs.devzone.common.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

// See Bug: https://github.com/zalando/problem-spring-web/issues/707

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling, SecurityAdviceTrait {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Problem> handleResourceNotFoundException(
            ResourceNotFoundException exception, NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return create(Status.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(DevZoneException.class)
    ResponseEntity<Problem> handleGeeksClubException(
            DevZoneException exception, NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return create(Status.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<Problem> handleBadRequestException(
            BadRequestException exception, NativeWebRequest request) {
        log.error(exception.getLocalizedMessage(), exception);
        return create(Status.BAD_REQUEST, exception, request);
    }
}
