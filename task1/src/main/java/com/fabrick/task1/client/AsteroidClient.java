package com.fabrick.task1.client;

import com.fabrick.task1.dto.NasaNeoLookupResponse;
import com.fabrick.task1.exception.AsteroidNotFoundException;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AsteroidClient {

    private final WebClient webClient;
    private final String apiKey;

    private final Cache<String, NasaNeoLookupResponse> cache;

    public AsteroidClient(WebClient.Builder builder,
                          @Value("${nasa.api.base-url}") String baseUrl,
                          @Value("${nasa.api.key}") String apiKey,
                          Cache<String, NasaNeoLookupResponse> cache) {
        this.apiKey = apiKey;
        this.cache = cache;
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Mono<NasaNeoLookupResponse> getAsteroidData(String asteroidId) {
        return Mono.justOrEmpty(cache.getIfPresent(asteroidId))
                .doOnNext(data -> log.info("Cache HIT for asteroid: {}", asteroidId))
                .switchIfEmpty(
                        Mono.defer(() -> fetchFromNasa(asteroidId))
                );
    }

    private Mono<NasaNeoLookupResponse> fetchFromNasa(String asteroidId) {
        log.info("Cache MISS for asteroid: {}. Calling NASA API...", asteroidId);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/neo/rest/v1/neo/{asteroidId}")
                        .queryParam("api_key", apiKey)
                        .build(asteroidId))
                .retrieve()
                .bodyToMono(NasaNeoLookupResponse.class)
                .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                    return Mono.error(new AsteroidNotFoundException(asteroidId));
                })
                .doOnNext(response -> cache.put(asteroidId, response));
    }
}