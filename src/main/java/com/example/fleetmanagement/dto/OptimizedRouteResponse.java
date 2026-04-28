package com.example.fleetmanagement.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptimizedRouteResponse {

    private Long routeId;
    private List<TaskInfo> optimizedOrder;

    @Data
    @AllArgsConstructor
    public static class TaskInfo {
        private Long taskId;
        private String customer;
        private String address;
        private String status;
    }
}
