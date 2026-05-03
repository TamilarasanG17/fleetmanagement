package com.example.fleetmanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import com.example.fleetmanagement.dto.TaskResponse;
import com.example.fleetmanagement.model.DeliveryTask;
import com.example.fleetmanagement.model.Route;
import com.example.fleetmanagement.repositry.RouteRepository;
import com.example.fleetmanagement.repositry.TaskRepository;

import org.springframework.lang.NonNull;

@Service
@RequiredArgsConstructor
public class DeliveryTaskService {

    private final TaskRepository taskRepo;
    private final RouteRepository routeRepo;

    // ================= CREATE =================
    public DeliveryTask createTask(DeliveryTask task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        return taskRepo.save(task);
    }

    // ASSIGN
    public TaskResponse assignTask(long taskId, long routeId) {

        validateId(taskId, "Task ID");
        validateId(routeId, "Route ID");

        DeliveryTask task = getTask(taskId);
        Route route = getRoute(routeId);

        task.setRoute(route);

        return mapToResponse(taskRepo.save(task));
    }

    // DISPATCH 
    public TaskResponse dispatch(long taskId) {
        DeliveryTask task = getTask(taskId);

        if (task.getStatus() != DeliveryTask.DeliveryStatus.UNASSIGNED) {
            throw new IllegalStateException("Only UNASSIGNED task can be DISPATCHED");
        }

        task.setStatus(DeliveryTask.DeliveryStatus.DISPATCHED);
        return mapToResponse(taskRepo.save(task));
    }

    // START TRANSIT 
    public TaskResponse startTransit(long taskId) {
        DeliveryTask task = getTask(taskId);

        if (task.getStatus() != DeliveryTask.DeliveryStatus.DISPATCHED) {
            throw new IllegalStateException("Only DISPATCHED task can be IN_TRANSIT");
        }

        task.setStatus(DeliveryTask.DeliveryStatus.IN_TRANSIT);
        task.setActualPickupTime(LocalDateTime.now());

        return mapToResponse(taskRepo.save(task));
    }

    // COMPLETE 
    public TaskResponse complete(long taskId) {
        DeliveryTask task = getTask(taskId);

        if (task.getStatus() != DeliveryTask.DeliveryStatus.IN_TRANSIT) {
            throw new IllegalStateException("Only IN_TRANSIT task can be DELIVERED");
        }

        task.setStatus(DeliveryTask.DeliveryStatus.DELIVERED);
        task.setActualDeliveryTime(LocalDateTime.now());

        return mapToResponse(taskRepo.save(task));
    }

    // CANCEL 
    public TaskResponse cancel(long taskId) {
        DeliveryTask task = getTask(taskId);

        task.setStatus(DeliveryTask.DeliveryStatus.CANCELLED);

        return mapToResponse(taskRepo.save(task));
    }

    // ================= GET =================
    private DeliveryTask getTask(@NonNull Long id) {
    validateId(id, "Task ID");

    return taskRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
}

    private Route getRoute(@NonNull Long id) {
        validateId(id, "Route ID");

        return routeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));
    }

    // ================= VALIDATION =================
    private void validateId(Long id, String field) {
        if (id == null) {
            throw new IllegalArgumentException(field + " cannot be null");
        }
    }

    // ================= MAPPING =================
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