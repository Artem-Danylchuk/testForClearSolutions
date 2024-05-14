package com.testForCS.controller;

import com.testForCS.exception.BadRequestException;
import com.testForCS.exception.EmailInvalidException;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DataIntegrityViolationException.class, EntityExistsException.class})
    public ResponseEntity<String> resourceAlreadyExists(Exception e) {
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>("Resource already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler({EmailInvalidException.class})
    public ResponseEntity<String> invalidEmail(Exception e) {
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<?> badRequestException(Exception e) {
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>("Are date is invalid.", HttpStatus.BAD_REQUEST);
    }

}
