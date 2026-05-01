package com.example.fleetmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.fleetmanagement.dto.CreateRouteRequest;
import com.example.fleetmanagement.dto.ManifestResponse;
import com.example.fleetmanagement.dto.OptimizedRouteResponse;
import com.example.fleetmanagement.model.Driver;
import com.example.fleetmanagement.model.Route;
import com.example.fleetmanagement.model.Vehicle;
import com.example.fleetmanagement.repositry.DriverRepository;
import com.example.fleetmanagement.repositry.RouteRepository;
import com.example.fleetmanagement.repositry.VehicleRepository;
import com.example.fleetmanagement.service.RateLimiterService;
import com.example.fleetmanagement.service.RouteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Route API", description = "Manage routes and optimization")
@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final VehicleRepository vehicleRepo;
    private final DriverRepository driverRepo;
    private final RouteService routeService;
    private final RouteRepository routeRepo;
    private final RateLimiterService rateLimiter;

    // ✅ CREATE ROUTE
   @PostMapping
   @Operation(summary = "Create a new route")
    public ResponseEntity<Route> createRoute(@Valid @RequestBody CreateRouteRequest req) {

        long vehicleId = req.getVehicleId();
        long driverId = req.getDriverId();

        Vehicle vehicle = vehicleRepo.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));

        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found with ID: " + driverId));

        Route route = new Route();
        route.setRouteName(req.getRouteName());
        route.setRouteDate(req.getRouteDate());
        route.setVehicle(vehicle);
        route.setDriver(driver);

        return new ResponseEntity<>(routeRepo.save(route), HttpStatus.CREATED);
    }

    // ✅ OPTIMIZE ROUTE
    @GetMapping("/{id}/optimize")
    @Operation(summary = "Optimize a route")
    public ResponseEntity<OptimizedRouteResponse> optimize(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.optimizeRoute(id));
    }

    // ✅ DISPATCH ROUTE
    @PutMapping("/{id}/dispatch")
    @Operation(summary = "Dispatch a route")
    public ResponseEntity<String> dispatchRoute(@PathVariable Long id) {
        routeService.dispatchRoute(id);
        return ResponseEntity.ok("Route dispatched successfully");
    }

    // ✅ GENERATE MANIFEST (WITH RATE LIMIT)
    @GetMapping("/{id}/manifest")
    @Operation(summary = "Generate manifest for a route")
    public ResponseEntity<ManifestResponse> manifest(
            @PathVariable Long id,
            HttpServletRequest request) {

        rateLimiter.checkLimit(request.getRemoteAddr());

        return ResponseEntity.ok(routeService.generateManifest(id));
    }
}