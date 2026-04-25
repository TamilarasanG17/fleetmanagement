package com.example.fleetmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @NotNull
    @Column(nullable = false)
    private LocalDate licenseExpiryDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DriverStatus status = DriverStatus.AVAILABLE;

    @Min(0)
    @Max(12)
    @Builder.Default
    private Integer shiftHours = 8;

    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    private List<DeliveryTask> deliveryTasks;

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

    public enum DriverStatus {
        AVAILABLE, ON_DUTY, OFF_DUTY, SUSPENDED
    }
}