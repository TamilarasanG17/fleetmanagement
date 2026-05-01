package com.example.fleetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.fleetmanagement.dto.ManifestResponse;
import com.example.fleetmanagement.dto.OptimizedRouteResponse;
import com.example.fleetmanagement.model.*;
import com.example.fleetmanagement.repositry.RouteRepository;

import java.time.LocalDateTime;
import java.util.List;
// import java.util.stream.Collectors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepo;
    private final RouteOptimizationService optimizer;

    // public List<Integer> optimizeRoute(Long routeId) {

    //     // Fetch route
    //     Route route = routeRepo.findById(routeId)
    //             .orElseThrow(() -> new RuntimeException("Route not found"));

    //     List<DeliveryTask> tasks = route.getDeliveryTasks();

    //     if (tasks == null || tasks.size() < 2) {
    //         throw new RuntimeException("Not enough tasks to optimize");
    //     }

    //     // Convert to OSRM format (lng,lat)
    //     List<String> coords = tasks.stream()
    //             .map(t -> t.getDeliveryLongitude() + "," + t.getDeliveryLatitude())
    //             .collect(Collectors.toList());

    //     // FIXED method call
    //     return optimizer.optimize(coords);
    // }

    public OptimizedRouteResponse optimizeRoute(Long routeId) {

        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        List<DeliveryTask> tasks = route.getDeliveryTasks();

        if (tasks == null || tasks.size() < 2) {
            throw new RuntimeException("Not enough tasks to optimize");
        }

        // Convert to coordinates
        List<String> coords = tasks.stream()
                .map(t -> t.getDeliveryLongitude() + "," + t.getDeliveryLatitude())
                .toList();

        // Get index order
        List<Integer> order = optimizer.optimize(coords);

        // Convert index → task details
        List<OptimizedRouteResponse.TaskInfo> result = new java.util.ArrayList<>();

        for (Integer i : order) {
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

      // 🚚 DISPATCH ROUTE
    public void dispatchRoute(Long routeId) {

        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        if (route.getDeliveryTasks() == null || route.getDeliveryTasks().isEmpty()) {
            throw new RuntimeException("No tasks in route");
        }

        route.setStatus(Route.RouteStatus.ACTIVE);
        route.setStartTime(LocalDateTime.now());

        for (DeliveryTask t : route.getDeliveryTasks()) {
            if (t.getStatus() == DeliveryTask.DeliveryStatus.UNASSIGNED) {
                t.setStatus(DeliveryTask.DeliveryStatus.DISPATCHED);
            }
        }

        routeRepo.save(route);
    }

    // 📋 GENERATE MANIFEST
    public ManifestResponse generateManifest(Long routeId) {

        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        List<ManifestResponse.TaskItem> tasks =
                route.getDeliveryTasks().stream()
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