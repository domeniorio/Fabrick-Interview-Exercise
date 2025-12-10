package com.fabrick.task1.service.impl;

import com.fabrick.task1.client.AsteroidClient;
import com.fabrick.task1.dto.AsteroidPathDTO;
import com.fabrick.task1.dto.NasaCloseApproachData;
import com.fabrick.task1.service.AsteroidService;
import lombok.extern.slf4j.Slf4j; // Import necessario per i log
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j // Abilita il logger 'log'
@Service
public class AsteroidServiceImpl implements AsteroidService {

    private final AsteroidClient asteroidClient;

    public AsteroidServiceImpl(AsteroidClient asteroidClient) {
        this.asteroidClient = asteroidClient;
    }

    @Override
    public Flux<AsteroidPathDTO> getAsteroidPaths(String asteroidId, LocalDate fromDate, LocalDate toDate) {

        log.info("Requesting paths for Asteroid ID: [{}], Range: [{} to {}]", asteroidId, fromDate, toDate);

        return asteroidClient.getAsteroidData(asteroidId)
                .flatMapMany(response -> {
                    log.debug("Received response from NASA for ID: {}", asteroidId); // Assumo che response abbia un getter id()
                    List<AsteroidPathDTO> paths = calculatePaths(response.getCloseApproachData(), fromDate, toDate);

                    log.info("Calculated {} paths for asteroid {}", paths.size(), asteroidId);
                    return Flux.fromIterable(paths);
                });
    }

    private List<AsteroidPathDTO> calculatePaths(List<NasaCloseApproachData> rawData, LocalDate fromFilter, LocalDate toFilter) {
        if (rawData == null || rawData.isEmpty()) {
            log.warn("No Close Approach Data found in NASA response");
            return List.of();
        }

        log.debug("Processing {} raw data entries", rawData.size());

        List<NasaCloseApproachData> sortedData = rawData.stream()
                .filter(Objects::nonNull)
                .filter(d -> {
                    LocalDate date = LocalDate.parse(d.getCloseApproachDate());
                    //in questo modo prendo in considerazione anche i percorsi iniziati prima di fromFilter e che finiscono entro toFilter. Per considerare soltato quelli iniziati dopo fromFilter l'operazione di filtraggio deve essere effettuata come ultimo step.
                    return !date.isBefore(fromFilter) && !date.isAfter(toFilter);
                })
                .sorted(Comparator.comparing(d -> LocalDate.parse(d.getCloseApproachDate())))
                .toList();

        log.debug("Data filtered by date range: {} entries remained", sortedData.size());

        if (sortedData.isEmpty()) {
            log.info("No close approach data found within the requested date range");
            return List.of();
        }

        return getAsteroidPathDTOS(sortedData);
    }

    private static List<AsteroidPathDTO> getAsteroidPathDTOS(List<NasaCloseApproachData> sortedData) {
        List<AsteroidPathDTO> paths = new ArrayList<>();

        String currentPlanet = sortedData.getFirst().getOrbitingBody();
        String currentStartDate = sortedData.getFirst().getCloseApproachDate();

        log.debug("Starting path calculation from planet: {} at date: {}", currentPlanet, currentStartDate);

        for (int i = 1; i < sortedData.size(); i++) {
            NasaCloseApproachData nextApproach = sortedData.get(i);
            String nextPlanet = nextApproach.getOrbitingBody();

            log.trace("Checking transition: {} -> {} on {}", currentPlanet, nextPlanet, nextApproach.getCloseApproachDate());

            if (!nextPlanet.equals(currentPlanet)) {
                log.debug("Planet change detected: {} -> {}", currentPlanet, nextPlanet);

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