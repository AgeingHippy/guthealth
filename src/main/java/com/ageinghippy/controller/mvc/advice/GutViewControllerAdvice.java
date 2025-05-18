package com.ageinghippy.controller.mvc.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;
import java.util.UUID;

@ControllerAdvice(basePackages = {"com.ageinghippy.controller.mvc"})
@Slf4j
public class GutViewControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchElementExceptionResponse(HttpServletRequest request,
                                                       NoSuchElementException ex) {
        log.warn(ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("errorMessage", ex.getMessage());

        return modelAndView;
    }

    @ExceptionHandler({IllegalArgumentException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView noSuchElementExceptionResponse(HttpServletRequest request,
                                                       Exception ex) {
        log.warn(ex.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("errorMessage", ex.getMessage());

        return modelAndView;
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

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ModelAndView authorizationDeniedExceptionResponse(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("errorMessage","Access Denied - You don't have the necessary permissions for this action") ;

        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionResponse(HttpServletRequest request, Exception ex) {
        UUID uuid = UUID.randomUUID();
        log.error(uuid + " :: " + ex.getMessage(), ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("errorMessage","Unexpected exception. Please provide the following log reference to support: " + uuid) ;

        return modelAndView;
    }

}
