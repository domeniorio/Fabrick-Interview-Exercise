package com.fabrick.task1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NasaCloseApproachData {
    @JsonProperty("close_approach_date") String closeApproachDate;
    @JsonProperty("orbiting_body") String orbitingBody;

}
