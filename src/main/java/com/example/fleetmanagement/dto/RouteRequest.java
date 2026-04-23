package com.example.fleetmanagement.dto;

import java.util.List;

import lombok.Data;

@Data
public class RouteRequest {

    private Long vehicleId;
    private List<String> coordinates; // format: "lng,lat"

}