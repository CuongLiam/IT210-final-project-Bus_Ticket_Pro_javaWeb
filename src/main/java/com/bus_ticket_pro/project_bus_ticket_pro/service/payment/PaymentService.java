package com.bus_ticket_pro.project_bus_ticket_pro.service.payment;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.Ticket;

import java.util.List;

public interface PaymentService {

    List<Ticket> getPendingTickets();

    void confirmPayment(Long ticketId);

    void cancelPendingTicket(Long ticketId);
}