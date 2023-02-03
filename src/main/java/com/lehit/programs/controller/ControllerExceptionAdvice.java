package com.lehit.programs.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionAdvice {
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> exceptionHandler(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> exceptionHandler(ConstraintViolationException ex) {
        log.debug("bad request: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getCause().getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> exceptionHandler(DataIntegrityViolationException ex) {
        log.debug("bad request: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getCause().getMessage());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> exceptionHandler(EmptyResultDataAccessException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }



    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> exceptionHandler(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
