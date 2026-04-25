package com.example.fleetmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.example.fleetmanagement.service.RouteService;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping("/{id}/optimize")
    public List<Integer> optimize(@PathVariable Long id) {
        return routeService.optimizeRoute(id);
    }
}