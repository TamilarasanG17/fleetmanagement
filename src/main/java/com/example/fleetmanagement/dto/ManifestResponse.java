package com.example.fleetmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ManifestResponse {

    private Long routeId;
    private String routeName;

    private Long driverId;
    private String driverName;

    private Long vehicleId;
    private String vehicleNumber;

    private List<TaskItem> tasks;

    @Data
    @AllArgsConstructor
    public static class TaskItem {
        private Long taskId;
        private String customerName;
        private String address;
        private String status;
    }
}