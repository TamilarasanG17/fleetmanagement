package com.example.fleetmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {

    private Long taskId;
    private String customerName;
    private String deliveryAddress;
    private String status;

    private Long routeId;
    private String routeName;
}