package com.bus_ticket_pro.project_bus_ticket_pro.repository;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketDetailDTO;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Ticket;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatus(TicketStatus status);

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
                CAST(bus.busType AS string),
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
}