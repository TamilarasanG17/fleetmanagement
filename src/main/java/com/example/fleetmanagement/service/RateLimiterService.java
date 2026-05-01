package com.example.fleetmanagement.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimiterService {

    private final Map<String, Integer> requestCount = new HashMap<>();

    private static final int LIMIT = 5;

    public void checkLimit(String ip) {

        requestCount.put(ip, requestCount.getOrDefault(ip, 0) + 1);

        if (requestCount.get(ip) > LIMIT) {
            throw new RuntimeException("Rate limit exceeded");
        }
    }
}
