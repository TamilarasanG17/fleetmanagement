package com.example.fleetmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.fleetmanagement.model.DistanceMatrixResponse;

@Service
public class RouteOptimizationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Integer> getOptimizedSequence(List<String> coordinates) {
        // Requirement: Integrate third-party mapping API (OSRM) [cite: 88]
        String coordsString = String.join(";", coordinates);
        String url = "http://router.project-osrm.org/table/v1/driving/" + coordsString + "?annotations=duration";
        
        DistanceMatrixResponse response = restTemplate.getForObject(url, DistanceMatrixResponse.class);
        
        if (response == null || response.getDurations() == null) {
            throw new RuntimeException("Failed to fetch distance matrix");
        }

        return calculateGreedyPath(response.getDurations());
    }

    // Algorithm: Greedy approach for Traveling Salesperson Problem [cite: 75]
    private List<Integer> calculateGreedyPath(double[][] matrix) {
        int n = matrix.length;
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[n];

        int current = 0; // Starting point (usually the Warehouse)
        result.add(current);
        visited[current] = true;

        while (result.size() < n) {
            int next = -1;
            double minTime = Double.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!visited[i] && matrix[current][i] < minTime) {
                    minTime = matrix[current][i];
                    next = i;
                }
            }
            
            if (next != -1) {
                visited[next] = true;
                result.add(next);
                current = next;
            }
        }
        return result;
    }
}
