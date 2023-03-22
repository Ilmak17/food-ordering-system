package com.food.ordering.system.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ErrorDTO.builder()
                .message("Unexpected error")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
    }

    @ResponseBody
    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ValidationException e) {
        ErrorDTO.ErrorDTOBuilder builder = ErrorDTO.builder();

        if (e instanceof ConstraintViolationException) {
            String violations = extractViolationFromException((ConstraintViolationException) e);
            log.error(violations, e);
            builder.message(violations).code(HttpStatus.BAD_REQUEST.getReasonPhrase());
        } else {
            String message = e.getMessage();
            log.error(message, e);
            builder.message(message).code(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        return builder.build();
    }

    private String extractViolationFromException(ConstraintViolationException e) {
        return e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }
}
