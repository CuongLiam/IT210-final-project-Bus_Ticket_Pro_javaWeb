package com.bus_ticket_pro.project_bus_ticket_pro.service.booking;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.booking.BookingRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Ticket;

public interface BookingService {

    Ticket createBooking(BookingRequest request);
}