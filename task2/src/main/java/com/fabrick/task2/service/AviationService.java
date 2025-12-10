package com.fabrick.task2.service;

import com.fabrick.task2.dto.AirportDTO;
import com.fabrick.task2.dto.StationDTO;
import reactor.core.publisher.Flux;

public interface AviationService {
    Flux<StationDTO> getStationsNearAirport(String airportId, Double closestBy);

    Flux<AirportDTO> getAirportsNearStation(String stationId, Double closestBy);
}
