package com.fabrick.task2.exception;

import java.time.ZonedDateTime;


import com.fabrick.task2.dto.ApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handlerGenericException(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;


        ApiExceptionResponse response = new ApiExceptionResponse(
                "Errore interno del server. Controllare i log.",
                status,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {AirportNotFoundException.class})
    public ResponseEntity<Object> handlerAirportNotFoundException(Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiExceptionResponse response = new ApiExceptionResponse(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {StationNotFoundException.class})
    public ResponseEntity<Object> handlerStationNotFoundException(Exception e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiExceptionResponse response = new ApiExceptionResponse(
                e.getMessage(),
                status,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(response, status);
    }


}