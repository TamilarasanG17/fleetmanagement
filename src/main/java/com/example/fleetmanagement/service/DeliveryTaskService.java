package com.example.fleetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.fleetmanagement.dto.TaskResponse;
import com.example.fleetmanagement.model.DeliveryTask;
import com.example.fleetmanagement.model.Route;
import com.example.fleetmanagement.repositry.RouteRepository;
import com.example.fleetmanagement.repositry.TaskRepository;

@Service
@RequiredArgsConstructor
public class DeliveryTaskService {

    private final TaskRepository taskRepo;
    private final RouteRepository routeRepo;

    // ✅ CREATE TASK (keep entity return if you want)
    public DeliveryTask createTaskEntity(DeliveryTask task) {
        return taskRepo.save(task);
    }

    // ✅ ASSIGN TASK TO ROUTE (DTO response)
    public TaskResponse assignTask(Long taskId, Long routeId) {

        DeliveryTask task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Route route = routeRepo.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));

        task.setRoute(route);

        DeliveryTask saved = taskRepo.save(task);

        return mapToResponse(saved);
    }

    // ✅ COMMON MAPPING METHOD
    private TaskResponse mapToResponse(DeliveryTask task) {

        return new TaskResponse(
                task.getId(),
                task.getCustomerName(),
                task.getDeliveryAddress(),
                task.getStatus().name(),
                task.getRoute() != null ? task.getRoute().getId() : null,
                task.getRoute() != null ? task.getRoute().getRouteName() : null
        );
    }
}