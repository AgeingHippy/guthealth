package com.ageinghippy.controller.rest.advice;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransientPropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestControllerAdvice(basePackages = {"com.ageinghippy.controller.rest"})
@Slf4j
public class GutRestControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseErrorMessage noSuchElementExceptionResponse(NoSuchElementException ex) {
        log.warn(ex.getMessage());
        return new ResponseErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseErrorMessage illegalArgumentExceptionResponse(IllegalArgumentException ex) {
        log.warn(ex.getMessage());
        return new ResponseErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, TransientPropertyValueException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseErrorMessage integrityViolationResponse(Exception ex) {
        log.warn(ex.getMessage());
        return new ResponseErrorMessage(HttpStatus.BAD_REQUEST, "Data Integrity Violation");
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseErrorMessage exceptionResponse(Exception ex) {
        UUID uuid = UUID.randomUUID();
        log.error(uuid + " :: " + ex.getCause());
        ex.printStackTrace();
        return new ResponseErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error. Log reference: " + uuid);
    }


}
