package com.bus_ticket_pro.project_bus_ticket_pro.service.trip;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.trip.TripRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;

import java.util.List;

public interface TripService {

    List<Trip> getAllActiveTrips();

    Trip getTripById(Long id);

    void createTrip(TripRequest request);

    void updateTrip(Long id, TripRequest request);

    void deleteTrip(Long id);
}