package com.bus_ticket_pro.project_bus_ticket_pro.controller.admin;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.trip.TripRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Bus;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Route;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.BusRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.RouteRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.trip.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/trips")
@RequiredArgsConstructor
public class AdminTripController {

    private final TripService tripService;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;

    @GetMapping
    public String listTrips(Model model) {
        List<Trip> trips = tripService.getAllActiveTrips();
        model.addAttribute("trips", trips);
        return "admin/trip/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("tripRequest", new TripRequest());
        loadFormData(model);
        return "admin/trip/form";
    }

    @PostMapping("/create")
    public String createTrip(
            @Valid @ModelAttribute("tripRequest") TripRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            loadFormData(model);
            return "admin/trip/form";
        }

        try {
            tripService.createTrip(request);
            return "redirect:/admin/trips?created=true";
        } catch (RuntimeException e) {
            loadFormData(model);
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/trip/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Trip trip = tripService.getTripById(id);

        TripRequest request = new TripRequest();
        request.setRouteId(trip.getRoute().getId());
        request.setBusId(trip.getBus().getId());
        request.setDepartureTime(trip.getDepartureTime());
        request.setPrice(trip.getPrice());

        model.addAttribute("tripId", id);
        model.addAttribute("tripRequest", request);
        loadFormData(model);

        return "admin/trip/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateTrip(
            @PathVariable Long id,
            @Valid @ModelAttribute("tripRequest") TripRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tripId", id);
            loadFormData(model);
            return "admin/trip/edit";
        }

        try {
            tripService.updateTrip(id, request);
            return "redirect:/admin/trips?updated=true";
        } catch (RuntimeException e) {
            model.addAttribute("tripId", id);
            loadFormData(model);
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/trip/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return "redirect:/admin/trips?deleted=true";
    }

    private void loadFormData(Model model) {
        List<Route> routes = routeRepository.findAllWithLocations();
        List<Bus> buses = busRepository.findByActiveTrue();

        model.addAttribute("routes", routes);
        model.addAttribute("buses", buses);
    }
}