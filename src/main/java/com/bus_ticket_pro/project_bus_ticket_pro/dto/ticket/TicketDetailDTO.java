package com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket;

import com.bus_ticket_pro.project_bus_ticket_pro.enums.BusType;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TicketDetailDTO {

    private String ticketCode;

    private String customerName;

    private String phone;

    private String email;

    private String fromLocation;

    private String toLocation;

    private LocalDateTime departureTime;

    private String plateNumber;

    private BusType busType;

    private String driverName;

    private String seatNumber;

    private BigDecimal totalPrice;

    private TicketStatus status;
}