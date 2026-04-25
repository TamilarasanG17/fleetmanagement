package com.infotact.fleetmanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteOptimizationService {

    private final RestTemplate restTemplate;

    public List<Integer> optimize(List<String> coordinates) {

        String coords = String.join(";", coordinates);

        String url = "http://router.project-osrm.org/table/v1/driving/"
                + coords + "?annotations=duration";

        double[][] matrix = restTemplate.getForObject(url, Map.class)
                .get("durations") instanceof List<?> list
                ? convertToMatrix(list)
                : null;

        if (matrix == null) throw new RuntimeException("Invalid matrix");

        return greedy(matrix);
    }

    private double[][] convertToMatrix(List<?> list) {
        int n = list.size();
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            List<?> row = (List<?>) list.get(i);
            for (int j = 0; j < n; j++) {
                matrix[i][j] = ((Number) row.get(j)).doubleValue();
            }
        }
        return matrix;
    }

    private List<Integer> greedy(double[][] matrix) {

        int n = matrix.length;
        boolean[] visited = new boolean[n];
        List<Integer> path = new ArrayList<>();

        int current = 0;
        path.add(current);
        visited[current] = true;

        while (path.size() < n) {

            int next = -1;
            double min = Double.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!visited[i] && matrix[current][i] < min) {
                    min = matrix[current][i];
                    next = i;
                }
            }

            path.add(next);
            visited[next] = true;
            current = next;
        }

        return path;
    }
}