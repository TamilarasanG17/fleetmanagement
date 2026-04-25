package com.example.fleetmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String routeName;

    @NotNull
    private LocalDate routeDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Driver driver;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private List<DeliveryTask> deliveryTasks;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RouteStatus status = RouteStatus.PLANNED;

    private Double estimatedDistanceKm;
    private Integer estimatedDurationMinutes;
    private Double totalCargoWeightKg;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum RouteStatus {
        PLANNED, ACTIVE, COMPLETED, CANCELLED
    }
}