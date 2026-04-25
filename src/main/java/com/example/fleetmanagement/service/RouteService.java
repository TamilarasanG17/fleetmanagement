package com.example.fleetmanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.fleetmanagement.model.DeliveryTask;
import com.example.fleetmanagement.repositry.RouteRepository;
import com.example.fleetmanagement.repositry.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final TaskRepository taskRepo;
    private final RouteRepository routeRepo;
    private final RouteOptimizationService optimizer;

    public List<Integer> optimizeRoute(Long routeId) {

        List<DeliveryTask> tasks = taskRepo.findByRouteId(routeId);

        List<String> coords = tasks.stream()
                .map(t -> t.getDeliveryLongitude() + "," + t.getDeliveryLatitude())
                .collect(Collectors.toList());

        return optimizer.optimize(coords);
    }
}