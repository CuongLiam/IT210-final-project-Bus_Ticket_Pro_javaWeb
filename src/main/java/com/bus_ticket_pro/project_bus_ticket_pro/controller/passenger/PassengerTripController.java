package com.bus_ticket_pro.project_bus_ticket_pro.controller.passenger;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.trip.TripSearchRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.LocationRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.passenger.PassengerTripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/passenger/trips")
@RequiredArgsConstructor
public class PassengerTripController {

    private final PassengerTripService passengerTripService;
    private final LocationRepository locationRepository;

    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("tripSearchRequest", new TripSearchRequest());
        model.addAttribute("locations", locationRepository.findAll());

        return "passenger/trip/search";
    }

    @PostMapping("/search")
    public String searchTrips(
            @Valid @ModelAttribute("tripSearchRequest") TripSearchRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        model.addAttribute("locations", locationRepository.findAll());

        if (bindingResult.hasErrors()) {
            return "passenger/trip/search";
        }

        List<Trip> trips = passengerTripService.searchTrips(request);
        model.addAttribute("trips", trips);

        return "passenger/trip/search";
    }

    @GetMapping("/{tripId}/seats")
    public String seatMap(@PathVariable Long tripId, Model model) {
        Trip trip = passengerTripService.getTripDetail(tripId);

        model.addAttribute("trip", trip);
        model.addAttribute("seats", passengerTripService.getSeatMap(tripId));

        return "passenger/trip/seats";
    }
}