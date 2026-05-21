package com.bus_ticket_pro.project_bus_ticket_pro.repository;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("""
            SELECT r FROM Route r
            JOIN FETCH r.fromLocation
            JOIN FETCH r.toLocation
            """)
    List<Route> findAllWithLocations();
}