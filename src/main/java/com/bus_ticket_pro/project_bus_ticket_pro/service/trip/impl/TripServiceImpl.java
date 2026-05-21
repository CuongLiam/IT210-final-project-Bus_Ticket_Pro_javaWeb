package com.bus_ticket_pro.project_bus_ticket_pro.service.trip.impl;

import com.bus_ticket_pro.project_bus_ticket_pro.dto.trip.TripRequest;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Bus;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Route;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Seat;
import com.bus_ticket_pro.project_bus_ticket_pro.entity.Trip;
import com.bus_ticket_pro.project_bus_ticket_pro.enums.SeatStatus;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.BusRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.RouteRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.SeatRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.repository.TripRepository;
import com.bus_ticket_pro.project_bus_ticket_pro.service.trip.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final BusRepository busRepository;
    private final SeatRepository seatRepository;

    @Override
    public List<Trip> getAllActiveTrips() {
        return tripRepository.findAllActiveWithDetails();
    }

    @Override
    public Trip getTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyến xe"));
    }

    @Override
    @Transactional
    public void createTrip(TripRequest request) {
        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tuyến đường"));

        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        if (!Boolean.TRUE.equals(bus.getActive())) {
            throw new RuntimeException("Xe này đã bị xóa hoặc không hoạt động");
        }

        Trip trip = new Trip();
        trip.setRoute(route);
        trip.setBus(bus);
        trip.setDepartureTime(request.getDepartureTime());
        trip.setPrice(request.getPrice());
        trip.setActive(true);

        Trip savedTrip = tripRepository.save(trip);

        generateSeatsForTrip(savedTrip, bus.getTotalSeats());
    }

    @Override
    @Transactional
    public void updateTrip(Long id, TripRequest request) {
        Trip trip = getTripById(id);

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tuyến đường"));

        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        if (!Boolean.TRUE.equals(bus.getActive())) {
            throw new RuntimeException("Xe này đã bị xóa hoặc không hoạt động");
        }

        trip.setRoute(route);
        trip.setBus(bus);
        trip.setDepartureTime(request.getDepartureTime());
        trip.setPrice(request.getPrice());

        tripRepository.save(trip);

        /*
         * Lưu ý:
         * Không tự sinh lại ghế khi sửa trip.
         * Vì nếu chuyến đã có vé, xóa/sinh lại ghế sẽ làm hỏng dữ liệu booking.
         * Nếu muốn đổi xe sau khi đã có vé thì cần logic riêng.
         */
    }

    @Override
    @Transactional
    public void deleteTrip(Long id) {
        Trip trip = getTripById(id);

        /*
         * Soft delete.
         * Không xóa cứng vì sau này tickets sẽ tham chiếu trip.
         */
        trip.setActive(false);

        tripRepository.save(trip);
    }

    private void generateSeatsForTrip(Trip trip, Integer totalSeats) {
        List<Seat> seats = new ArrayList<>();

        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setTrip(trip);
            seat.setSeatNumber(generateSeatNumber(i));
            seat.setStatus(SeatStatus.AVAILABLE);

            seats.add(seat);
        }

        seatRepository.saveAll(seats);
    }

    private String generateSeatNumber(int index) {
        return "S" + String.format("%02d", index);
    }
}