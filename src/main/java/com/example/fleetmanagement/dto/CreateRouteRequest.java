package com.example.fleetmanagement.dto;


import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateRouteRequest {
    private String routeName;
    private LocalDate routeDate;
    private Long vehicleId;
    private Long driverId;
}
