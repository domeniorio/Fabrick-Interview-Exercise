package com.fabrick.task2.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dettagli relativi ad un Aeroporto")
public class AirportDTO {

    @Schema(description = "Codice ICAO dell'aeroporto", example = "KAFF")
    @JsonProperty("icaoId")
    private String id;

    @Schema(description = "Nome completo dell'aeroporto", example = "Air Force Academy Arfld")
    private String name;

    @Schema(description = "Sigla dello stato", example = "CO")
    private String state;

    @Schema(description = "Codice della nazione", example = "US")
    private String country;

    @Schema(description = "Latitudine in gradi decimali", example = "38.971")
    @JsonAlias({"lat", "latitude"})
    private Double latitude;

    @Schema(description = "Longitudine in gradi decimali", example = "-104.816")
    @JsonAlias({"lon", "longitude"})
    private Double longitude;

    @Schema(description = "Altitudine in metri", example = "2003.0")
    @JsonAlias({"elev", "elevation"})
    private Double elevation;
}