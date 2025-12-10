package com.fabrick.task1.service.impl;

import com.fabrick.task1.client.AsteroidClient;
import com.fabrick.task1.dto.AsteroidPathDTO;
import com.fabrick.task1.dto.NasaCloseApproachData;
import com.fabrick.task1.service.AsteroidService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class AsteroidServiceImpl implements AsteroidService {

    private final AsteroidClient asteroidClient;

    public AsteroidServiceImpl(AsteroidClient asteroidClient) {
        this.asteroidClient = asteroidClient;
    }

    @Override
    public Flux<AsteroidPathDTO> getAsteroidPaths(String asteroidId, LocalDate fromDate, LocalDate toDate) {

        return asteroidClient.getAsteroidData(asteroidId)
                .flatMapMany(response -> {
                    List<AsteroidPathDTO> paths = calculatePaths(response.getCloseApproachData(), fromDate, toDate);
                    return Flux.fromIterable(paths);
                });
    }

    private List<AsteroidPathDTO> calculatePaths(List<NasaCloseApproachData> rawData, LocalDate fromFilter, LocalDate toFilter) {
        if (rawData == null || rawData.isEmpty()) {
            return List.of();
        }

        List<NasaCloseApproachData> sortedData = rawData.stream()
                .filter(Objects::nonNull)
                .filter(d -> {
                    LocalDate date = LocalDate.parse(d.getCloseApproachDate());
                    return !date.isBefore(fromFilter) && !date.isAfter(toFilter);
                })
                .sorted(Comparator.comparing(d -> LocalDate.parse(d.getCloseApproachDate())))
                .toList();

        if (sortedData.isEmpty()) {
            return List.of();
        }

        return getAsteroidPathDTOS(sortedData);
    }

    private static List<AsteroidPathDTO> getAsteroidPathDTOS(List<NasaCloseApproachData> sortedData) {
        List<AsteroidPathDTO> paths = new ArrayList<>();

        String currentPlanet = sortedData.getFirst().getOrbitingBody();
        String currentStartDate = sortedData.getFirst().getCloseApproachDate();

        for (int i = 1; i < sortedData.size(); i++) {
            NasaCloseApproachData nextApproach = sortedData.get(i);
            String nextPlanet = nextApproach.getOrbitingBody();

            if (!nextPlanet.equals(currentPlanet)) {

                AsteroidPathDTO path = new AsteroidPathDTO(
                        currentPlanet,
                        nextPlanet,
                        currentStartDate,
                        nextApproach.getCloseApproachDate()
                );

                paths.add(path);

                currentPlanet = nextPlanet;
                currentStartDate = nextApproach.getCloseApproachDate();
            }
        }
        return paths;
    }
}