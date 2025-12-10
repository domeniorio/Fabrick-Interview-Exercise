package com.fabrick.task1.exception;

public class AsteroidNotFoundException extends RuntimeException {
    public AsteroidNotFoundException(String asteroidId) {
        super("Asteroid with ID " + asteroidId + " not found");
    }
}