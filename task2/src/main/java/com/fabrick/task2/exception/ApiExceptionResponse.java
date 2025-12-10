package com.fabrick.task2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class ApiExceptionResponse {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}