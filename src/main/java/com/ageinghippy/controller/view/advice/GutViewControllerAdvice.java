package com.ageinghippy.controller.view.advice;

import com.ageinghippy.controller.api.advice.ResponseErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@ControllerAdvice(basePackages = {"com.ageinghippy.controller.view"})
@Slf4j
public class GutViewControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseErrorMessage noSuchElementExceptionResponse(NoSuchElementException ex) {
        log.warn(ex.getMessage());
        return new ResponseErrorMessage(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseErrorMessage noSuchElementExceptionResponse(IllegalArgumentException ex) {
        log.warn(ex.getMessage());
        return new ResponseErrorMessage(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView noSuchElementExceptionResponse(HttpServletRequest request,
                                                       DataIntegrityViolationException ex) {
        log.warn("Data Integrity Exception :: request URL= " + request.getRequestURL());
        log.warn(ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("errorMessage", "Data Integrity Violation");

        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionResponse(HttpServletRequest request, Exception ex) {
        log.warn(ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("errorMessage", ex.getMessage());

        return modelAndView;
    }

}
