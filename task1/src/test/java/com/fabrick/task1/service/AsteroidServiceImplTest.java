package com.fabrick.task1.service;

import com.fabrick.task1.client.AsteroidClient;
import com.fabrick.task1.dto.NasaCloseApproachData;
import com.fabrick.task1.dto.NasaNeoLookupResponse;
import com.fabrick.task1.service.impl.AsteroidServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsteroidServiceImplTest {

    @Mock
    private AsteroidClient asteroidClient;

    @InjectMocks
    private AsteroidServiceImpl asteroidService;

    private NasaCloseApproachData createData(String date, String planet) {
        NasaCloseApproachData data = new NasaCloseApproachData();
        data.setCloseApproachDate(date);
        data.setOrbitingBody(planet);
        return data;
    }

    @Test
    void getAsteroidPaths_ShouldReturnCorrectPath_WhenPlanetsChange() {
        String asteroidId = "123";
        LocalDate from = LocalDate.of(1900, 1, 1);
        LocalDate to = LocalDate.of(2000, 1, 1);

        List<NasaCloseApproachData> mockData = List.of(
                createData("1910-01-01", "Earth"),
                createData("1920-01-01", "Earth"),
                createData("1930-01-01", "Mars"),
                createData("1940-01-01", "Mars"),
                createData("1950-01-01", "Earth")
        );

        NasaNeoLookupResponse mockResponse = new NasaNeoLookupResponse("123", "Test", mockData);

        when(asteroidClient.getAsteroidData(asteroidId)).thenReturn(Mono.just(mockResponse));

        StepVerifier.create(asteroidService.getAsteroidPaths(asteroidId, from, to))
                .expectNextMatches(dto ->
                        dto.getFromPlanet().equals("Earth") &&
                                dto.getToPlanet().equals("Mars") &&
                                dto.getFromDate().equals("1910-01-01") &&
                                dto.getToDate().equals("1930-01-01")
                )
                .expectNextMatches(dto ->
                        dto.getFromPlanet().equals("Mars") &&
                                dto.getToPlanet().equals("Earth") &&
                                dto.getFromDate().equals("1930-01-01") &&
                                dto.getToDate().equals("1950-01-01")
                )
                .verifyComplete();
    }

    @Test
    void getAsteroidPaths_ShouldFilterDatesCorrectly() {
        String asteroidId = "123";
        LocalDate from = LocalDate.of(1940, 1, 1);
        LocalDate to = LocalDate.of(1960, 1, 1);

        List<NasaCloseApproachData> mockData = List.of(
                createData("1910-01-01", "Earth"),
                createData("1950-01-01", "Earth"),
                createData("1955-01-01", "Mars"),
                createData("1990-01-01", "Mars")
        );

        when(asteroidClient.getAsteroidData(asteroidId))
                .thenReturn(Mono.just(new NasaNeoLookupResponse("123", "Test", mockData)));

        StepVerifier.create(asteroidService.getAsteroidPaths(asteroidId, from, to))
                .expectNextMatches(dto ->
                        dto.getFromPlanet().equals("Earth") &&
                                dto.getToPlanet().equals("Mars") &&
                                dto.getFromDate().equals("1950-01-01")
                )
                .verifyComplete();
    }

    @Test
    void getAsteroidPaths_ShouldReturnEmpty_WhenNoPlanetChanges() {
        List<NasaCloseApproachData> mockData = List.of(
                createData("1910-01-01", "Earth"),
                createData("1920-01-01", "Earth")
        );

        when(asteroidClient.getAsteroidData("123"))
                .thenReturn(Mono.just(new NasaNeoLookupResponse("123", "Test", mockData)));

        StepVerifier.create(asteroidService.getAsteroidPaths("123", LocalDate.MIN, LocalDate.MAX))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void getAsteroidPaths_ShouldHandleEmptyData() {
        when(asteroidClient.getAsteroidData("123"))
                .thenReturn(Mono.just(new NasaNeoLookupResponse("123", "Test", List.of()))); // Lista vuota

        StepVerifier.create(asteroidService.getAsteroidPaths("123", LocalDate.now(), LocalDate.now()))
                .expectNextCount(0)
                .verifyComplete();
    }
}