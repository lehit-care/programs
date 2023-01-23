package com.lehit.programs.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
//todo implement method
public class ControllerExceptionAdvice {
//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<?> exceptionHandler(IllegalStateException ex) {
//        return forbidden(new HttpHeaders(), ex);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<?> exceptionHandler(ConstraintViolationException ex) {
//        log.debug("bad request: ", ex);
//        return badRequest(new HttpHeaders(), ex);
//    }
//
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<?> exceptionHandler(DataIntegrityViolationException ex) {
//        log.debug("bad request: ", ex);
//        return badRequest(new HttpHeaders(), ex);
//    }
//
//    @ExceptionHandler(JpaSystemException.class)
//    public ResponseEntity<?> exceptionHandler(JpaSystemException ex) {
//        log.debug("bad request: ", ex);
//        return badRequest(new HttpHeaders(), ex);
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<?> exceptionHandler(NoSuchElementException ex) {
//        return notFound(new HttpHeaders());
//    }
//
//
//    private static ResponseEntity<?> notFound(HttpHeaders headers) {
//        return response(HttpStatus.NOT_FOUND, headers, null);
//    }
//
//    private static ResponseEntity<ExceptionMessage> badRequest(HttpHeaders headers, Exception throwable) {
//        return errorResponse(HttpStatus.BAD_REQUEST, headers, throwable);
//    }
//
//    private static ResponseEntity<ExceptionMessage> forbidden(HttpHeaders headers, Exception throwable) {
//        return errorResponse(HttpStatus.FORBIDDEN, headers, throwable);
//    }
//
//    private static ResponseEntity<ExceptionMessage> errorResponse(HttpStatus status, HttpHeaders headers,
//                                                                  Exception exception) {
//
//        if (exception != null) {
//
//            String message = exception.getMessage();
//            log.debug(LogFormatUtils.formatValue(message, -1, true), exception);
//
//            if (StringUtils.hasText(message)) {
//                return response(status, headers, new ExceptionMessage(exception));
//            }
//        }
//
//        return response(status, headers, null);
//    }


    private static <T> ResponseEntity<T> response(HttpStatus status, HttpHeaders headers, T body) {

        Assert.notNull(headers, "Headers must not be null");
        Assert.notNull(status, "HttpStatus must not be null");

        return new ResponseEntity<T>(body, headers, status);
    }
}
