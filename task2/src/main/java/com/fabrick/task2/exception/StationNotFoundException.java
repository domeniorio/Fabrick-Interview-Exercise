package com.fabrick.task2.exception;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException(String stationId) {
        super("Station with ID " + stationId + " not found");
    }
}
