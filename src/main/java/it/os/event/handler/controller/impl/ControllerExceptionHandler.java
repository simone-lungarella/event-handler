package it.os.event.handler.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.exception.StepNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ StepNotFoundException.class })
    public ResponseEntity<String> handleException(final RuntimeException ex, final WebRequest request) {

        String bodyOfResponse = "Entity not found";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ BusinessException.class })
    public ResponseEntity<Void> handleGenericException(final RuntimeException ex, final WebRequest request) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
