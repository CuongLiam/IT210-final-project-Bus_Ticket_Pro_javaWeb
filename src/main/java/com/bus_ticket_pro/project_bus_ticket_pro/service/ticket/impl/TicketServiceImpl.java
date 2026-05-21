package com.bus_ticket_pro.project_bus_ticket_pro.service.ticket.impl;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketDetailDTO;
import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketLookupRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.TicketRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.ticket.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public TicketDetailDTO lookupTicket(TicketLookupRequest request) {
        TicketDetailDTO detail = ticketRepository.findTicketDetailByCodeAndPhone(
                request.getTicketCode(),
                request.getPhone()
        );

        if (detail == null) {
            throw new RuntimeException("Không tìm thấy vé với mã vé và số điện thoại đã nhập");
        }

        return detail;
    }
}