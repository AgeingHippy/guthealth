package com.ageinghippy.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class GutControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public ResponseErrorMessage noSuchElementExceptionResponse(NoSuchElementException ex) {
        log.warn(ex.getMessage());
        return new ResponseErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public ResponseErrorMessage noSuchElementExceptionResponse(IllegalArgumentException ex) {
        log.warn(ex.getMessage());
        return new ResponseErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public ResponseErrorMessage noSuchElementExceptionResponse(DataIntegrityViolationException ex) {
        log.warn(ex.getMessage());
        return new ResponseErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
    }



}
