package com.bus_ticket_pro.project_bus_ticket_pro.service.passenger.impl;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.seat.SeatViewDTO;
import com.bus_ticket_pro.project_bus_ticket_pro.dto.trip.TripSearchRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Seat;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.SeatRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.TripRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.passenger.PassengerTripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerTripServiceImpl implements PassengerTripService {

    private final TripRepository tripRepository;
    private final SeatRepository seatRepository;


    @Override
    public List<Trip> searchTrips(TripSearchRequest request) {
        LocalDateTime startOfDay = request.getDepartureDate().atStartOfDay();
        LocalDateTime endOfDat = request.getDepartureDate().plusDays(1).atStartOfDay();

        return tripRepository.searchTrips(
                request.getFromLocationId(),
                request.getToLocationId(),
                startOfDay,
                endOfDat
        );
    }

    @Override
    public Trip getTripDetail(Long tripId) {

        Trip trip = tripRepository.findByIdWithDetails(tripId);

        if (trip == null){
            throw new RuntimeException("ko tìm thấy chuyến xe");
        }

        return trip;
    }

    @Override
    public List<SeatViewDTO> getSeatMap(Long tripId) {

        List<Seat> seats = seatRepository.findByTripIdOrderBySeatNumberAsc(tripId);

        return seats.stream()
                .map(seat -> new SeatViewDTO(
                        seat.getId(),
                        seat.getSeatNumber(),
                        seat.getStatus()
                ))
                .toList();
    }
}
