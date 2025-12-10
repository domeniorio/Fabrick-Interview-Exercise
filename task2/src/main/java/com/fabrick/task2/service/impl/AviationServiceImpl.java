package com.fabrick.task2.service.impl;

import com.fabrick.task2.client.AviationClient;
import com.fabrick.task2.dto.AirportDTO;
import com.fabrick.task2.dto.StationDTO;
import com.fabrick.task2.service.AviationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class AviationServiceImpl implements AviationService {

    private final AviationClient aviationClient;

    @Override
    public Flux<StationDTO> getStationsNearAirport(String airportId, Double closestBy) {
        return aviationClient.getAirport(airportId)
                .flatMapMany(airport -> {
                    if (airport.getLatitude() == null || airport.getLongitude() == null) {
                        log.warn("Airport {} found but coordinates are missing. Skipping search.", airport.getId());
                        return Flux.empty();
                    }

                    log.info("Found airport {} at [{}, {}]", airport.getId(), airport.getLatitude(), airport.getLongitude());

                    double minLat = airport.getLatitude() - closestBy;
                    double maxLat = airport.getLatitude() + closestBy;
                    double minLon = airport.getLongitude() - closestBy;
                    double maxLon = airport.getLongitude() + closestBy;

                    return aviationClient.getStationsInBBox(minLat, minLon, maxLat, maxLon);
                });
    }

    @Override
    public Flux<AirportDTO> getAirportsNearStation(String stationId, Double closestBy) {
        return aviationClient.getStation(stationId)
                .flatMapMany(station -> {
                    if (station.getLatitude() == null || station.getLongitude() == null) {
                        log.warn("Station {} found but coordinates are missing. Skipping search.", station.getId());
                        return Flux.empty();
                    }

                    log.info("Found station {} at [{}, {}]", station.getId(), station.getLatitude(), station.getLongitude());

                    double minLat = station.getLatitude() - closestBy;
                    double maxLat = station.getLatitude() + closestBy;
                    double minLon = station.getLongitude() - closestBy;
                    double maxLon = station.getLongitude() + closestBy;

                    return aviationClient.getAirportsInBBox(minLat, minLon, maxLat, maxLon);
                });
    }
}