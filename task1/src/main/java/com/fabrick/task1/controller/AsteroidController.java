package com.fabrick.task1.controller;

import com.fabrick.task1.dto.AsteroidPathDTO;
import com.fabrick.task1.service.AsteroidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/fabrick/v1.0/asteroids")
@Tag(name = "Asteroid Controller", description = "Endpoints for retrieving Asteroid path information")
public class AsteroidController {

    private final AsteroidService asteroidService;

    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }

    @Operation(summary = "Get Asteroid Paths", description = "Retrieves the path of a specific asteroid across the Solar System within a given date range. If dates are not provided, it defaults to the last 100 years.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paths retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AsteroidPathDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid parameters provided", content = @Content),
            @ApiResponse(responseCode = "404", description = "Asteroid not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/{asteroidId}/paths")
    public Flux<AsteroidPathDTO> getAsteroidPath(
            @Parameter(description = "The NASA SPK-ID of the asteroid", required = true, example = "3542519")
            @PathVariable String asteroidId,

            @Parameter(description = "Start date for the path search (YYYY-MM-DD). Defaults to 100 years ago if omitted.", example = "1900-01-01")
            @RequestParam(required = false) LocalDate fromDate,

            @Parameter(description = "End date for the path search (YYYY-MM-DD). Defaults to today if omitted.", example = "2000-01-01")
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