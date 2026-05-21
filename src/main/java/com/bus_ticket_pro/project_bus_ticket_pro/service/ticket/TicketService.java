package com.bus_ticket_pro.project_bus_ticket_pro.service.ticket;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketDetailDTO;
import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketLookupRequest;

public interface TicketService {

    TicketDetailDTO lookupTicket(TicketLookupRequest request);
}