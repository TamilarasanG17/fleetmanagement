package com.example.fleetmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RouteResponse {
    private Long routeId;
    private List<Integer> optimizedSequence;
    private double totalTime;
}