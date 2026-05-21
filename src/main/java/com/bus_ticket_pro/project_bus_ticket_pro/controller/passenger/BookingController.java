package com.bus_ticket_pro.project_bus_ticket_pro.controller.passenger;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.booking.BookingRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Seat;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Ticket;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.SeatRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.TripRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.booking.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/passenger/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final TripRepository tripRepository;
    private final SeatRepository seatRepository;

    @GetMapping("/create")
    public String createBookingForm(
            @RequestParam Long tripId,
            @RequestParam Long seatId,
            Model model
    ) {
        Trip trip = tripRepository.findByIdWithDetails(tripId);

        if (trip == null) {
            throw new RuntimeException("Không tìm thấy chuyến xe");
        }

        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ghế"));

        BookingRequest request = new BookingRequest();
        request.setTripId(tripId);
        request.setSeatId(seatId);

        model.addAttribute("bookingRequest", request);
        model.addAttribute("trip", trip);
        model.addAttribute("seat", seat);

        return "passenger/booking/create";
    }

    @PostMapping("/create")
    public String createBooking(
            @Valid @ModelAttribute("bookingRequest") BookingRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        Trip trip = tripRepository.findByIdWithDetails(request.getTripId());

        Seat seat = seatRepository.findById(request.getSeatId())
                .orElse(null);

        model.addAttribute("trip", trip);
        model.addAttribute("seat", seat);

        if (bindingResult.hasErrors()) {
            return "passenger/booking/create";
        }

        try {
            Ticket ticket = bookingService.createBooking(request);
            return "redirect:/passenger/bookings/success?ticketCode=" + ticket.getTicketCode();
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "passenger/booking/create";
        }
    }

    @GetMapping("/success")
    public String bookingSuccess(
            @RequestParam String ticketCode,
            Model model
    ) {
        model.addAttribute("ticketCode", ticketCode);
        return "passenger/booking/success";
    }
}