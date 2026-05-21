package com.bus_ticket_pro.project_bus_ticket_pro.service.payment.impl;

import com.bus_ticket_pro.project_bus_ticket_pro.entity.Seat;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Ticket;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.SeatStatus;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.TicketStatus;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.SeatRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.TicketRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    @Override
    public List<Ticket> getPendingTickets() {
        return ticketRepository.findByStatusWithDetails(TicketStatus.PENDING);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPayment(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdForUpdate(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Vé đã được xử lý, không thể xác nhận thanh toán");
        }

        Seat seat = ticket.getSeat();

        if (seat.getStatus() != SeatStatus.PENDING) {
            throw new RuntimeException("Ghế không ở trạng thái chờ thanh toán");
        }

        ticket.setStatus(TicketStatus.PAID);
        seat.setStatus(SeatStatus.BOOKED);

        ticketRepository.save(ticket);
        seatRepository.save(seat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPendingTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findByIdForUpdate(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vé"));

        if (ticket.getStatus() != TicketStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy vé đang chờ thanh toán");
        }

        Seat seat = ticket.getSeat();

        ticket.setStatus(TicketStatus.CANCELLED);
        seat.setStatus(SeatStatus.AVAILABLE);

        ticketRepository.save(ticket);
        seatRepository.save(seat);
    }
}