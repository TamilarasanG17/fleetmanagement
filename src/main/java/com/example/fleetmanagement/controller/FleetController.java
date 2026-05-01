package com.example.fleetmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // ============================
    // 🚗 Register Vehicle
    // ============================
    @PostMapping("/vehicles")
    @Operation(summary = "Register a new vehicle")
    public ResponseEntity<?> registerVehicle(@Valid @RequestBody Vehicle vehicle) {

        if (vehicle == null) {
            return ResponseEntity.badRequest().body("Vehicle data cannot be null");
        }

        Vehicle savedVehicle = vehicleRepo.save(vehicle);
        return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
    }

    // ============================
    // 👨‍✈️ Create Driver
    // ============================
    @PostMapping("/drivers")
    @Operation(summary = "Create a new driver")
    public ResponseEntity<?> createDriver(@Valid @RequestBody Driver driver) {

        if (driver == null) {
            return ResponseEntity.badRequest().body("Driver data cannot be null");
        }

        Driver savedDriver = driverRepo.save(driver);
        return new ResponseEntity<>(savedDriver, HttpStatus.CREATED);
    }

    // ============================
    // 🔗 Assign Driver to Vehicle
    // ============================
    @PutMapping("/drivers/{driverId}/assign/{vehicleId}")
    @Operation(summary = "Assign a driver to a vehicle")
    public ResponseEntity<?> assignDriver(
            @PathVariable Long driverId,
            @PathVariable Long vehicleId) {

        // Validate IDs
        if (driverId == null || vehicleId == null) {
            return ResponseEntity.badRequest().body("Driver ID and Vehicle ID must not be null");
        }

        // Fetch vehicle
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));

        // Fetch driver
        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));

        // Business validation
        if (driver.getStatus() != Driver.DriverStatus.AVAILABLE) {
            return ResponseEntity.badRequest().body("Driver is not available");
        }

        // Assign driver
        vehicle.setCurrentDriver(driver);
        driver.setStatus(Driver.DriverStatus.ON_DUTY);

        // Save updates
        vehicleRepo.save(vehicle);
        driverRepo.save(driver);

        return ResponseEntity.ok(vehicle);
    }
}