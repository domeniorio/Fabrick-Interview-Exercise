package com.fabrick.task1.service;

import com.fabrick.task1.dto.AsteroidPathDTO;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface AsteroidService {
    Flux<AsteroidPathDTO> getAsteroidPaths(String asteroidId, LocalDate fromDate, LocalDate toDate);


}
