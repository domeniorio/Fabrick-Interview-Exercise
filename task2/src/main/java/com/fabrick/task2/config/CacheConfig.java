package com.fabrick.task2.config;

import com.fabrick.task2.dto.AirportDTO;
import com.fabrick.task2.dto.StationDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, AirportDTO> airportCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(1))
                .maximumSize(500)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<String, StationDTO> stationCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofHours(1))
                .maximumSize(500)
                .recordStats()
                .build();
    }
}