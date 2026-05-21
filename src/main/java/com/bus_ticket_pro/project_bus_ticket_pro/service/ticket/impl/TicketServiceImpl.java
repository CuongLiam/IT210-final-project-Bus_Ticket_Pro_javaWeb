package com.bus_ticket_pro.project_bus_ticket_pro.service.ticket.impl;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.CancelTicketRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketDetailDTO;
import com.bus_ticket_pro.project_bus_ticket_pro.dto.ticket.TicketLookupRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Seat;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Ticket;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.SeatStatus;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.TicketStatus;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.SeatRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.TicketRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.ticket.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelTicket(CancelTicketRequest request) {
        Ticket ticket = ticketRepository.findByTicketCodeAndPhoneForUpdate(
                        request.getTicketCode(),
                        request.getPhone()
                )
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé với mã vé và số điện thoại đã nhập"));

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new RuntimeException("Vé này đã bị hủy trước đó");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureTime = ticket.getTrip().getDepartureTime();

        long hoursUntilDeparture = Duration.between(now, departureTime).toHours();

        if (hoursUntilDeparture < 12) {
            throw new RuntimeException("Không thể hủy vé vì còn dưới 12 tiếng trước giờ khởi hành");
        }

        Seat seat = ticket.getSeat();

        ticket.setStatus(TicketStatus.CANCELLED);
        seat.setStatus(SeatStatus.AVAILABLE);

        ticketRepository.save(ticket);
        seatRepository.save(seat);
    }
}