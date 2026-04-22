package com.example.fleetmanagement.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fleetmanagement.model.DeliveryTask;

@Repository
public interface TaskRepository extends JpaRepository<DeliveryTask, Long> {}
