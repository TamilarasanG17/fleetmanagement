package com.example.fleetmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String licensePlate;

    @NotBlank
    @Column(nullable = false)
    private String model;

    @NotBlank
    @Column(nullable = false)
    private String make;

    @Min(2000)
    @Max(2100)
    private Integer manufactureYear;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @NotNull
    @Column(nullable = false)
    private Double maxCapacityKg;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MaintenanceStatus maintenanceStatus = MaintenanceStatus.OPERATIONAL;

    private LocalDate lastServiceDate;
    private LocalDate nextServiceDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VehicleStatus status = VehicleStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_driver_id")
    @JsonIgnoreProperties({"currentDriver", "deliveryTasks", "routes"})
    private Driver currentDriver;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<DeliveryTask> deliveryTasks;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<Route> routes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.maintenanceStatus == null) {
            this.maintenanceStatus = MaintenanceStatus.OPERATIONAL;
        }

        if (this.status == null) {
            this.status = VehicleStatus.AVAILABLE;
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum VehicleType {
        MOTORCYCLE, VAN, TRUCK_SMALL, TRUCK_MEDIUM, TRUCK_LARGE, REFRIGERATED
    }

    public enum MaintenanceStatus {
        OPERATIONAL, SCHEDULED_SERVICE, UNDER_MAINTENANCE, DECOMMISSIONED
    }

    public enum VehicleStatus {
        AVAILABLE, IN_USE, OUT_OF_SERVICE
    }
}