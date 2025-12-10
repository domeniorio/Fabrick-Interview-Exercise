package com.fabrick.task1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Oggetto che rappresenta un segmento del percorso di un asteroide tra due pianeti")
public class AsteroidPathDTO {

    @Schema(description = "Il pianeta di partenza del segmento", example = "Juptr")
    private String fromPlanet;

    @Schema(description = "Il pianeta di destinazione del segmento", example = "Earth")
    private String toPlanet;

    @Schema(description = "Data di inizio del passaggio sul pianeta di partenza (YYYY-MM-DD)", example = "1917-04-30")
    private String fromDate;

    @Schema(description = "Data di arrivo sul pianeta di destinazione (YYYY-MM-DD)", example = "1930-06-01")
    private String toDate;
}