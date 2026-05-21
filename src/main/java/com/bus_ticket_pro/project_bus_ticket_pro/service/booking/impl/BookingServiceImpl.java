package com.bus_ticket_pro.project_bus_ticket_pro.service.booking.impl;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.booking.BookingRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Seat;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Ticket;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.SeatStatus;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.TicketStatus;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.SeatRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.TicketRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.TripRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.booking.BookingService;
import com.bus_ticket_pro.project_bus_ticket_pro.util.TicketCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final TripRepository tripRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional(rollbackFor = Exception.class) // important
    public Ticket createBooking(BookingRequest request) {

        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến xe"));

        // important
        Seat seat = seatRepository.findByIdForUpdate(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghế"));

        if (!seat.getTrip().getId().equals(trip.getId())) {
            throw new RuntimeException("Ghế không thuộc chuyến xe này");
        }

        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            throw new RuntimeException("Ghế đã có người đặt, vui lòng chọn ghế khác");
        }

        Ticket ticket = new Ticket();
        ticket.setTicketCode(TicketCodeGenerator.generate());
        ticket.setCustomerName(request.getCustomerName());
        ticket.setPhone(request.getPhone());
        ticket.setEmail(request.getEmail());
        ticket.setTrip(trip);
        ticket.setSeat(seat);
        ticket.setTotalPrice(trip.getPrice());
        ticket.setStatus(TicketStatus.PENDING);

        Ticket savedTicket = ticketRepository.save(ticket);

        seat.setStatus(SeatStatus.PENDING);
        seatRepository.save(seat);

        return savedTicket;
    }
}