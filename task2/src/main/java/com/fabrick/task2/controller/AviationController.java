package com.fabrick.task2.controller;

import com.fabrick.task2.dto.AirportDTO;
import com.fabrick.task2.dto.StationDTO;
import com.fabrick.task2.service.AviationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/fabrick/v1.0")
@RequiredArgsConstructor
@Tag(name = "Aviation Controller", description = "Endpoints for Airports and Weather Stations geo-search")
public class AviationController {

    private final AviationService aviationService;

    @Operation(summary = "Find stations near an airport",
            description = "Given an airport ICAO code, finds all weather stations within a specific radius (Bounding Box).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stations found successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = StationDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Airport not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/airports/{airportId}/stations")
    public Flux<StationDTO> getStationsByAirport(
            @Parameter(description = "ICAO Airport Code (e.g., 'KAFF', 'LIRF')", required = true, example = "KAFF")
            @PathVariable String airportId,

            @Parameter(description = "Radius in degrees for the Bounding Box (e.g. 0.5 ~ 55km). Default is 0.0 (exact match).", example = "1.0")
            @RequestParam(required = false, defaultValue = "0.0") Double closestBy
    ) {
        return aviationService.getStationsNearAirport(airportId, closestBy);
    }

    @Operation(summary = "Find airports near a station",
            description = "Given a station ICAO code, finds all airports within a specific radius (Bounding Box).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airports found successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AirportDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Station not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping("/stations/{stationId}/airports")
    public Flux<AirportDTO> getAirportsByStation(
            @Parameter(description = "Station ID (e.g., 'KAFF')", required = true, example = "KAFF")
            @PathVariable String stationId,

            @Parameter(description = "Radius in degrees for the Bounding Box. Default is 0.0.", example = "1.0")
            @RequestParam(required = false, defaultValue = "0.0") Double closestBy
    ) {
        return aviationService.getAirportsNearStation(stationId, closestBy);
    }
}