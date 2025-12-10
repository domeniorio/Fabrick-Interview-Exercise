package com.fabrick.task1.exception;

import java.time.ZonedDateTime;


import com.fabrick.task1.dto.ApiExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handlerGenericException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;


        ApiExceptionResponse response = new ApiExceptionResponse(
                "Errore interno del server. Controllare i log.",
                status,
                ZonedDateTime.now()
        );
        log.warn(e.getMessage());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {AsteroidNotFoundException.class})
    public ResponseEntity<Object> handlerAsteroidNotFoundException(Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiExceptionResponse response = new ApiExceptionResponse(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(response, status);
    }



}