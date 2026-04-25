package com.example.fleetmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fleetmanagement.model.Driver;
import com.example.fleetmanagement.model.Route;
import com.example.fleetmanagement.model.Vehicle;
import com.example.fleetmanagement.repositry.DriverRepository;
import com.example.fleetmanagement.repositry.RouteRepository;
import com.example.fleetmanagement.repositry.VehicleRepository;
import com.example.fleetmanagement.service.RouteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final VehicleRepository vehicleRepo;
    private final DriverRepository driverRepo;
    private final RouteService routeService;
     private final RouteRepository routeRepo;

    @PostMapping
public ResponseEntity<Route> createRoute(@RequestBody Route route) {

    Vehicle vehicle = vehicleRepo.findById(route.getVehicle().getId())
            .orElseThrow(() -> new RuntimeException("Vehicle not found"));

    Driver driver = driverRepo.findById(route.getDriver().getId())
            .orElseThrow(() -> new RuntimeException("Driver not found"));

    route.setVehicle(vehicle);
    route.setDriver(driver);

    return ResponseEntity.ok(routeRepo.save(route));
}


    @GetMapping("/{id}/optimize")
    public List<Integer> optimize(@PathVariable Long id) {
        return routeService.optimizeRoute(id);
    }
}