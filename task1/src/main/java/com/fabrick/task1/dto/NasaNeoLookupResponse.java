package com.fabrick.task1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NasaNeoLookupResponse {
    String id;
    String name;
    @JsonProperty("close_approach_data")
    List<NasaCloseApproachData> closeApproachData;
}
