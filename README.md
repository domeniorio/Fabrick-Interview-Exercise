# Fabrick Interview Exercise - Spring Boot Solutions

This repository contains the solutions for the Fabrick Interview technical assessment. The project is divided into two sub-projects corresponding to the requested tasks.

Both solutions are implemented using a **Non-Blocking (Reactive)** approach with Spring WebFlux.

## 🛠 Technology Stack

* **Language:** Java 21
* **Framework:** Spring Boot 4.0.0
* **Reactive Stack:** Spring WebFlux (Project Reactor - Netty)
* **HTTP Client:** Spring `WebClient`
* **Caching:** Caffeine Cache (In-memory)
* **Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Utils:** Lombok
* **Testing:** JUnit 5, Mockito, StepVerifier

## 📂 Project Structure

* `task1/`: **Asteroids Path** - Calculates the path of asteroids across the Solar System using NASA APIs.
* `task2/`: **Airports & Stations** - Geo-search for airports and weather stations using Aviation Weather Center APIs.

## 🚀 How to Run

### Prerequisites
* Java 21 or higher installed.
* Maven installed.

### Task 1 - Asteroids Path

This service relies on the **NASA NeoWs API**. You need an API Key to run it properly (you can get one at [api.nasa.gov](https://api.nasa.gov)).

1.  Navigate to the project folder:
    ```bash
    cd task1
    ```
2.  Run the application passing your API Key as an argument:
    ```bash
    mvn spring-boot:run -Dspring-boot.run.arguments="--nasa.api.key=YOUR_NASA_API_KEY"
    ```
    *(Alternatively, you can use the default `DEMO_KEY` configured in `application.yml`, but it has rate limits).*

3.  **Explore the API:**
    Open your browser and navigate to the Swagger UI:
    👉 [http://localhost:8080/webjars/swagger-ui/index.html](http://localhost:8080/webjars/swagger-ui/index.html)

### Task 2 - Airports and Stations

This service uses the **Aviation Weather Center API** (no authentication required).

1.  Navigate to the project folder:
    ```bash
    cd task2
    ```
2.  Run the application:
    ```bash
    mvn spring-boot:run
    ```

3.  **Explore the API:**
    Open your browser and navigate to the Swagger UI:
    👉 [http://localhost:8080/webjars/swagger-ui/index.html](http://localhost:8080/webjars/swagger-ui/index.html)

## ✅ Key Features Implemented

* **Reactive Chains:** Usage of `Mono` and `Flux` for full non-blocking I/O.
* **Caching Strategy:** Implementation of Caffeine cache with "Look-aside" pattern using `Mono.defer` for lazy evaluation.
* **Resilience:** Custom exception handling (`@RestControllerAdvice`) to map external API errors (e.g., 404) to clean JSON responses.
* **Data Quality:** Usage of Java Records/Lombok DTOs and defensive programming against null values.
* **Unit Testing:** Extensive testing of Service layers using `StepVerifier` and `Mockito`.

---
