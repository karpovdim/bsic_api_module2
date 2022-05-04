package com.epam.esm.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class ExceptionControllerAdviser {

    private static final List<String> AVAILABLE_LOCALES = Arrays.asList("en_US", "ru_RU");
    private static final Locale DEFAULT_LOCALE = new Locale("en", "US");

    private final ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    public ExceptionControllerAdviser(ResourceBundleMessageSource bundleMessageSource) {
        this.bundleMessageSource = bundleMessageSource;
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(String message, Integer code, HttpStatus status) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(message, code);
        return new ResponseEntity<>(exceptionResponse, status);
    }

    private String resolveResourceBundle(String key, Locale locale) {
        if (!AVAILABLE_LOCALES.contains(locale.toString())) {
            locale = DEFAULT_LOCALE;
        }
        return bundleMessageSource.getMessage(key, null, locale);
    }

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(NoSuchEntityException e, Locale locale) {
        return buildErrorResponse(resolveResourceBundle(e.getMessage(), locale), 40404, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(DuplicateEntityException e, Locale locale) {
        return buildErrorResponse(resolveResourceBundle(e.getMessage(), locale), 40409, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ExceptionResponse> handleServiceException(InvalidEntityException e, Locale locale) {
        return buildErrorResponse(resolveResourceBundle(e.getMessage(), locale), 40400, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchEntityException(MissingServletRequestParameterException e) {
        return buildErrorResponse(e.getMessage(), 40002, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatchException(TypeMismatchException e) {
        return buildErrorResponse(e.getMessage(), 40000, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return buildErrorResponse(e.getMessage(), 40500, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleNotReadableBodyException(Locale locale) {
        final String message = "Required request body data is missing.";
        return buildErrorResponse(resolveResourceBundle(message, locale), 40001, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNoFoundException(NoHandlerFoundException e) {
        return buildErrorResponse(e.getMessage(), 40400, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionResponse> handleOtherExceptions(Exception e) {
        return buildErrorResponse(e.getMessage(), 50000, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
