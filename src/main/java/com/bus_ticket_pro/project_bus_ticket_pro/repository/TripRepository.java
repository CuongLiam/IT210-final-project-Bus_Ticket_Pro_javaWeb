package com.bus_ticket_pro.project_bus_ticket_pro.repository;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    @Query("""
            SELECT t FROM Trip t
            JOIN FETCH t.route r
            JOIN FETCH r.fromLocation
            JOIN FETCH r.toLocation
            JOIN FETCH t.bus
            WHERE t.active = true
            ORDER BY t.departureTime DESC
            """)
    List<Trip> findAllActiveWithDetails();
}