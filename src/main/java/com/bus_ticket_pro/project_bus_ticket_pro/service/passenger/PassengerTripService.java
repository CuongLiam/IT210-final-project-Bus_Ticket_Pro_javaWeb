package com.bus_ticket_pro.project_bus_ticket_pro.service.passenger;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.seat.SeatViewDTO;
import com.bus_ticket_pro.project_bus_ticket_pro.dto.trip.TripSearchRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;

import java.util.List;

public interface PassengerTripService {

    List<Trip> searchTrips(TripSearchRequest request);

    Trip getTripDetail(Long tripId);

    List<SeatViewDTO> getSeatMap(Long tripId);

}
