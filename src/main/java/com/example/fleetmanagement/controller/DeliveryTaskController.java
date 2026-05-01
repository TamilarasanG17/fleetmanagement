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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Task API", description = "Manage delivery tasks")
public class DeliveryTaskController {

    private final TaskRepository taskRepo;
    // private final RouteRepository routeRepo;
    private final DeliveryTaskService taskService;

    // ✅ CREATE TASK
    @PostMapping
    @Operation(summary = "Create a new delivery task")
    public ResponseEntity<DeliveryTask> createTask(
            @Valid @RequestBody DeliveryTask task) {

        return ResponseEntity.ok(taskRepo.save(task));
    }

    // ✅ ASSIGN TASK TO ROUTE
    @PutMapping("/{taskId}/assign/{routeId}")
    @Operation(summary = "Assign a task to a route")
    public ResponseEntity<TaskResponse> assignTaskToRoute(
            @PathVariable Long taskId,
            @PathVariable Long routeId) {

        return ResponseEntity.ok(taskService.assignTask(taskId, routeId));
    }

    @PutMapping("/{id}/dispatch")
    @Operation(summary = "Dispatch a delivery task")
    public ResponseEntity<TaskResponse> dispatch(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.dispatch(id));
    }

    @PutMapping("/{id}/start")
    @Operation(summary = "Start a delivery task")
    public ResponseEntity<TaskResponse> start(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.startTransit(id));
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete a delivery task")
    public ResponseEntity<TaskResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.complete(id));
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Get the status of a delivery task")
    public ResponseEntity<String> getStatus(@PathVariable Long id) {

        DeliveryTask task = taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        return ResponseEntity.ok(task.getStatus().name());
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