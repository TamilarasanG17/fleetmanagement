package com.example.fleetmanagement.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;

import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteOptimizationService {

    private final RestTemplate restTemplate;

    // ================= OPTIMIZE =================
    public List<Integer> optimize(@NonNull List<String> coordinates) {

        if (coordinates.isEmpty()) {
            throw new IllegalArgumentException("Coordinates cannot be empty");
        }

        String coords = String.join(";", coordinates);

        String url = "http://router.project-osrm.org/table/v1/driving/"
                + coords + "?annotations=duration";

        HttpMethod method = Objects.requireNonNull(HttpMethod.GET);

        ResponseEntity<Map<String, Object>> entity =
                restTemplate.exchange(
                        url,
                        method,
                        null,
                        new ParameterizedTypeReference<Map<String, Object>>() {}
                );

        final Map<String, Object> response = Optional.ofNullable(entity.getBody())
                .orElseThrow(() -> new RuntimeException("OSRM response body is null"));

        Object durationsObj = response.get("durations");

        if (!(durationsObj instanceof List<?> list)) {
            throw new RuntimeException("Invalid durations data");
        }

        double[][] matrix = convertToMatrix(list);

        return greedy(matrix);
    }

    // ================= MATRIX CONVERSION =================
    private double[][] convertToMatrix(List<?> list) {

        int n = list.size();
        double[][] matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            List<?> row = (List<?>) list.get(i);

            for (int j = 0; j < n; j++) {
                Object value = row.get(j);

                if (!(value instanceof Number)) {
                    throw new RuntimeException("Invalid matrix value");
                }

                matrix[i][j] = ((Number) value).doubleValue();
            }
        }

        return matrix;
    }

    // ================= GREEDY ROUTE =================
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

            if (next == -1) {
                throw new RuntimeException("No valid next route found");
            }

            path.add(next);
            visited[next] = true;
            current = next;
        }

        return path;
    }
}