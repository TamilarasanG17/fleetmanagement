package com.example.fleetmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fleetmanagement.model.Driver;
import com.example.fleetmanagement.model.Vehicle;
import com.example.fleetmanagement.repositry.DriverRepository;
import com.example.fleetmanagement.repositry.VehicleRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Fleet API", description = "Driver and Vehicle management")
public class FleetController {

    private final VehicleRepository vehicleRepo;
    private final DriverRepository driverRepo;

    public FleetController(VehicleRepository vehicleRepo, DriverRepository driverRepo) {
        this.vehicleRepo = vehicleRepo;
        this.driverRepo = driverRepo;
    }

    // Register Vehicle
    @PostMapping("/vehicles")
    @Operation(summary = "Register a new vehicle")
    public ResponseEntity<Vehicle> registerVehicle(@Valid @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleRepo.save(vehicle));
    }

    // Create Driver
    @PostMapping("/drivers")
    @Operation(summary = "Create a new driver")
    public ResponseEntity<Driver> createDriver(@Valid @RequestBody Driver driver) {
        return ResponseEntity.ok(driverRepo.save(driver));
    }

    // Assign Driver to Vehicle
    @PutMapping("/drivers/{driverId}/assign/{vehicleId}")
    @Operation(summary = "Assign a driver to a vehicle")
    public ResponseEntity<Vehicle> assignDriver(
            @PathVariable Long driverId,
            @PathVariable Long vehicleId) {

        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // Business validation
        if (driver.getStatus() != Driver.DriverStatus.AVAILABLE) {
            throw new RuntimeException("Driver is not available");
        }

        vehicle.setCurrentDriver(driver);
        driver.setStatus(Driver.DriverStatus.ON_DUTY);

        vehicleRepo.save(vehicle);
        driverRepo.save(driver);

        return ResponseEntity.ok(vehicle);
    }
}