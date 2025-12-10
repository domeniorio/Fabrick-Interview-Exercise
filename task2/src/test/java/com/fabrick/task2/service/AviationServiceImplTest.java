package com.fabrick.task2.service;

import com.fabrick.task2.client.AviationClient;
import com.fabrick.task2.dto.AirportDTO;
import com.fabrick.task2.dto.StationDTO;
import com.fabrick.task2.service.impl.AviationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AviationServiceImplTest {

    @Mock
    private AviationClient aviationClient;

    @InjectMocks
    private AviationServiceImpl aviationService;

    private AirportDTO createAirport(String id, double lat, double lon) {
        return new AirportDTO(
                id,
                "Test Airport",
                "CA",
                "US",
                lat,
                lon,
                100.0
        );
    }

    private StationDTO createStation(String id, double lat, double lon) {
        return new StationDTO(
                id,
                "Test Station",
                "ST",
                "US",
                lat,
                lon,
                100.0
        );
    }

    @Test
    @DisplayName("getStationsNearAirport: deve trovare stazioni se l'aeroporto esiste")
    void getStationsNearAirport_Success() {
        String airportId = "KAFF";
        double airportLat = 40.0;
        double airportLon = -100.0;
        double closestBy = 1.0;

        when(aviationClient.getAirport(airportId))
                .thenReturn(Mono.just(createAirport(airportId, airportLat, airportLon)));

        when(aviationClient.getStationsInBBox(eq(39.0), eq(-101.0), eq(41.0), eq(-99.0)))
                .thenReturn(Flux.just(
                        createStation("ST1", 40.5, -100.5),
                        createStation("ST2", 39.5, -99.5)
                ));

        StepVerifier.create(aviationService.getStationsNearAirport(airportId, closestBy))
                .expectNextMatches(s -> s.getId().equals("ST1"))
                .expectNextMatches(s -> s.getId().equals("ST2"))
                .verifyComplete();

        verify(aviationClient).getAirport(airportId);
        verify(aviationClient).getStationsInBBox(39.0, -101.0, 41.0, -99.0);
    }

    @Test
    @DisplayName("getAirportsNearStation: deve trovare aeroporti se la stazione esiste")
    void getAirportsNearStation_Success() {
        String stationId = "S1";
        double lat = 10.0;
        double lon = 20.0;
        double closestBy = 0.5;

        when(aviationClient.getStation(stationId))
                .thenReturn(Mono.just(createStation(stationId, lat, lon)));

        when(aviationClient.getAirportsInBBox(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Flux.just(createAirport("A1", 10.1, 20.1)));

        StepVerifier.create(aviationService.getAirportsNearStation(stationId, closestBy))
                .expectNextMatches(a -> a.getId().equals("A1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Se l'aeroporto non viene trovato (Mono.error), il flusso deve propagare l'errore")
    void getStationsNearAirport_AirportNotFound() {
        String airportId = "INVALID";
        when(aviationClient.getAirport(airportId))
                .thenReturn(Mono.error(new RuntimeException("Not Found")));

        StepVerifier.create(aviationService.getStationsNearAirport(airportId, 1.0))
                .expectError(RuntimeException.class)
                .verify();

        verify(aviationClient, never()).getStationsInBBox(anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Se l'aeroporto ha coordinate NULL, deve restituire vuoto e NON crashare")
    void getStationsNearAirport_NullCoordinates() {
        AirportDTO badAirport = new AirportDTO(
                "BAD",
                "Bad",
                "XX",
                "US",
                null,
                null,
                0.0
        );

        when(aviationClient.getAirport("BAD"))
                .thenReturn(Mono.just(badAirport));
        StepVerifier.create(aviationService.getStationsNearAirport("BAD", 1.0))
                .expectNextCount(0)
                .verifyComplete();
    }
}