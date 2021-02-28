package com.ofk.template.authenticationservice.advice;

import com.ofk.template.authenticationservice.exception.BadCredentialsException;
import com.ofk.template.authenticationservice.exception.EmailAlreadyExistsException;
import com.ofk.template.authenticationservice.exception.UsernameAlreadyExistsException;
import com.ofk.template.authenticationservice.exception.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PublicControllerAdvice {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadCredentials(BadCredentialsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUsernameNotFound(UsernameNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEmailAlreadyExists(EmailAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUsernameAlreadyExists(UsernameAlreadyExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleUnknownException(Exception exception) {
        return exception.getMessage();
    }
}
