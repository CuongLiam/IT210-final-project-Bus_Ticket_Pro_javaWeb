package com.bus_ticket_pro.project_bus_ticket_pro.repository;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
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

    @Query("""
            SELECT t FROM Trip t
            JOIN FETCH t.route r
            JOIN FETCH r.fromLocation
            JOIN FETCH r.toLocation
            JOIN FETCH t.bus
            WHERE t.active = true
              AND r.fromLocation.id = :fromId
              AND r.toLocation.id = :toId
              AND t.departureTime >= :startOfDay
              AND t.departureTime < :endOfDay
            ORDER BY t.departureTime ASC
            """)
    List<Trip> searchTrips(
            @Param("fromId") Long fromId,
            @Param("toId") Long toId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    @Query("""
            SELECT t FROM Trip t
            JOIN FETCH t.route r
            JOIN FETCH r.fromLocation
            JOIN FETCH r.toLocation
            JOIN FETCH t.bus
            WHERE t.id = :id
            """)
    Trip findByIdWithDetails(@Param("id") Long id);
}