package com.example.fleetmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.fleetmanagement.model.DistanceMatrixResponse;

@Service
public class RouteOptimizationService {

    private final RestTemplate restTemplate;

    public RouteOptimizationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Integer> getOptimizedSequence(List<String> coordinates) {

        if (coordinates == null || coordinates.isEmpty()) {
            throw new IllegalArgumentException("Coordinates cannot be empty");
        }

        // OSRM expects: lng,lat
        String coordsString = String.join(";", coordinates);

        String url = "http://router.project-osrm.org/table/v1/driving/"
                + coordsString + "?annotations=duration";

        DistanceMatrixResponse response =
                restTemplate.getForObject(url, DistanceMatrixResponse.class);

        if (response == null || response.getDurations() == null) {
            throw new RuntimeException("Failed to fetch distance matrix");
        }

        return calculateGreedyPath(response.getDurations());
    }

    // Greedy TSP Algorithm
    private List<Integer> calculateGreedyPath(double[][] matrix) {

        int n = matrix.length;
        boolean[] visited = new boolean[n];
        List<Integer> path = new ArrayList<>();

        int current = 0;
        path.add(current);
        visited[current] = true;

        while (path.size() < n) {

            int next = -1;
            double minTime = Double.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!visited[i] && matrix[current][i] < minTime) {
                    minTime = matrix[current][i];
                    next = i;
                }
            }

            if (next == -1) break;

            path.add(next);
            visited[next] = true;
            current = next;
        }

        // return to start (optional but good)
        path.add(path.get(0));

        return path;
    }
}