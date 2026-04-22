package com.example.fleetmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/fleet")
public class FleetController {

    @Autowired
    private VehicleRepository vehicleRepo;
    
    @Autowired
    private DriverRepository driverRepo;

    // Register a new vehicle
    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> registerVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleRepo.save(vehicle));
    }

    // Assign a driver to a vehicle
    @PutMapping("/drivers/{driverId}/assign/{vehicleId}")
    public ResponseEntity<Driver> assignDriver(@PathVariable Long driverId, @PathVariable Long vehicleId) {
        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        
        driver.setVehicle(vehicle);
        return ResponseEntity.ok(driverRepo.save(driver));
    }
}
