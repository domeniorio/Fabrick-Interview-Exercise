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
@Schema(description = "Dettagli relativi ad una Stazione Meteo")
public class StationDTO {

    @Schema(description = "Codice identificativo della stazione", example = "KDEN")
    @JsonProperty("icaoId")
    private String id;

    @Schema(description = "Nome del sito o della località", example = "DENVER/DENVER_INTL")
    private String site;

    @Schema(description = "Sigla dello stato", example = "CO")
    private String state;

    @Schema(description = "Codice della nazione", example = "US")
    private String country;

    @Schema(description = "Latitudine in gradi decimali", example = "39.8617")
    @JsonAlias({"lat", "latitude"})
    private Double latitude;

    @Schema(description = "Longitudine in gradi decimali", example = "-104.6732")
    @JsonAlias({"lon", "longitude"})
    private Double longitude;

    @Schema(description = "Altitudine in metri", example = "1656.6")
    @JsonAlias({"elev", "elevation"})
    private Double elevation;
}