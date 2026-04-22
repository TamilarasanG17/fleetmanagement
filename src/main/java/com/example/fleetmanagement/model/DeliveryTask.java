package com.example.fleetmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "delivery_tasks")
public class DeliveryTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destinationAddress;
    private String status; // e.g., "UNASSIGNED"

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
