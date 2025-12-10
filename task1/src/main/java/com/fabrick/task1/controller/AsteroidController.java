package com.fabrick.task1.controller;

import com.fabrick.task1.dto.AsteroidPathDTO;
import com.fabrick.task1.service.AsteroidService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/fabrick/v1.0/asteroids")
public class AsteroidController {

    private final AsteroidService asteroidService;

    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    @GetMapping("/{asteroidId}/paths")
    public Flux<AsteroidPathDTO> getAsteroidPath(
            @PathVariable String asteroidId,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate
    ) {

        if (toDate == null) {
            toDate = LocalDate.now();
        }

        if (fromDate == null) {
            fromDate = toDate.minusYears(100);
        }

        return asteroidService.getAsteroidPaths(asteroidId, fromDate, toDate);
    }
}