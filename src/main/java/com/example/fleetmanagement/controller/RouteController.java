package com.example.fleetmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fleetmanagement.service.RouteOptimizationService;

@RestController
@RequestMapping("/api/fleet/routing")
public class RouteController {
    @Autowired
    private RouteOptimizationService optimizationService;

    @PostMapping("/optimize")
    public ResponseEntity<List<Integer>> optimizeRoute(@RequestBody List<String> coordinates) {
        if (coordinates == null || coordinates.size() < 2) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            List<Integer> optimizedIndexOrder = optimizationService.getOptimizedSequence(coordinates);
            return ResponseEntity.ok(optimizedIndexOrder);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
