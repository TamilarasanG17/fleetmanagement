package com.example.fleetmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true) // Ignores extra data from OSRM like 'code' or 'sources'
public class DistanceMatrixResponse {
    private double[][] durations;
}
