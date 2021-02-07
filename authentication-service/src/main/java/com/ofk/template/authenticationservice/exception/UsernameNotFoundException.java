package com.ofk.template.authenticationservice.exception;

public class UsernameNotFoundException extends RuntimeException {

    public UsernameNotFoundException() {
    }

    public UsernameNotFoundException(String message) {
        super(message);
    }
}
