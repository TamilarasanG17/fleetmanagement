package com.example.fleetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.fleetmanagement.model.*;
import com.example.fleetmanagement.repositry.RouteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepo;
    private final RouteOptimizationService optimizer;

    public List<Integer> optimizeRoute(Long routeId) {

        // Fetch route
        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        List<DeliveryTask> tasks = route.getDeliveryTasks();

        if (tasks == null || tasks.size() < 2) {
            throw new RuntimeException("Not enough tasks to optimize");
        }

        // Convert to OSRM format (lng,lat)
        List<String> coords = tasks.stream()
                .map(t -> t.getDeliveryLongitude() + "," + t.getDeliveryLatitude())
                .collect(Collectors.toList());

        // FIXED method call
        return optimizer.optimize(coords);
    }
}