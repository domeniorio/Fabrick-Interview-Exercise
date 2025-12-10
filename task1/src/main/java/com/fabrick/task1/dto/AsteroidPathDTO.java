package com.fabrick.task1.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsteroidPathDTO {
    String fromPlanet;
    String toPlanet;
    String fromDate;
    String toDate;
}
