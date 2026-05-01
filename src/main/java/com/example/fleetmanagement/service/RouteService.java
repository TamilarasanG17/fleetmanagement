package com.example.fleetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.fleetmanagement.dto.ManifestResponse;
import com.example.fleetmanagement.dto.OptimizedRouteResponse;
import com.example.fleetmanagement.model.*;
import com.example.fleetmanagement.repositry.RouteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepo;
    private final RouteOptimizationService optimizer;

    // ================= OPTIMIZE ROUTE =================
    public OptimizedRouteResponse optimizeRoute(long routeId) {

        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        List<DeliveryTask> tasks = route.getDeliveryTasks();

        if (tasks == null || tasks.size() < 2) {
            throw new RuntimeException("Not enough tasks to optimize");
        }

        // Build coordinates safely
        List<String> coords = tasks.stream()
                .map(t -> {
                    if (t.getDeliveryLongitude() == null || t.getDeliveryLatitude() == null) {
                        throw new RuntimeException("Invalid coordinates in task ID: " + t.getId());
                    }
                    return t.getDeliveryLongitude() + "," + t.getDeliveryLatitude();
                })
                .collect(Collectors.toList());

        if (coords.isEmpty()) {
            throw new RuntimeException("Coordinates list is empty");
        }

        List<Integer> order = optimizer.optimize(coords);

        if (order == null || order.isEmpty()) {
            throw new RuntimeException("Optimization failed");
        }

        List<OptimizedRouteResponse.TaskInfo> result = new ArrayList<>();

        for (Integer i : order) {

            if (i == null || i < 0 || i >= tasks.size()) {
                throw new RuntimeException("Invalid optimized index: " + i);
            }

            DeliveryTask t = tasks.get(i);

            result.add(new OptimizedRouteResponse.TaskInfo(
                    t.getId(),
                    t.getCustomerName(),
                    t.getDeliveryAddress(),
                    t.getStatus().name()
            ));
        }

        return new OptimizedRouteResponse(routeId, result);
    }

    // ================= DISPATCH ROUTE =================
    public void dispatchRoute(long routeId) {

        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        List<DeliveryTask> tasks = route.getDeliveryTasks();

        if (tasks == null || tasks.isEmpty()) {
            throw new RuntimeException("No tasks in route");
        }

        route.setStatus(Route.RouteStatus.ACTIVE);
        route.setStartTime(LocalDateTime.now());

        for (DeliveryTask t : tasks) {
            if (t.getStatus() == DeliveryTask.DeliveryStatus.UNASSIGNED) {
                t.setStatus(DeliveryTask.DeliveryStatus.DISPATCHED);
            }
        }

        routeRepo.save(route);
    }

    // ================= GENERATE MANIFEST =================
    public ManifestResponse generateManifest(long routeId) {

        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        if (route.getDriver() == null || route.getVehicle() == null) {
            throw new RuntimeException("Driver or Vehicle not assigned");
        }

        List<DeliveryTask> tasksList = route.getDeliveryTasks();

        if (tasksList == null || tasksList.isEmpty()) {
            throw new RuntimeException("No tasks available");
        }

        List<ManifestResponse.TaskItem> tasks = tasksList.stream()
                .map(t -> new ManifestResponse.TaskItem(
                        t.getId(),
                        t.getCustomerName(),
                        t.getDeliveryAddress(),
                        t.getStatus().name()
                ))
                .collect(Collectors.toList());

        return new ManifestResponse(
                route.getId(),
                route.getRouteName(),
                route.getDriver().getId(),
                route.getDriver().getName(),
                route.getVehicle().getId(),
                route.getVehicle().getLicensePlate(),
                tasks
        );
    }
}