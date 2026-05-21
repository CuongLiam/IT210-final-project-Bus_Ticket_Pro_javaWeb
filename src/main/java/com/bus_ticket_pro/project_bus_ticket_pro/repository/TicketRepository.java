package com.bus_ticket_pro.project_bus_ticket_pro.repository;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketDetailDTO;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Ticket;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.TicketStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatus(TicketStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Ticket t JOIN FETCH t.seat WHERE t.id = :id")
    Optional<Ticket> findByIdForUpdate(@Param("id") Long id);

    @Query("""
            SELECT t FROM Ticket t
            JOIN FETCH t.trip trip
            JOIN FETCH trip.route route
            JOIN FETCH route.fromLocation
            JOIN FETCH route.toLocation
            JOIN FETCH trip.bus
            JOIN FETCH t.seat
            WHERE t.status = :status
            ORDER BY t.createdAt ASC
            """)
    List<Ticket> findByStatusWithDetails(@Param("status") TicketStatus status);

    @Query("""
            SELECT new com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketDetailDTO(
                t.ticketCode,
                t.customerName,
                t.phone,
                t.email,
                fromLoc.name,
                toLoc.name,
                trip.departureTime,
                bus.plateNumber,
                bus.busType,
                bus.driverName,
                seat.seatNumber,
                t.totalPrice,
                t.status
            )
            FROM Ticket t
            JOIN t.trip trip
            JOIN trip.route route
            JOIN route.fromLocation fromLoc
            JOIN route.toLocation toLoc
            JOIN trip.bus bus
            JOIN t.seat seat
            WHERE t.ticketCode = :ticketCode
              AND t.phone = :phone
            """)
    TicketDetailDTO findTicketDetailByCodeAndPhone(
            @Param("ticketCode") String ticketCode,
            @Param("phone") String phone
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT t FROM Ticket t
            JOIN FETCH t.trip trip
            JOIN FETCH t.seat seat
            WHERE t.ticketCode = :ticketCode
              AND t.phone = :phone
            """)
    Optional<Ticket> findByTicketCodeAndPhoneForUpdate(
            @Param("ticketCode") String ticketCode,
            @Param("phone") String phone
    );
}