package com.example.fleetmanagement.service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

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

    // DISPATCHED: driver heading to pickup
    public TaskResponse dispatch(Long taskId) {
        DeliveryTask task = get(taskId);

        if (task.getStatus() != DeliveryTask.DeliveryStatus.UNASSIGNED) {
            throw new IllegalStateException("Only UNASSIGNED task can be DISPATCHED");
        }

        task.setStatus(DeliveryTask.DeliveryStatus.DISPATCHED);
        return mapToResponse(taskRepo.save(task));
    }

    // IN_TRANSIT: pickup done
    public TaskResponse startTransit(Long taskId) {
        DeliveryTask task = get(taskId);

        if (task.getStatus() != DeliveryTask.DeliveryStatus.DISPATCHED) {
            throw new IllegalStateException("Only DISPATCHED task can be IN_TRANSIT");
        }

        task.setStatus(DeliveryTask.DeliveryStatus.IN_TRANSIT);
        task.setActualPickupTime(LocalDateTime.now());
        return mapToResponse(taskRepo.save(task));
    }

    // DELIVERED: delivery completed
    public TaskResponse complete(Long taskId) {
        DeliveryTask task = get(taskId);

        if (task.getStatus() != DeliveryTask.DeliveryStatus.IN_TRANSIT) {
            throw new IllegalStateException("Only IN_TRANSIT task can be DELIVERED");
        }

        task.setStatus(DeliveryTask.DeliveryStatus.DELIVERED);
        task.setActualDeliveryTime(LocalDateTime.now());
        return mapToResponse(taskRepo.save(task));
    }

    // CANCEL (from any state)
    public TaskResponse cancel(Long taskId) {
        DeliveryTask task = get(taskId);
        task.setStatus(DeliveryTask.DeliveryStatus.CANCELLED);
        return mapToResponse(taskRepo.save(task));
    }

    // -------- helpers --------
    private DeliveryTask get(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }


    //  COMMON MAPPING METHOD
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