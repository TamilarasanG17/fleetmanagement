package com.example.fleetmanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.fleetmanagement.dto.TaskResponse;
import com.example.fleetmanagement.model.DeliveryTask;
// import com.example.fleetmanagement.model.Route;
import com.example.fleetmanagement.repositry.TaskRepository;
import com.example.fleetmanagement.service.DeliveryTaskService;
// import com.example.fleetmanagement.repositry.RouteRepository;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class DeliveryTaskController {

    private final TaskRepository taskRepo;
    // private final RouteRepository routeRepo;
    private final DeliveryTaskService taskService;

    // ✅ CREATE TASK
    @PostMapping
    public ResponseEntity<DeliveryTask> createTask(
            @Valid @RequestBody DeliveryTask task) {

        return ResponseEntity.ok(taskRepo.save(task));
    }

    // ✅ ASSIGN TASK TO ROUTE
    @PutMapping("/{taskId}/assign/{routeId}")
    public ResponseEntity<TaskResponse> assignTaskToRoute(
            @PathVariable Long taskId,
            @PathVariable Long routeId) {

        return ResponseEntity.ok(taskService.assignTask(taskId, routeId));
    }
    // @PutMapping("/{taskId}/assign/{routeId}")
    // public ResponseEntity<DeliveryTask> assignTaskToRoute(
    //         @PathVariable Long taskId,
    //         @PathVariable Long routeId) {

    //     DeliveryTask task = taskRepo.findById(taskId)
    //             .orElseThrow(() -> new RuntimeException("Task not found"));

    //     Route route = routeRepo.findById(routeId)
    //             .orElseThrow(() -> new RuntimeException("Route not found"));

    //     task.setRoute(route);

    //     return ResponseEntity.ok(taskRepo.save(task));
    // }
}