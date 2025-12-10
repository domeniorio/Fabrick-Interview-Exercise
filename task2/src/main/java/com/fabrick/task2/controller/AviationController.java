package com.fabrick.task2.controller;

import com.fabrick.task2.dto.AirportDTO;
import com.fabrick.task2.dto.StationDTO;
import com.fabrick.task2.service.AviationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/fabrick/v1.0")
@RequiredArgsConstructor
public class AviationController {

    private final AviationService aviationService;

    @GetMapping("/airports/{airportId}/stations")
    public Flux<StationDTO> getStationsByAirport(
            @PathVariable String airportId,
            @RequestParam(required = false, defaultValue = "0.0") Double closestBy
    ) {
        return aviationService.getStationsNearAirport(airportId, closestBy);
    }

    @GetMapping("/stations/{stationId}/airports")
    public Flux<AirportDTO> getAirportsByStation(
            @PathVariable String stationId,
            @RequestParam(required = false, defaultValue = "0.0") Double closestBy
    ) {
        return aviationService.getAirportsNearStation(stationId, closestBy);
    }
}