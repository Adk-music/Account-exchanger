package com.kutyrina.accountexchanger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorizedException(UnauthorizedException exception) {
        return exception.getMessage();
    }
}
