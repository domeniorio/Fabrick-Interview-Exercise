package com.fabrick.task2.client;

import com.fabrick.task2.dto.AirportDTO;
import com.fabrick.task2.dto.StationDTO;
import com.fabrick.task2.exception.AirportNotFoundException;
import com.fabrick.task2.exception.StationNotFoundException;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Slf4j
@Component
public class AviationClient {

    private final WebClient webClient;

    private final Cache<String, AirportDTO> airportCache;
    private final Cache<String, StationDTO> stationCache;

    public AviationClient(WebClient.Builder builder,
                          @Value("${aviation.api.base-url}") String baseUrl,
                          Cache<String, AirportDTO> airportCache,
                          Cache<String, StationDTO> stationCache) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.airportCache = airportCache;
        this.stationCache = stationCache;
    }

    public Mono<AirportDTO> getAirport(String airportId) {
        return Mono.justOrEmpty(airportCache.getIfPresent(airportId))
                .doOnNext(a -> log.info("Cache HIT for airport: {}", airportId))
                .switchIfEmpty(Mono.defer(() -> fetchAirportFromApi(airportId)));
    }

    private Mono<AirportDTO> fetchAirportFromApi(String airportId) {
        log.info("Cache MISS for airport: {}. Calling API...", airportId);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airport")
                        .queryParam("ids", airportId)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToFlux(AirportDTO.class)
                .next()
                .switchIfEmpty(Mono.error(new AirportNotFoundException(airportId)))
                .doOnNext(dto -> airportCache.put(airportId, dto));
    }

    public Mono<StationDTO> getStation(String stationId) {
        return Mono.justOrEmpty(stationCache.getIfPresent(stationId))
                .doOnNext(s -> log.info("Cache HIT for station: {}", stationId))
                .switchIfEmpty(Mono.defer(() -> fetchStationFromApi(stationId)));
    }

    private Mono<StationDTO> fetchStationFromApi(String stationId) {
        log.info("Cache MISS for station: {}. Calling API...", stationId);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stationinfo")
                        .queryParam("ids", stationId)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToFlux(StationDTO.class)
                .next()
                .switchIfEmpty(Mono.error(new StationNotFoundException(stationId)))
                .doOnNext(dto -> stationCache.put(stationId, dto));
    }

    public Flux<StationDTO> getStationsInBBox(double minLat, double minLon, double maxLat, double maxLon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stationinfo")
                        .queryParam("bbox", String.format(Locale.US, "%f,%f,%f,%f", minLon, minLat, maxLon, maxLat))
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToFlux(StationDTO.class);
    }

    public Flux<AirportDTO> getAirportsInBBox(double minLat, double minLon, double maxLat, double maxLon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airport")
                        .queryParam("bbox", String.format(Locale.US, "%f,%f,%f,%f", minLon, minLat, maxLon, maxLat))
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToFlux(AirportDTO.class);
    }
}