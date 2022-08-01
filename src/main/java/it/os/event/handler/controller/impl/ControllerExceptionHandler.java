package it.os.event.handler.controller.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import it.os.event.handler.exception.AdminRequiredException;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.exception.EventNotFoundException;
import it.os.event.handler.exception.StepNotFoundException;
import it.os.event.handler.exception.UsernameAlreadyTakenException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({ StepNotFoundException.class })
    public ResponseEntity<String> handleException(final StepNotFoundException ex, final WebRequest request) {

        String bodyOfResponse = "Step not found";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ EventNotFoundException.class })
    public ResponseEntity<String> handleException(final EventNotFoundException ex, final WebRequest request) {

        String bodyOfResponse = "Event not found";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ UsernameNotFoundException.class })
    public ResponseEntity<String> handleUserMissingException(final RuntimeException ex, final WebRequest request) {

        String bodyOfResponse = "User with given username not found";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ UsernameAlreadyTakenException.class })
    public ResponseEntity<String> handleUsernameTakenException(final RuntimeException ex, final WebRequest request) {

        String bodyOfResponse = "The given username is already used by another user";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ AdminRequiredException.class })
    public ResponseEntity<String> handleAdminRequiredException(final RuntimeException ex, final WebRequest request) {

        String bodyOfResponse = "To execute this operation an admin user is required";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler({ BusinessException.class })
    public ResponseEntity<Void> handleGenericException(final RuntimeException ex, final WebRequest request) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
