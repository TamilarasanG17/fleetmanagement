package com.example.fleetmanagement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteResponse {

    private Long vehicleId;
    private List<Integer> optimizedSequence;
}