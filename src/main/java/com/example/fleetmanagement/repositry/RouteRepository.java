package com.example.fleetmanagement.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fleetmanagement.model.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
}
